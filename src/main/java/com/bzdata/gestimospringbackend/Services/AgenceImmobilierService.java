package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.VerificationToken;

import java.util.List;

public interface AgenceImmobilierService {

    AgenceResponseDto save(AgenceRequestDto dto);

    AgenceResponseDto findById(Long id);

    List<AgenceResponseDto> listOfAgenceImmobilier();

    void delete(Long id);

    AgenceResponseDto findAgenceByEmail(String email);

    void verifyAccount(String token);

    void feachUserAndEnable(VerificationToken verificationToken);
}
