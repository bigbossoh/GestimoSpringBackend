package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.ImmeubleDto;

public interface ImmeubleService {

    ImmeubleDto save(ImmeubleDto dto);

    boolean delete(Long id);

    List<ImmeubleDto> findAll();

    ImmeubleDto findById(Long id);

    ImmeubleDto findByName(String nom);

    List<ImmeubleDto> findAllByIdSite(Long id);
}
