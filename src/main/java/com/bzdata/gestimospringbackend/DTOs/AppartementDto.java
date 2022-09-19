package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
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
    boolean isOccupied= false;

}
