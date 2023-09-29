package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.DTOs.PrixParCategorieChambreDto;
import com.bzdata.gestimospringbackend.Models.hotel.CategorieChambre;
import com.bzdata.gestimospringbackend.Models.hotel.PrixParCategorieChambre;
import com.bzdata.gestimospringbackend.Services.PrixParCategorieChambreService;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.CategoryChambreRepository;
import com.bzdata.gestimospringbackend.repository.PrixParCategorieChambreRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrixParCategoryChambreServiceImpl
  implements PrixParCategorieChambreService {

  final PrixParCategorieChambreRepository prixParCategorieChambreService;
  final CategoryChambreRepository categoryChambreRepository;
  final GestimoWebMapperImpl gestimoWebMapperImpl;

  @Override
  public Long save(PrixParCategorieChambreDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
  }

  @Override
  public List<PrixParCategorieChambreDto> findAll() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findAll'");
  }

  @Override
  public PrixParCategorieChambreDto findById(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findById'");
  }

  @Override
  public void delete(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

  @Override
  public PrixParCategorieChambreDto saveOrUpdate(
    PrixParCategorieChambreDto dto
  ) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'saveOrUpdate'"
    );
  }

  @Override
  public PrixParCategorieChambreDto saveOrUpDatePrixPArCategoryChambre(
    PrixParCategorieChambreDto dto
  ) {
    PrixParCategorieChambre parCategorieChambre = prixParCategorieChambreService
      .findById(dto.getId())
      .orElse(null);
    CategorieChambre categorieChambre = categoryChambreRepository
      .findById(dto.getIdCategorieChambre())
      .orElse(null);
    if (parCategorieChambre != null) {
      parCategorieChambre.setCategorieChambre(categorieChambre);
      parCategorieChambre.setDescription(dto.getDescription());
      parCategorieChambre.setIntervalPrix(dto.getIntervalPrix());
      parCategorieChambre.setNbrDiffJour(dto.getNbrDiffJour());
      parCategorieChambre.setNombreDeJour(dto.getNombreDeJour());
      parCategorieChambre.setPrix(dto.getPrix());
      PrixParCategorieChambre saveParCategorieChambre = prixParCategorieChambreService.save(
        parCategorieChambre
      );
      return gestimoWebMapperImpl.fromPrixParCategorieChambreDto(
        saveParCategorieChambre
      );
    }
    PrixParCategorieChambre newPrixParCategorieChambre = new PrixParCategorieChambre();
    newPrixParCategorieChambre.setCategorieChambre(categorieChambre);
    newPrixParCategorieChambre.setDescription(dto.getDescription());
    newPrixParCategorieChambre.setIntervalPrix(dto.getIntervalPrix());
    newPrixParCategorieChambre.setNbrDiffJour(dto.getNbrDiffJour());
    newPrixParCategorieChambre.setNombreDeJour(dto.getNombreDeJour());
    newPrixParCategorieChambre.setPrix(dto.getPrix());
    PrixParCategorieChambre saveParCategorieChambre = prixParCategorieChambreService.save(
      newPrixParCategorieChambre
    );

    return gestimoWebMapperImpl.fromPrixParCategorieChambreDto(
      saveParCategorieChambre
    );
  }

  @Override
  public PrixParCategorieChambreDto categoriParPrix(Long idCategori) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'categoriParPrix'"
    );
  }
}
