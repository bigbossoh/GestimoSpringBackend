package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.ImmeubleAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.ImmeubleDto;
import com.bzdata.gestimospringbackend.DTOs.ImmeubleEtageDto;

public interface ImmeubleService {



  ImmeubleEtageDto saveImmeubleEtageDto(ImmeubleEtageDto dto);

  ImmeubleDto updateImmeuble(ImmeubleDto dto);


  boolean delete(Long id);

  List<ImmeubleDto> findAll();

  List<ImmeubleEtageDto> findAllPourAffichageImmeuble();

  ImmeubleDto findById(Long id);

  ImmeubleDto findByName(String nom);

  List<ImmeubleDto> findAllByIdSite(Long id);
}
