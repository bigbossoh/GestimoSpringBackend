package com.bzdata.gestimospringbackend.DTOs;

import java.time.LocalDate;

import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.Studio;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BailStudioDto {
    Long id;
    String designationBail;
    String abrvCodeBail;
    boolean enCoursBail;
    boolean archiveBail;
    double montantCautionBail;
    int nbreMoisCautionBail;

    LocalDate dateDebut;
    LocalDate dateFin;
    StudioDto studioDto;
    UtilisateurRequestDto utilisateurRequestDto;

    public static BailStudioDto fromEntity(BailLocation bailLocation) {
        if (bailLocation == null) {
            return null;
        }
        return BailStudioDto.builder()
                .abrvCodeBail(bailLocation.getAbrvCodeBail())
                .archiveBail(bailLocation.isArchiveBail())
                .dateDebut(bailLocation.getDateDebut())
                .dateFin(bailLocation.getDateFin())
                .designationBail(bailLocation.getDesignationBail())
                .enCoursBail(bailLocation.isEnCoursBail())
                .id(bailLocation.getId())
                .studioDto(StudioDto.fromEntity((Studio) bailLocation.getStudioBail()))
                .montantCautionBail(bailLocation.getMontantCautionBail())
                .nbreMoisCautionBail(bailLocation.getNbreMoisCautionBail())
                .utilisateurRequestDto(UtilisateurRequestDto.fromEntity(bailLocation.getUtilisateurOperation()))
                .build();
    }

    public static BailLocation toEntity(BailStudioDto bailStudioDto) {
        if (bailStudioDto == null) {
            return null;
        }
        BailLocation bailLocation = new BailLocation();
        bailLocation.setAbrvCodeBail(bailStudioDto.getAbrvCodeBail());
        bailLocation.setArchiveBail(bailLocation.isArchiveBail());
        bailLocation.setStudioBail(StudioDto.toEntity(bailStudioDto.getStudioDto()));
        bailLocation.setDateDebut(bailStudioDto.getDateDebut());
        bailLocation.setDateFin(bailStudioDto.getDateFin());
        bailLocation.setDesignationBail(bailStudioDto.getDesignationBail());
        bailLocation.setEnCoursBail(bailStudioDto.isEnCoursBail());
        bailLocation.setId(bailStudioDto.getId());
        bailLocation.setMontantCautionBail(bailStudioDto.getMontantCautionBail());
        bailLocation.setNbreMoisCautionBail(bailStudioDto.getNbreMoisCautionBail());
        bailLocation.setUtilisateurOperation(UtilisateurRequestDto.toEntity(bailStudioDto.getUtilisateurRequestDto()));

        return bailLocation;
    }
}
