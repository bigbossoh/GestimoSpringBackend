package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.EncaissementPayloadDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;
import com.bzdata.gestimospringbackend.DTOs.LocataireEncaisDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface EncaissementPrincipalService {
    boolean saveEncaissement(EncaissementPayloadDto dto);

    boolean saveEncaissementMasse(List<EncaissementPayloadDto> dtos);

    List<EncaissementPrincipalDTO> findAllEncaissement(Long idAgence);

    double getTotalEncaissementByIdAppelLoyer(Long idAppelLoyer);

    EncaissementPrincipalDTO findEncaissementById(Long id);

    List<EncaissementPrincipalDTO> findAllEncaissementByIdBienImmobilier(Long id);

    List<EncaissementPrincipalDTO> findAllEncaissementByIdLocataire(Long id);

    List<EncaissementPrincipalDTO> saveEncaissementAvecRetourDeList(EncaissementPayloadDto dto);

    double sommeEncaisserParJour(String jour,Long idAgence, Long chapitre);

    boolean delete(Long id);

   List<LocataireEncaisDTO> listeLocataireImpayerParAgenceEtPeriode(Long agence,String periode);
    double sommeEncaissementParAgenceEtParChapitreEtParPeriode(Long agence,  Long chapitre, String dateDebut,String dateFin );
    double sommeEncaissementParAgenceEtParPeriode(Long agence, LocalDate dateDebut,LocalDate dateFin );
    double sommeImpayerParAgenceEtParChapitreEtParPeriode(Long agence,  Long chapitre, String dateDebut,String dateFin );
    double sommeImpayerParAgenceEtParPeriode(Long agence, String dateDebut,String dateFin );
    Map<YearMonth, Double> getTotalEncaissementsParMois( Long idAgence,LocalDate debut, LocalDate fin);
    Map<YearMonth, Double[]> getTotalEncaissementsEtMontantsDeLoyerParMois(Long idAgence,LocalDate debut, LocalDate fin);
}
