package com.bzdata.gestimospringbackend.DTOs;

import java.time.LocalDate;

import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.Villa;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BailVillaDto {
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
    VillaDto villaDto;
    UtilisateurRequestDto utilisateurRequestDto;

    public static BailVillaDto fromEntity(BailLocation bailLocation) {
        if (bailLocation == null) {
            return null;
        }
        return BailVillaDto.builder()
                .abrvCodeBail(bailLocation.getAbrvCodeBail())
                .archiveBail(bailLocation.isArchiveBail())
                .idAgence(bailLocation.getIdAgence())
                .dateDebut(bailLocation.getDateDebut())
                .dateFin(bailLocation.getDateFin())
                .designationBail(bailLocation.getDesignationBail())
                .enCoursBail(bailLocation.isEnCoursBail())
                .id(bailLocation.getId())
                .villaDto(VillaDto.fromEntity((Villa) bailLocation.getBienImmobilierOperation()))
                .montantCautionBail(bailLocation.getMontantCautionBail())
                .nbreMoisCautionBail(bailLocation.getNbreMoisCautionBail())
                .utilisateurRequestDto(UtilisateurRequestDto.fromEntity(bailLocation.getUtilisateurOperation()))
                .build();
    }

    public static BailLocation toEntity(BailVillaDto bailVillaDto) {
        if (bailVillaDto == null) {
            return null;
        }
        BailLocation bailLocation = new BailLocation();
        bailLocation.setAbrvCodeBail(bailVillaDto.getAbrvCodeBail());
        bailLocation.setArchiveBail(bailLocation.isArchiveBail());
        bailLocation.setBienImmobilierOperation(VillaDto.toEntity(bailVillaDto.getVillaDto()));
        bailLocation.setIdAgence(bailVillaDto.getIdAgence());
        bailLocation.setDateDebut(bailVillaDto.getDateDebut());
        bailLocation.setDateFin(bailVillaDto.getDateFin());
        bailLocation.setDesignationBail(bailVillaDto.getDesignationBail());
        bailLocation.setEnCoursBail(bailVillaDto.isEnCoursBail());
        bailLocation.setId(bailVillaDto.getId());
        bailLocation.setMontantCautionBail(bailVillaDto.getMontantCautionBail());
        bailLocation.setNbreMoisCautionBail(bailVillaDto.getNbreMoisCautionBail());
        bailLocation.setUtilisateurOperation(UtilisateurRequestDto.toEntity(bailVillaDto.getUtilisateurRequestDto()));

        return bailLocation;
    }
}
