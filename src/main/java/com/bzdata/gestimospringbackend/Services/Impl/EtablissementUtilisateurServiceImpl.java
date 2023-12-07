package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.DTOs.EtablissementUtilisateurDto;
import com.bzdata.gestimospringbackend.Models.Chapitre;
import com.bzdata.gestimospringbackend.Models.EtablissementUtilisateur;
import com.bzdata.gestimospringbackend.Models.Etablissement;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.EtablissementUtilsateurService;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.ChapitreRepository;
import com.bzdata.gestimospringbackend.repository.ChapitreUserRepository;
import com.bzdata.gestimospringbackend.repository.EtablissementRepository;
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
public class EtablissementUtilisateurServiceImpl implements EtablissementUtilsateurService {

  final EtablissementRepository chapitreRepository;
  final ChapitreUserRepository chapitreUserRepository;
  final UtilisateurRepository utilisateurRepository;
  final GestimoWebMapperImpl gestimoWebMapperImpl;

  @Override
  public Long save(EtablissementUtilisateurDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
  }

  @Override
  public List<EtablissementUtilisateurDto> findAll() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findAll'");
  }

  @Override
  public EtablissementUtilisateurDto findById(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findById'");
  }

  @Override
  public void delete(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

  @Override
  public EtablissementUtilisateurDto saveOrUpdate(EtablissementUtilisateurDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'saveOrUpdate'"
    );
  }

  @Override
  public EtablissementUtilisateurDto saveorUpdateChapitreUser(
EtablissementUtilisateurDto chapitreUserDto

  ) {
    EtablissementUtilisateur chapitreUser = new EtablissementUtilisateur();
    Utilisateur findUser = utilisateurRepository
      .findById(chapitreUserDto.getUtilisateur())
      .orElse(null);
    Etablissement findChapitre = chapitreRepository.findById(chapitreUserDto.getChapite()).orElse(null);
    if (findUser != null && findChapitre != null) {
        chapitreUser.setEtabl(findChapitre);
        chapitreUser.setUtilisateurEtabl(findUser);
        chapitreUser.setEtableDefault(chapitreUserDto.isDefaultChapite());
      EtablissementUtilisateur chapitreSave=  chapitreUserRepository.save(chapitreUser);
      return gestimoWebMapperImpl.fromEtablissementUtilisateur(chapitreSave);
    }
    return null;
  }

  @Override
  public EtablissementUtilisateurDto findDefaultChapitreUserByIdUser(Long idUser) {
    EtablissementUtilisateur chapitreSave=  chapitreUserRepository
    .findAll().stream().filter(chap->chap.getUtilisateurEtabl().getId()==idUser&& chap.isEtableDefault()==true).findFirst().orElse(null);
  return gestimoWebMapperImpl.fromEtablissementUtilisateur(chapitreSave);
  }
}
