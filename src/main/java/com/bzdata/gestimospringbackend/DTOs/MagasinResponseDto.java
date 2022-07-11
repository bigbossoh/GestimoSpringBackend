package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Magasin;
import com.bzdata.gestimospringbackend.Models.Utilisateur;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MagasinResponseDto {
    Long id;
    Long idAgence;
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

    Utilisateur utilisateur;
    String proprietaire;

    public static MagasinResponseDto fromEntity(Magasin magasin) {
        if (magasin == null) {
            return null;
        }

        return MagasinResponseDto.builder()
                .id(magasin.getId())
                .idAgence(magasin.getIdAgence())
                .numBien(magasin.getNumBien())
                .statutBien(magasin.getStatutBien())
                .isArchived(magasin.isArchived())
                .abrvBienimmobilier(magasin.getAbrvBienimmobilier())
                .description(magasin.getDescription())
                .nomBien(magasin.getNomBien())
                .superficieBien(magasin.getSuperficieBien())
                .isOccupied(magasin.isOccupied())
                .isUnderBuildingMagasin(magasin.isUnderBuildingMagasin())
                .abrvNomMagasin(magasin.getAbrvNomMagasin())
                .nmbrPieceMagasin(magasin.getNmbrPieceMagasin())
                .nomMagasin(magasin.getNomMagasin())
                .proprietaire(magasin.getUtilisateur().getNom()+" "+(magasin.getUtilisateur().getPrenom()))
                .build();
    }
}
