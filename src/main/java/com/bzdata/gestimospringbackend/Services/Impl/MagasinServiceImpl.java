package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.DTOs.*;
import com.bzdata.gestimospringbackend.Models.Magasin;
import com.bzdata.gestimospringbackend.Models.Villa;
import com.bzdata.gestimospringbackend.Services.MagasinService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.EtageRepository;
import com.bzdata.gestimospringbackend.repository.MagasinRepository;
import com.bzdata.gestimospringbackend.repository.SiteRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.validator.MagasinDtoValidator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MagasinServiceImpl implements MagasinService {

    final MagasinRepository magasinRepository;
    final SiteRepository siteRepository;
    final UtilisateurRepository utilisateurRepository;
    final EtageRepository etageRepository;

    @Override
    public MagasinDto save(MagasinDto dto) {
        log.info("We are going to create  a new Magasin {}", dto);
        List<String> errors = MagasinDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Le magasin n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Magasin sont null.",
                    ErrorCodes.MAGASIN_NOT_VALID, errors);
        }
        SiteResponseDto recoverySite = siteRepository.findById(dto.getSiteRequestDto().getId()).map(SiteResponseDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Site has been found with Code " + dto.getSiteRequestDto().getId(),
                        ErrorCodes.SITE_NOT_FOUND));
        UtilisateurRequestDto utilisateurRequestDto=utilisateurRepository.findById(dto.getUtilisateurRequestDto().getId()).map(UtilisateurRequestDto::fromEntity)
                .orElseThrow(()-> new InvalidEntityException("Aucun Utilisateur has been found with code "+ dto.getUtilisateurRequestDto().getId(),
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
        if(utilisateurRequestDto.getRoleRequestDto().getRoleName().equals("PROPRIETAIRE")){
            dto.setSiteRequestDto(siteRepository.findById(dto.getSiteRequestDto().getId()).map(SiteRequestDto::fromEntity).orElseThrow(
                    () -> new InvalidEntityException("Aucun Site has been found with Code " + dto.getSiteRequestDto().getId(),
                            ErrorCodes.SITE_NOT_FOUND)));

            Long numBien= 0L;
            if (magasinRepository.count()==0) {
                numBien=1L;
            }else {
                numBien=maxOfNumBienMagasin()+1;
            }
            dto.setNumBien(numBien);
            if (!StringUtils.hasLength(dto.getNomMagasin())) {
                dto.setAbrvNomMagasin("magasin-"+dto.getNumBien());
                dto.setNomBien((recoverySite.getNomSite()+"-villa-"+dto.getNumBien()).toUpperCase(Locale.ROOT));
            }else {
                dto.setAbrvNomMagasin("villa-"+dto.getNomMagasin()+"-"+dto.getNumBien());
                dto.setNomBien((recoverySite.getNomSite()+"-villa-"+dto.getNomMagasin()+"-"+dto.getNumBien()).toUpperCase(Locale.ROOT));
            }
            dto.setAbrvBienimmobilier((recoverySite.getAbrSite()+"-"+dto.getAbrvNomMagasin()).toUpperCase(Locale.ROOT));
//            if(dto.getEtageMagasinDto().getId().equals(null) || dto.getEtageMagasinDto()==null ){
//             //   dto.setEtageMagasinDto(null);
//            }else{
            if(dto.getEtageMagasinDto()!=null){
                EtageDto etageDto = etageRepository.findById(dto.getEtageMagasinDto().getId()).map(EtageDto::fromEntity).orElseThrow(
                        () -> new InvalidEntityException("Aucun etage has been found with Code " + dto.getEtageMagasinDto().getId(),
                                ErrorCodes.ETAGE_NOT_FOUND));
                dto.setEtageMagasinDto(etageDto);
            }

            Magasin magasin= magasinRepository.save(MagasinDto.toEntity(dto));
            return MagasinDto.fromEntity(magasin);
        }else{
            throw new InvalidEntityException("L'utilisateur choisi n'a pas un rôle propriétaire, mais pluôt "
                    +utilisateurRequestDto.getRoleRequestDto().getRoleName(),
                    ErrorCodes.UTILISATEUR_NOT_GOOD_ROLE);
        }
    }

    @Override
    public boolean delete(Long id) {
        log.info("We are going to delete a Magasin with the ID {}", id);
        if (id == null) {
            log.error("you are provided a null ID for the Magasin");
            return false;
        }
        boolean exist = magasinRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucune Magasin avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.MAGASIN_NOT_FOUND);
        }
        //TODO
        magasinRepository.deleteById(id);
        return true;
    }

    @Override
    public Long maxOfNumBienMagasin() {
        LongSummaryStatistics collectMaxNumBien = magasinRepository.findAll()
                .stream()
                .collect(Collectors.summarizingLong(Magasin::getNumBien));
        return collectMaxNumBien.getMax();
    }

    @Override
    public List<MagasinDto> findAll() {
        return magasinRepository.findAll(Sort.by(Sort.Direction.ASC, "nomBien")).stream()
                .map(MagasinDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public MagasinDto findById(Long id) {
        log.info("We are going to get back the MagasinDto By {}", id);
        if (id == null) {
            log.error("you are not provided a Villa.");
            return null;
        }
        return magasinRepository.findById(id).map(MagasinDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Studio has been found with Code " + id,
                        ErrorCodes.MAGASIN_NOT_FOUND));
    }

    @Override
    public MagasinDto findByName(String nom) {
        return null;
    }

    @Override
    public List<MagasinDto> findAllBySite(SiteRequestDto siteRequestDto) {
        return null;
    }

    @Override
    public List<MagasinDto> findAllByIdSite(Long id) {
        return null;
    }

    @Override
    public List<MagasinDto> findAllByIdEtage(Long id) {
        return null;
    }
}
