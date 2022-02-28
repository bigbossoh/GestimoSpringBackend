package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;

import java.util.List;

public interface AgenceImmobilierService {

    AgenceResponseDto save(AgenceRequestDto dto);

    AgenceResponseDto findById(Long id);

    List<AgenceResponseDto> listOfAgenceImmobilier();

    void delete(Long id);

    AgenceResponseDto findAgenceByEmail(String email);
}
