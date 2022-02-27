package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;

import java.util.List;

public interface UtilisateurService {
    UtilisateurRequestDto save(UtilisateurRequestDto dto);

    UtilisateurRequestDto findById(Long id);

    List<UtilisateurRequestDto> listOfUtilisateur();

    void delete(Long id);

    UtilisateurRequestDto findByEmail(String email);


}
