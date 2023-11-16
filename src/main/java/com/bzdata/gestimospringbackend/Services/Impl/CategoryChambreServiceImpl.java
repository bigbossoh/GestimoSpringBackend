package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.DTOs.CategoryChambreSaveOrUpdateDto;
import com.bzdata.gestimospringbackend.Models.hotel.CategorieChambre;
import com.bzdata.gestimospringbackend.Services.CategoryChambreService;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.CategoryChambreRepository;
import com.bzdata.gestimospringbackend.validator.ObjectsValidator;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class CategoryChambreServiceImpl implements CategoryChambreService {

  final CategoryChambreRepository categoryChambreRepository;
  private final ObjectsValidator<CategoryChambreSaveOrUpdateDto> validator;

  @Override
  public Long save(CategoryChambreSaveOrUpdateDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'save'");
  }

  @Override
  public List<CategoryChambreSaveOrUpdateDto> findAll() {
    return categoryChambreRepository
      .findAll()
      .stream()
      .sorted(Comparator.comparing(CategorieChambre::getName))
      .map(GestimoWebMapperImpl::fromCategoryChambre)
      .collect(Collectors.toList());
  }

  @Override
  public CategoryChambreSaveOrUpdateDto findById(Long id) {
    CategorieChambre categorieChambre = categoryChambreRepository
      .findById(id)
      .orElse(null);
    if (categorieChambre != null) {
      return GestimoWebMapperImpl.fromCategoryChambre(categorieChambre);
    } else {
      return null;
    }
  }

  @Override
  public void delete(Long id) {
    CategorieChambre categorieChambre = categoryChambreRepository
      .findById(id)
      .orElse(null);
    if (categorieChambre != null) {
      categoryChambreRepository.delete(categorieChambre);
    } else {
      throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
  }

  @Override
  public CategoryChambreSaveOrUpdateDto saveOrUpdate(
    CategoryChambreSaveOrUpdateDto dto
  ) {
    CategorieChambre categorieChambre = categoryChambreRepository
      .findById(dto.getId())
      .orElse(null);

    validator.validate(dto);
    if (categorieChambre != null) {
      categorieChambre.setDescription(dto.getDescription());
      categorieChambre.setName(dto.getName());
      categorieChambre.setPrice(dto.getPrice());
      categorieChambre.setIdAgence(dto.getIdAgence());
      categorieChambre.setIdCreateur(dto.getIdCreateur());
      categorieChambre.setPourcentReduc(dto.getPourcentReduc());
      categorieChambre.setNbrDiffJour(dto.getNbrDiffJour());
      CategorieChambre savCategorieChambre = categoryChambreRepository.save(
        categorieChambre
      );
      return GestimoWebMapperImpl.fromCategoryChambre(savCategorieChambre);
    } else {
      CategorieChambre newCategorieChambre = new CategorieChambre();
      newCategorieChambre.setDescription(dto.getDescription());
      newCategorieChambre.setName(dto.getName());
      newCategorieChambre.setPrice(dto.getPrice());
      newCategorieChambre.setIdCreateur(dto.getIdCreateur());
      newCategorieChambre.setIdAgence(dto.getIdAgence());
      newCategorieChambre.setPourcentReduc(dto.getPourcentReduc());
      newCategorieChambre.setNbrDiffJour(dto.getNbrDiffJour());
      CategorieChambre savCategorieChambre = categoryChambreRepository.save(
        newCategorieChambre
      );
      return GestimoWebMapperImpl.fromCategoryChambre(savCategorieChambre);
    }
  }

  @Override
  public CategoryChambreSaveOrUpdateDto saveOrUpdateCategoryChambre(
    CategoryChambreSaveOrUpdateDto dto
  ) {
    log.info("Info sur le dto est : {}", dto);
    CategorieChambre categorieChambre = categoryChambreRepository
      .findById(dto.getId())
      .orElse(null);
    if (categorieChambre != null) {
      categorieChambre.setDescription(dto.getDescription());
      categorieChambre.setName(dto.getName());
      categorieChambre.setPrice(dto.getPrice());
      categorieChambre.setPourcentReduc(dto.getPourcentReduc());
      categorieChambre.setNbrDiffJour(dto.getNbrDiffJour());
      categoryChambreRepository.save(categorieChambre);
      return GestimoWebMapperImpl.fromCategoryChambre(categorieChambre);
    }
    CategorieChambre newCategite = new CategorieChambre();
    newCategite.setIdCreateur(dto.getIdCreateur());
    newCategite.setIdAgence(dto.getIdAgence());
    newCategite.setDescription(dto.getDescription());
    newCategite.setName(dto.getName());
    newCategite.setPrice(dto.getPrice());
    newCategite.setPourcentReduc(dto.getPourcentReduc());
    newCategite.setNbrDiffJour(dto.getNbrDiffJour());
    categoryChambreRepository.save(newCategite);
    return GestimoWebMapperImpl.fromCategoryChambre(newCategite);
  }

  @Override
  public List<CategoryChambreSaveOrUpdateDto> findAllCategorie(Long idAgnce) {
    return categoryChambreRepository
      .findAll()
      .stream()
      .filter(t->t.getIdAgence()==idAgnce)
      .map(GestimoWebMapperImpl::fromCategoryChambre)
      .collect(Collectors.toList());
  }
}
