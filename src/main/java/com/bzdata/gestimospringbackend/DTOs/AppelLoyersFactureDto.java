package com.bzdata.gestimospringbackend.DTOs;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppelLoyersFactureDto {
    Long id;
    Long idAgence;
    Long idCreateur;
    String periodeAppelLoyer;
    String statusAppelLoyer;
    LocalDate datePaiementPrevuAppelLoyer;
    LocalDate dateDebutMoisAppelLoyer;
    LocalDate dateFinMoisAppelLoyer;
    String periodeLettre;
    String moisUniquementLettre;
    int anneeAppelLoyer;
    int moisChiffreAppelLoyer;
    String descAppelLoyer;
    double montantLoyerBailLPeriode;
    double soldeAppelLoyer;
    boolean isSolderAppelLoyer;
    boolean isCloturer;
    // Locataire
    String nomLocataire;
    String prenomLocataire;
    String genreLocataire;
    String emailLocatire;
    Long idLocataire;
    // Agence
    String nomAgence;
    String telAgence;
    String compteContribuableAgence;
    String emailAgence;
    String mobileAgence;
    String regimeFiscaleAgence;
    String faxAgence;
    String sigleAgence;
    // Bien Immobilier
    String bienImmobilierFullName;
    String abrvBienimmobilier;
    // Proprietaire
    String nomPropietaire;
    String prenomPropietaire;
    String genrePropietaire;
    String mailProprietaire;
    // BailLocation
    Long idBailLocation;
    String abrvCodeBail;
    double nouveauMontantLoyer;

}
