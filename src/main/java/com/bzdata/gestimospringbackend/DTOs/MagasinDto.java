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
    int nombrePieceMagasin;
    Long numMagasin;
    String codeAbrvBienImmobilier;
    String nomCompletBienImmobilier;
    String nomBaptiserBienImmobilier;
    String description;
    double superficieBien;
    boolean bienMeublerResidence;
    boolean isOccupied= false;
    boolean isUnderBuildingMagasin;
    Long idEtage;
    Long idSite;
    Long idUtilisateur;
    String proprietaire;
    Long idmmeuble;
}
