package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.LocataireEncaisDTO;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.VerificationToken;

import java.util.List;

public interface UtilisateurService {

    UtilisateurAfficheDto saveUtilisateur(UtilisateurRequestDto dto);

    UtilisateurRequestDto findById(Long id);

    UtilisateurRequestDto findUtilisateurByEmail(String email);

    UtilisateurRequestDto findUtilisateurByUsername(String username);

    List<UtilisateurAfficheDto> listOfAllUtilisateurOrderbyName(Long idAgence);

    List<UtilisateurAfficheDto> listOfAllUtilisateurLocataireOrderbyName(Long idAgence);

    List<UtilisateurAfficheDto> listOfAllUtilisateurProprietaireOrderbyName(Long idAgence);

    List<UtilisateurAfficheDto> listOfAllUtilisateurGerantOrderbyName(Long idAgence);

    List<UtilisateurAfficheDto> listOfAllUtilisateurSuperviseurOrderbyName();

    void deleteLocatire(Long id);

    void deleteProprietaire(Long id);

    void verifyAccount(String token);

    void feachUserAndEnable(VerificationToken verificationToken);

    List<UtilisateurAfficheDto> listOfAllUtilisateurLocataireOrderbyNameByAgence(Long idAgence);

    List<LocataireEncaisDTO> listOfLocataireAyantunbail(Long idAgence);
}
