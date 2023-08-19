package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseDto;
import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseEncaisPeriodeDto;
import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseEncaissementDto;
import com.bzdata.gestimospringbackend.Models.SuivieDepense;
import com.bzdata.gestimospringbackend.Services.SuivieDepenseService;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.mappers.BailMapperImpl;
import com.bzdata.gestimospringbackend.repository.SuivieDepenseRepository;
import com.bzdata.gestimospringbackend.validator.SuivieDepenseValidator;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class SuivieDepenseServiceImpl implements SuivieDepenseService {

  private final SuivieDepenseRepository suivieDepenseRepository;
  private final BailMapperImpl bailMapperImpl;

  @Override
  public List<SuivieDepenseDto> saveNewDepense(SuivieDepenseDto dto) {
    List<String> errors = SuivieDepenseValidator.validate(dto);
    if (!errors.isEmpty()) {
      log.error("l'objet suivie de depense n'est pas valide {}", errors);
      throw new InvalidEntityException(
        "Certain attributs de l'object agence immobiliere sont null.",
        ErrorCodes.SUIVIEDEPENSE_NOT_VALID,
        errors
      );
    }
    if (dto.getId() != null) {
      SuivieDepense suivieDepense = new SuivieDepense();
      suivieDepense.setCodeTransaction(UUID.randomUUID().toString());
      suivieDepense.setDateEncaissement(dto.getDateEncaissement());
      suivieDepense.setDesignation(dto.getDesignation());
      suivieDepense.setMontantDepense(dto.getMontantDepense());
      suivieDepense.setIdAgence(dto.getIdAgence());
      suivieDepense.setIdCreateur(dto.getIdCreateur());
      suivieDepense.setModePaiement(dto.getModePaiement());
      suivieDepense.setOperationType(dto.getOperationType());
      SuivieDepense suivieDepenseSaved = suivieDepenseRepository.save(
        suivieDepense
      );
      bailMapperImpl.fromSuivieDepense(suivieDepenseSaved);
      return findAlEncaissementParAgence(dto.getIdAgence());
    } else {
      SuivieDepense suivieDepense = suivieDepenseRepository
        .findById(dto.getId())
        .orElseThrow(() ->
          new InvalidEntityException(
            "Aucune Depense has been found with id " + dto.getId(),
            ErrorCodes.SUIVIEDEPENSE_NOT_FOUND
          )
        );

      suivieDepense.setDateEncaissement(dto.getDateEncaissement());
      suivieDepense.setDesignation(dto.getDesignation());
      suivieDepense.setMontantDepense(dto.getMontantDepense());
      suivieDepense.setIdAgence(dto.getIdAgence());
      suivieDepense.setIdCreateur(dto.getIdCreateur());
      suivieDepense.setModePaiement(dto.getModePaiement());
      suivieDepense.setOperationType(dto.getOperationType());
      SuivieDepense suivieDepenseSaved = suivieDepenseRepository.save(
        suivieDepense
      );
      bailMapperImpl.fromSuivieDepense(suivieDepenseSaved);
      return findAlEncaissementParAgence(dto.getIdAgence());
    }
  }

  @Override
  public boolean annulerTransactionByCodeTransaction(String codeTransation) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean annulerTransactionById(String Id) {
    return false;
  }

  @Override
  public List<SuivieDepenseDto> supprimerUneEcritureById(
    Long id,
    Long idAgence
  ) {
    SuivieDepense suivitrouver = suivieDepenseRepository
      .findById(id)
      .orElseThrow(null);
    try {
      if (suivitrouver != null) {
        suivieDepenseRepository.delete(suivitrouver);
        return findAlEncaissementParAgence(idAgence);
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return null;
  }

  @Override
  public SuivieDepenseDto findById(Long id) {
    log.info("We are going to get back the suivie depense By {}", id);
    if (id == null) {
      log.error("you are not provided a id of suivie depense.");
      return null;
    }
    return suivieDepenseRepository
      .findById(id)
      .map(bailMapperImpl::fromSuivieDepense)
      .orElseThrow(() ->
        new InvalidEntityException(
          "Aucun Studio has been found with Code " + id,
          ErrorCodes.SUIVIEDEPENSE_NOT_FOUND
        )
      );
  }

  @Override
  public SuivieDepenseDto findByCodeTransaction(String codeTransation) {
    log.info(
      "We are going to get back the suivie depense By {}",
      codeTransation
    );
    if (codeTransation == null) {
      log.error("you are not provided a codeTransation of suivie depense.");
      return null;
    }
    return suivieDepenseRepository
      .findByCodeTransaction(codeTransation)
      .map(bailMapperImpl::fromSuivieDepense)
      .orElseThrow(() ->
        new InvalidEntityException(
          "Aucun Studio has been found with Code " + codeTransation,
          ErrorCodes.SUIVIEDEPENSE_NOT_FOUND
        )
      );
  }

  @Override
  public List<SuivieDepenseDto> findByDateEncaissement(
    SuivieDepenseEncaissementDto suivieDepenseEncaissementDto
  ) {
    return suivieDepenseRepository
      .findAll(Sort.by(Sort.Direction.DESC, "id"))
      .stream()
      .filter(agence ->
        agence.getIdAgence() == suivieDepenseEncaissementDto.getIdAgence()
      )
      .filter(dateEncaissement ->
        dateEncaissement.getDateEncaissement() ==
        suivieDepenseEncaissementDto.getDateEncaissement()
      )
      .map(bailMapperImpl::fromSuivieDepense)
      .collect(Collectors.toList());
  }

  @Override
  public List<SuivieDepenseDto> findByAllEncaissementByPeriode(
    SuivieDepenseEncaisPeriodeDto suivieDepenseEncaisPeriodeDto
  ) {
    return suivieDepenseRepository
      .findAll(Sort.by(Sort.Direction.DESC, "id"))
      .stream()
      .filter(agence ->
        agence.getIdAgence() == suivieDepenseEncaisPeriodeDto.getIdAgence()
      )
      .filter(dates ->
        dates
          .getDateEncaissement()
          .isAfter(suivieDepenseEncaisPeriodeDto.getDateDebutEncaissement()) &&
        dates
          .getDateEncaissement()
          .isBefore(suivieDepenseEncaisPeriodeDto.getDateFinEncaissement())
      )
      .map(bailMapperImpl::fromSuivieDepense)
      .collect(Collectors.toList());
  }

  @Override
  public List<SuivieDepenseDto> findAlEncaissementParAgence(Long idAgence) {
    return suivieDepenseRepository
      .findAll(Sort.by(Sort.Direction.DESC, "id"))
      .stream()
      .filter(agence -> agence.getIdAgence() == idAgence)
      .map(bailMapperImpl::fromSuivieDepense)
      .collect(Collectors.toList());
  }

  @Override
  public SuivieDepenseEncaisPeriodeDto totalSuiviDepenseEntreDeuxDate(
    Long idAgence,
    LocalDate debut,
    LocalDate fin
  ) {
    List<Double> listSuivieDepenseDtos = suivieDepenseRepository
      .findAll()
      .stream()
      .filter(agence ->
        agence.getIdAgence() == idAgence &&
        agence.getDateEncaissement().isAfter(debut) &&
        agence.getDateEncaissement().isBefore(fin)
      )
      .map(SuivieDepense::getMontantDepense)
      .collect(Collectors.toList());
    double totalEncaisse = listSuivieDepenseDtos
      .stream()
      .mapToDouble(Double::doubleValue)
      .sum();
    SuivieDepenseEncaisPeriodeDto newDepenseEncaisPeriodeDto = new SuivieDepenseEncaisPeriodeDto();
    newDepenseEncaisPeriodeDto.setDateDebutEncaissement(debut);
    newDepenseEncaisPeriodeDto.setDateFinEncaissement(fin);
    newDepenseEncaisPeriodeDto.setIdAgence(idAgence);
    newDepenseEncaisPeriodeDto.setTotalMontantDepense(totalEncaisse);
    newDepenseEncaisPeriodeDto.setCodeTransaction("1544");
    newDepenseEncaisPeriodeDto.setDateFinEncaissement(LocalDate.now());
    newDepenseEncaisPeriodeDto.setDesignation(
      "Sortie de caisse de " + debut + " à " + fin
    );
    return newDepenseEncaisPeriodeDto;
  }

  @Override
  public List<SuivieDepenseDto> listSuiviDepenseEntreDeuxDate(
    Long idAgence,
    LocalDate debut,
    LocalDate fin
  ) {
    log.info("les dates sont : {},{}", debut, fin);
    return suivieDepenseRepository
      .findAll(Sort.by(Sort.Direction.DESC, "id"))
      .stream()
      .filter(agence ->
        agence.getIdAgence() == idAgence &&
        agence.getDateEncaissement().isAfter(debut) &&
        agence.getDateEncaissement().isBefore(fin) 
       
      )
      .map(bailMapperImpl::fromSuivieDepense)
      .collect(Collectors.toList());
  }
}
