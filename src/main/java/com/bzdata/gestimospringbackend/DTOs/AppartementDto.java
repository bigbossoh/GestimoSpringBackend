package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppartementDto {
    Long id;
    Long idAgence;
    Long idCreateur;
    int nbrPieceApp;
    int nbreChambreApp;
    int nbreSalonApp;
    int nbreSalleEauApp;
    Long numApp;
    Long idEtageAppartement;
    String fullNameProprio;
    String codeAbrvBienImmobilier;
    String nomCompletBienImmobilier;
    String nomBaptiserBienImmobilier;
    String description;
    double superficieBien;
    boolean bienMeublerResidence;
    boolean isOccupied;

    Long idCategorie;
    String nameCategorie;
    double priceCategorie;
    int nbrDiffJourCategorie;
    double pourcentReducCategorie;
    CategoryChambreSaveOrUpdateDto categorieChambre;

}
