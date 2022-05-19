package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.Models.VerificationToken;

public interface AgenceImmobilierService {

    boolean save(AgenceRequestDto dto);

    AgenceResponseDto findAgenceById(Long id);

    List<AgenceResponseDto> listOfAgenceImmobilier();

    List<AgenceResponseDto> listOfAgenceOrderByNomAgenceAsc();

    void deleteAgence(Long id);

    AgenceResponseDto findAgenceByEmail(String email);

    void verifyAccount(String token);

    void feachUserAndEnable(VerificationToken verificationToken);
}
