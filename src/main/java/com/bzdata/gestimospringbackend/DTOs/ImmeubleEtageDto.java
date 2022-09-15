package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImmeubleEtageDto  {
    Long id;
    private Long idAgence;
    private  Long idCreateur;
    int nbrEtage;
    int nbrePieceImmeuble;
    String abrvNomImmeuble;
    String descriptionImmeuble;
    int numeroImmeuble;
    boolean isGarrage;
    String statutBien;
    String denominationBien;
    String nomBien;
    String etatBien;
    double superficieBien;
    boolean isOccupied;
    Long idSite;
    Long idUtilisateur;

    String nomPropio;
    String prenomProprio;
}
