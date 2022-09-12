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
public class BailAppartementDto {
    Long id;
    Long idAgence;
    Long idCreateur;
    String designationBail;
    String abrvCodeBail;
    boolean enCoursBail;
    boolean archiveBail;
    double montantCautionBail;
    int nbreMoisCautionBail;
    double nouveauMontantLoyer;

    LocalDate dateDebut;
    LocalDate dateFin;
    Long idAppartement;
    Long idUtilisateur;

    public static BailAppartementDto fromEntity(BailLocation bailLocation) {
        if (bailLocation == null) {
            return null;
        }
        return BailAppartementDto.builder()
                .abrvCodeBail(bailLocation.getAbrvCodeBail())
                .archiveBail(bailLocation.isArchiveBail())
                .dateDebut(bailLocation.getDateDebut())
                .dateFin(bailLocation.getDateFin())
                .designationBail(bailLocation.getDesignationBail())
                .enCoursBail(bailLocation.isEnCoursBail())
                .id(bailLocation.getId())
                .idAppartement(bailLocation.getAppartementBail().getId())
                .montantCautionBail(bailLocation.getMontantCautionBail())
                .nbreMoisCautionBail(bailLocation.getNbreMoisCautionBail())
                .idUtilisateur(bailLocation.getUtilisateurOperation().getId())
                .build();
    }

    // public static BailLocation toEntity(BailAppartementDto bailAppartementDto) {
    // if (bailAppartementDto == null) {
    // return null;
    // }
    // BailLocation bailLocation = new BailLocation();
    // bailLocation.setAbrvCodeBail(bailAppartementDto.getAbrvCodeBail());
    // bailLocation.setArchiveBail(bailLocation.isArchiveBail());
    // bailLocation.setAppartementBail(AppartementDto.toEntity(bailAppartementDto.getAppartementDto()));
    // bailLocation.setDateDebut(bailAppartementDto.getDateDebut());
    // bailLocation.setDateFin(bailAppartementDto.getDateFin());
    // bailLocation.setDesignationBail(bailAppartementDto.getDesignationBail());
    // bailLocation.setEnCoursBail(bailAppartementDto.isEnCoursBail());
    // bailLocation.setId(bailAppartementDto.getId());
    // bailLocation.setMontantCautionBail(bailAppartementDto.getMontantCautionBail());
    // bailLocation.setNbreMoisCautionBail(bailAppartementDto.getNbreMoisCautionBail());
    // bailLocation
    // .setUtilisateurOperation(UtilisateurRequestDto.toEntity(bailAppartementDto.getUtilisateurRequestDto()));

    // return bailLocation;
    // }
}
