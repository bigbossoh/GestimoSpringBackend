package com.bzdata.gestimospringbackend.Services;

import java.io.IOException;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.AgenceImmobilierDTO;
import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.DTOs.ImageLogoDto;
import com.bzdata.gestimospringbackend.Models.VerificationToken;

public interface AgenceImmobilierService {

    boolean save(AgenceRequestDto dto);

    AgenceImmobilierDTO  saveUneAgence(AgenceRequestDto dto);
    AgenceResponseDto findAgenceById(Long id);

    List<AgenceImmobilierDTO> listOfAgenceImmobilier();

    List<AgenceImmobilierDTO> listOfAgenceOrderByNomAgenceAsc(Long idAgence);

    void deleteAgence(Long id);

    AgenceImmobilierDTO findAgenceByEmail(String email);

    void verifyAccount(String token);

    void feachUserAndEnable(VerificationToken verificationToken);

    String uploadLogoAgence(ImageLogoDto dto)throws IOException;
}
