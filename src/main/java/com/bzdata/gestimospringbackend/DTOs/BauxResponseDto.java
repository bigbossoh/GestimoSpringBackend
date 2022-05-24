package com.bzdata.gestimospringbackend.DTOs;

import java.time.LocalDate;

import com.bzdata.gestimospringbackend.Models.BailLocation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BauxResponseDto {
    Long id;
    Long idAgence;
    String designationBail;
    String abrvCodeBail;
    boolean enCoursBail;
    boolean archiveBail;
    double montantCautionBail;
    int nbreMoisCautionBail;
    double nouveauMontantLoyer;
    LocalDate dateDebut;
    LocalDate dateFin;

    public static BauxResponseDto fromEntity(BailLocation bailLocation) {
        if (bailLocation == null) {
            return null;
        }
        return BauxResponseDto.builder()
                .abrvCodeBail(bailLocation.getAbrvCodeBail())
                .archiveBail(bailLocation.isArchiveBail())
                .idAgence(bailLocation.getIdAgence())
                .dateDebut(bailLocation.getDateDebut())
                .dateFin(bailLocation.getDateFin())
                .designationBail(bailLocation.getDesignationBail())
                .enCoursBail(bailLocation.isEnCoursBail())
                .id(bailLocation.getId())
                .montantCautionBail(bailLocation.getMontantCautionBail())
                .nbreMoisCautionBail(bailLocation.getNbreMoisCautionBail())
                .build();
    }
}
