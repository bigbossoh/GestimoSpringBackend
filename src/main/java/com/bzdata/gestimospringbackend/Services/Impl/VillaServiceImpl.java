package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.DTOs.SiteRequestDto;
import com.bzdata.gestimospringbackend.DTOs.SiteResponseDto;
import com.bzdata.gestimospringbackend.DTOs.VillaDto;
import com.bzdata.gestimospringbackend.Models.Villa;
import com.bzdata.gestimospringbackend.Services.VillaService;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.SiteRepository;
import com.bzdata.gestimospringbackend.repository.VillaRepository;
import com.bzdata.gestimospringbackend.validator.VillaDtoValidator;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VillaServiceImpl implements VillaService {

    final VillaRepository villaRepository;
    final SiteRepository siteRepository;
    @Override
    public VillaDto save(VillaDto dto) {
        log.info("We are going to create  a new Villa {}", dto);
        List<String> errors = VillaDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("la Villa n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Villa sont null.",
                    ErrorCodes.VILLA_NOT_VALID, errors);
        }
        SiteResponseDto recoverySite = siteRepository.findById(dto.getSiteRequestDto().getId()).map(SiteResponseDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Site has been found with Code " + dto.getSiteRequestDto().getId(),
                        ErrorCodes.SITE_NOT_FOUND));
        dto.setSiteRequestDto(siteRepository.findById(dto.getSiteRequestDto().getId()).map(SiteRequestDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Site has been found with Code " + dto.getSiteRequestDto().getId(),
                        ErrorCodes.SITE_NOT_FOUND)));

        Long numBien=0L;
        if (!StringUtils.hasLength(countNberOfRecordVilla().toString())) {
            numBien=1L;
            dto.setNumBien(numBien.toString());
        }else {
            numBien=countNberOfRecordVilla()+1;
            dto.setNumBien(numBien.toString());
        }
        if (!StringUtils.hasLength(dto.getNomVilla())) {
            dto.setAbrvVilla("villa-"+dto.getNumBien());
            dto.setNomBien((recoverySite.getNomSite()+"-villa-"+dto.getNumBien()).toUpperCase(Locale.ROOT));
        }else {
            dto.setAbrvVilla("villa-"+dto.getNomVilla()+"-"+dto.getNumBien());
            dto.setNomBien((recoverySite.getNomSite()+"-villa-"+dto.getNomVilla()+"-"+dto.getNumBien()).toUpperCase(Locale.ROOT));
        }
        dto.setAbrvBienimmobilier((recoverySite.getAbrSite()+"-"+dto.getAbrvVilla()).toUpperCase(Locale.ROOT));


        Villa villa= villaRepository.save(VillaDto.toEntity(dto));
        return VillaDto.fromEntity(villa);
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public Long countNberOfRecordVilla() {
        return villaRepository.count();
    }

    @Override
    public List<VillaDto> findAll() {
        return villaRepository.findAll(Sort.by(Sort.Direction.ASC, "nomBien")).stream()
                .map(VillaDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public VillaDto findById(Long id) {
        return null;
    }

    @Override
    public VillaDto findByName(String nom) {
        return null;
    }

    @Override
    public List<VillaDto> findAllBySite(SiteRequestDto siteRequestDto) {
        return null;
    }

    @Override
    public List<VillaDto> findAllByIdSite(Long id) {
        return null;
    }
}
