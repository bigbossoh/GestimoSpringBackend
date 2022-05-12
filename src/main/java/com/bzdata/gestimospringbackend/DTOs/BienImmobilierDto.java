package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Bienimmobilier;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BienImmobilierDto {
    Long id;
    Long numBien;
    String statutBien;
    boolean isArchived;
    String abrvBienimmobilier;
    String description;
    String nomBien;
    double superficieBien;
    boolean isOccupied;
    String site;
    String utilisateur;

    public static BienImmobilierDto fromEntity(Bienimmobilier bienimmobilier) {
        if (bienimmobilier == null) {
            return null;
        }
        return BienImmobilierDto.builder()
                .abrvBienimmobilier(bienimmobilier.getAbrvBienimmobilier())
                .description(bienimmobilier.getDescription())
                .isArchived(bienimmobilier.isArchived())
                .isOccupied(bienimmobilier.isOccupied())
                .nomBien(bienimmobilier.getNomBien())
                .numBien(bienimmobilier.getNumBien())
                .site(bienimmobilier.getSite().getNomSite())
                .statutBien(bienimmobilier.getStatutBien())
                .superficieBien(bienimmobilier.getSuperficieBien())
                .id(bienimmobilier.getId())
                .utilisateur(bienimmobilier.getUtilisateur().getNom() + " " + bienimmobilier.getUtilisateur()
                        .getPrenom())

                .build();
    }
}
