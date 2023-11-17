package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.EtablissementUtilisateurDto;

public interface EtablissementUtilsateurService
  extends AbstractService<EtablissementUtilisateurDto> {
  EtablissementUtilisateurDto saveorUpdateChapitreUser(
    EtablissementUtilisateurDto dto
  );
  EtablissementUtilisateurDto findDefaultChapitreUserByIdUser(Long idUser);
}
