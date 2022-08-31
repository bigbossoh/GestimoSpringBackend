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
    double montantBailLPeriode;
    double soldeAppelLoyer;
    boolean isSolderAppelLoyer;
    //Locataire
    String nomLocataire;
    String prenomLocataire;
    String genreLocataire;
    String mailLocataire;
      //Agence
    String nomAgence;
    String telAgence;
    String compteContribuableAgence;
    String emailAgence;
    String mobileAgence;
    String regimeFiscaleAgence;
    String faxAgence;
    String sigleAgence;
    //Bien Immobilier
    String bienImmobilierFullName;
    String abrvBienimmobilier;
    //Proprietaire
    String nomPropietaire;
    String prenomPropietaire;
    String genrePropietaire;
    String mailProprietaire;
    //BailLocation
    String abrvCodeBail;
    double nouveauMontantLoyer;


}
