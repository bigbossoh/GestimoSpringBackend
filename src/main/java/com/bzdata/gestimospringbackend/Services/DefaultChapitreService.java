package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.DefaultChapitreDto;

public interface DefaultChapitreService
  extends AbstractService<DefaultChapitreDto> {
  DefaultChapitreDto saveOrUpDefaultChapitre(DefaultChapitreDto dto);
   
}
