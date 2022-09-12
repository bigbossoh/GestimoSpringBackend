package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MagasinDto {
    Long id;
    Long idAgence;
    Long idCreateur;
    Long numBien;
    String statutBien;
    boolean isArchived;
    String abrvBienimmobilier;
    String description;
    String nomBien;
    double superficieBien;
    boolean isOccupied;
    boolean isUnderBuildingMagasin;
    String abrvNomMagasin;
    int nmbrPieceMagasin;
    String nomMagasin;
    Long idEtage;
    Long idSite;
    Long idUtilisateur;
    String proprietaire;

}
