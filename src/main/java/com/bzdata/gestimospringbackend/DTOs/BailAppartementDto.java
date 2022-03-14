package com.bzdata.gestimospringbackend.DTOs;

import java.time.LocalDate;

import com.bzdata.gestimospringbackend.Models.Appartement;
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
    String designationBail;
    String abrvCodeBail;
    boolean enCoursBail;
    boolean archiveBail;
    double montantCautionBail;
    int nbreMoisCautionBail;

    LocalDate dateDebut;
    LocalDate dateFin;
    AppartementDto appartementDto;
    UtilisateurRequestDto utilisateurRequestDto;

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
                .appartementDto(AppartementDto.fromEntity((Appartement) bailLocation.getAppartementBail()))
                .montantCautionBail(bailLocation.getMontantCautionBail())
                .nbreMoisCautionBail(bailLocation.getNbreMoisCautionBail())
                .utilisateurRequestDto(UtilisateurRequestDto.fromEntity(bailLocation.getUtilisateurOperation()))
                .build();
    }

    public static BailLocation toEntity(BailAppartementDto bailAppartementDto) {
        if (bailAppartementDto == null) {
            return null;
        }
        BailLocation bailLocation = new BailLocation();
        bailLocation.setAbrvCodeBail(bailAppartementDto.getAbrvCodeBail());
        bailLocation.setArchiveBail(bailLocation.isArchiveBail());
        bailLocation.setAppartementBail(AppartementDto.toEntity(bailAppartementDto.getAppartementDto()));
        bailLocation.setDateDebut(bailAppartementDto.getDateDebut());
        bailLocation.setDateFin(bailAppartementDto.getDateFin());
        bailLocation.setDesignationBail(bailAppartementDto.getDesignationBail());
        bailLocation.setEnCoursBail(bailAppartementDto.isEnCoursBail());
        bailLocation.setId(bailAppartementDto.getId());
        bailLocation.setMontantCautionBail(bailAppartementDto.getMontantCautionBail());
        bailLocation.setNbreMoisCautionBail(bailAppartementDto.getNbreMoisCautionBail());
        bailLocation
                .setUtilisateurOperation(UtilisateurRequestDto.toEntity(bailAppartementDto.getUtilisateurRequestDto()));

        return bailLocation;
    }
}
