package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.DTOs.ChapitreUserDto;
import com.bzdata.gestimospringbackend.Models.Chapitre;
import com.bzdata.gestimospringbackend.Models.ChapitreUser;
import com.bzdata.gestimospringbackend.Models.DefaultChapitre;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.ChapitreUserService;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.ChapitreRepository;
import com.bzdata.gestimospringbackend.repository.ChapitreUserRepository;
import com.bzdata.gestimospringbackend.repository.DefaultChapitreRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import java.util.List;
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
public class ChapitreUserServiceImpl implements ChapitreUserService {

  final DefaultChapitreRepository chapitreRepository;
  final ChapitreUserRepository chapitreUserRepository;
  final UtilisateurRepository utilisateurRepository;
  final GestimoWebMapperImpl gestimoWebMapperImpl;

  @Override
  public Long save(ChapitreUserDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
  }

  @Override
  public List<ChapitreUserDto> findAll() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findAll'");
  }

  @Override
  public ChapitreUserDto findById(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findById'");
  }

  @Override
  public void delete(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

  @Override
  public ChapitreUserDto saveOrUpdate(ChapitreUserDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'saveOrUpdate'"
    );
  }

  @Override
  public ChapitreUserDto saveorUpdateChapitreUser(
ChapitreUserDto chapitreUserDto

  ) {
    ChapitreUser chapitreUser = new ChapitreUser();
    Utilisateur findUser = utilisateurRepository
      .findById(chapitreUserDto.getUtilisateur())
      .orElse(null);
    DefaultChapitre findChapitre = chapitreRepository.findById(chapitreUserDto.getChapite()).orElse(null);
    if (findUser != null && findChapitre != null) {
        chapitreUser.setDefaultChapitre(findChapitre);
        chapitreUser.setUtilisateurChapitre(findUser);
        chapitreUser.setChapitreDefault(chapitreUserDto.isDefaultChapite());
      ChapitreUser chapitreSave=  chapitreUserRepository.save(chapitreUser);
      return gestimoWebMapperImpl.fromChapitreUser(chapitreSave);
    }
    return null;
  }
}
