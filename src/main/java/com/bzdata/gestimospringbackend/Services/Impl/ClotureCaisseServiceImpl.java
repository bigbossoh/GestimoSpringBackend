package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.DTOs.ClotureCaisseDto;
import com.bzdata.gestimospringbackend.Models.ClotureCaisse;
import com.bzdata.gestimospringbackend.Services.ClotureCaisseService;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.ClotureCaisseRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClotureCaisseServiceImpl implements ClotureCaisseService {

  final ClotureCaisseRepository caisseRepository;
  final GestimoWebMapperImpl gestimoWebMapper;

  @Override
  public Long save(ClotureCaisseDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
  }

  @Override
  public List<ClotureCaisseDto> findAll() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findAll'");
  }

  @Override
  public ClotureCaisseDto findById(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findById'");
  }

  @Override
  public void delete(Long id) {
    caisseRepository.deleteById(id);
  }

  @Override
  public ClotureCaisseDto saveOrUpdate(ClotureCaisseDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'saveOrUpdate'"
    );
  }

  @Override
  public int countClotureCaisse(Long idCaisse) {
    return caisseRepository.countByIdCreateur(idCaisse);
  }

  @Override
  public boolean saveClotureCaisse(ClotureCaisseDto dto) {
    ClotureCaisse clotureCaisse = caisseRepository.save(
      gestimoWebMapper.toClotureCaisse(dto)
    );
    if (clotureCaisse != null) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public List<ClotureCaisseDto> findNonCloturerByDInferieurOuEgaleDate(
    Instant dateEnCours,
    Long idCaisse
  ) {
    return caisseRepository
      .findAll()
      .stream()
      .filter(cai -> cai.getCreationDate().isBefore(dateEnCours))
      .map(gestimoWebMapper::fromClotureCaisse)
      .collect(Collectors.toList());
  }

  @Override
  public List<ClotureCaisseDto> findNonCloturerByDate(
    Instant dateEnCours,
    Long idCaisse
  ) {
    throw new UnsupportedOperationException(
      "Unimplemented method 'findNonCloturerByDate'"
    );
  }

  @Override
  public List<ClotureCaisseDto> findNAllClotureCaisseByDate(
    Instant dateEnCours,
    Long idCaisse
  ) {
    throw new UnsupportedOperationException(
      "Unimplemented method 'findNAllClotureCaisseByDate'"
    );
  }

  @Override
  public List<ClotureCaisseDto> findAllByCaissier(Long idCaisse) {
    return caisseRepository
      .findAll()
      .stream()
      .filter(cai -> cai.getIdCreateur() == idCaisse)
      .map(gestimoWebMapper::fromClotureCaisse)
      .collect(Collectors.toList());
  }
}
