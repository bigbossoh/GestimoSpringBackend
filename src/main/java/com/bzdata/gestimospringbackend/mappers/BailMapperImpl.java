package com.bzdata.gestimospringbackend.mappers;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.BailAppartementDto;
import com.bzdata.gestimospringbackend.DTOs.BailMagasinDto;
import com.bzdata.gestimospringbackend.DTOs.BailVillaDto;
import com.bzdata.gestimospringbackend.DTOs.LocataireEncaisDTO;
import com.bzdata.gestimospringbackend.DTOs.MontantLoyerBailDto;
import com.bzdata.gestimospringbackend.DTOs.OperationDto;
import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseDto;
import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;
import com.bzdata.gestimospringbackend.Models.SuivieDepense;
import com.bzdata.gestimospringbackend.repository.AppelLoyerRepository;
import com.bzdata.gestimospringbackend.repository.MontantLoyerBailRepository;
import com.bzdata.gestimospringbackend.repository.SuivieDepenseRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class BailMapperImpl {
        final MontantLoyerBailRepository montantLoyerBailRepository;
        final SuivieDepenseRepository suivieDepenseRepository;
        final AppelLoyerRepository appelLoyerRepository;

        // BAIL VILLA MAPPER
        public BailVillaDto fromBailVilla(BailLocation bailLocation) {
                BailVillaDto bailLocaDto = new BailVillaDto();
                BeanUtils.copyProperties(bailLocation, bailLocaDto);
                bailLocaDto.setIdVilla(bailLocation.getBienImmobilierOperation().getId());
                bailLocaDto.setFullNomLocatire(bailLocation.getUtilisateurOperation().getNom() + " "
                                + bailLocation.getUtilisateurOperation().getPrenom());
                bailLocaDto.setIdLocataire(bailLocation.getUtilisateurOperation().getId());
                return bailLocaDto;
        }

        // BAIL MAGASIN MAPPER
        public BailMagasinDto fromBailMagasin(BailLocation bailLocation) {
                BailMagasinDto bailLocaDto = new BailMagasinDto();
                BeanUtils.copyProperties(bailLocation, bailLocaDto);

                bailLocaDto.setNomLocataire(bailLocation.getUtilisateurOperation().getNom() + " "
                                + bailLocation.getUtilisateurOperation().getPrenom());
                bailLocaDto.setIdLocataire(bailLocation.getUtilisateurOperation().getId());
                bailLocaDto.setCodeBien(bailLocation.getBienImmobilierOperation().getCodeAbrvBienImmobilier());
                return bailLocaDto;
        }

        // BAIL MAGASIN MAPPER
        public BailAppartementDto fromBailAppartement(BailLocation bailLocation) {
                BailAppartementDto bailLocaDto = new BailAppartementDto();
                BeanUtils.copyProperties(bailLocation, bailLocaDto);
                bailLocaDto.setIdBienImmobilier(bailLocation.getBienImmobilierOperation().getId());
                bailLocaDto.setNomLocataire(bailLocation.getUtilisateurOperation().getNom() + " "
                                + bailLocation.getUtilisateurOperation().getPrenom());
                bailLocaDto.setIdLocataire(bailLocation.getUtilisateurOperation().getId());
                bailLocaDto.setCodeBien(bailLocation.getBienImmobilierOperation().getCodeAbrvBienImmobilier());
                return bailLocaDto;
        }

        // BAIL MAGASIN MAPPER
        public OperationDto fromOperation(BailLocation bailLocation) {
                OperationDto bailLocaDto = new OperationDto();
                bailLocaDto.setIdFirstAppel(0L);
                List<AppelLoyer> appelLoyers = appelLoyerRepository.findAll()
                                .stream()
                                .filter(b -> b.getBailLocationAppelLoyer() == bailLocation)
                                .filter(mon -> mon.getSoldeAppelLoyer() > 0)
                                .collect(Collectors.toList());
                if (appelLoyers.size() > 0) {
                        bailLocaDto.setIdFirstAppel(appelLoyers.get(0).getId());
                }
                BeanUtils.copyProperties(bailLocation, bailLocaDto);
                bailLocaDto.setIdBienImmobilier(bailLocation.getBienImmobilierOperation().getId());
                bailLocaDto.setUtilisateurOperation(bailLocation.getUtilisateurOperation().getNom() + " "
                                + bailLocation.getUtilisateurOperation().getPrenom());
                bailLocaDto.setIdLocataire(bailLocation.getUtilisateurOperation().getId());
                bailLocaDto
                                .setCodeAbrvBienImmobilier(
                                                bailLocation.getBienImmobilierOperation().getCodeAbrvBienImmobilier());
                Double montantloyer = montantLoyerBailRepository.findAll().stream()
                                .filter(bail -> bail.getBailLocation().equals(bailLocation))
                                .filter(locat -> locat.isStatusLoyer() == true)
                                .mapToDouble(mon -> mon.getNouveauMontantLoyer()).sum();
                bailLocaDto.setNouveauMontantLoyer(montantloyer);
                return bailLocaDto;
        }

        public LocataireEncaisDTO fromOperationBailLocation(BailLocation bailLocation) {
                LocataireEncaisDTO locataireEncaisDTO = new LocataireEncaisDTO();
                locataireEncaisDTO.setId(bailLocation.getUtilisateurOperation().getId());

                locataireEncaisDTO.setNom(bailLocation.getUtilisateurOperation().getNom());
                locataireEncaisDTO.setPrenom(bailLocation.getUtilisateurOperation().getPrenom());
                return locataireEncaisDTO;
        }

        public MontantLoyerBailDto fromMontantLoyerBail(MontantLoyerBail montantLoyerBail) {
                MontantLoyerBailDto montantLoyerBailDto = new MontantLoyerBailDto();
                BeanUtils.copyProperties(montantLoyerBail, montantLoyerBailDto);
                return montantLoyerBailDto;
        }

        // SUIVIE DEPENSE DTO MAPPER
        public SuivieDepenseDto fromSuivieDepense(SuivieDepense suivieDepense) {
                SuivieDepenseDto suivieDepenseDto = new SuivieDepenseDto();
                BeanUtils.copyProperties(suivieDepense, suivieDepenseDto);
                return suivieDepenseDto;
        }

        // SUIVIE DEPENSE MAPPER
        public SuivieDepense fromSuivieDepenseDto(SuivieDepenseDto suivieDepenseDto) {
                SuivieDepense suivieDepense = new SuivieDepense();
                BeanUtils.copyProperties(suivieDepenseDto, suivieDepense);
                return suivieDepense;
        }

        // public SuivieDepense fromSuivieDepenseDto(SuivieDepenseEncaissementDto
        // suivieDepenseEncaissementDto) {
        // suivieDepenseRepository.findById(suivieDepenseEncaissementDto.getId)
        // SuivieDepense suivieDepense = new SuivieDepense();
        // BeanUtils.copyProperties(suivieDepenseDto, suivieDepense);
        // return suivieDepense;
        // }

}
