package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.VerificationToken;

import java.util.List;

public interface UtilisateurService {

    // UtilisateurRequestDto saveLocataire(UtilisateurRequestDto dto);

    // boolean saveProprietaire(UtilisateurRequestDto dto);

    // UtilisateurRequestDto saveGerant(UtilisateurRequestDto dto);
    boolean saveutilisateur(UtilisateurRequestDto dto);

    UtilisateurRequestDto findById(Long id);

    UtilisateurRequestDto findUtilisateurByEmail(String email);

    UtilisateurRequestDto findUtilisateurByUsername(String username);

    List<UtilisateurRequestDto> listOfAllUtilisateurOrderbyName();

    List<UtilisateurRequestDto> listOfAllUtilisateurLocataireOrderbyName();

    List<UtilisateurRequestDto> listOfAllUtilisateurProprietaireOrderbyName();

    List<UtilisateurRequestDto> listOfAllUtilisateurGerantOrderbyName();

    List<UtilisateurRequestDto> listOfAllUtilisateurSuperviseurOrderbyName();

    void deleteLocatire(Long id);

    void deleteProprietaire(Long id);

    void verifyAccount(String token);

    void feachUserAndEnable(VerificationToken verificationToken);

}
