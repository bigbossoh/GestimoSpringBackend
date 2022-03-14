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
    String designationBail;
    String abrvCodeBail;
    boolean enCoursBail;
    boolean archiveBail;
    double montantCautionBail;
    int nbreMoisCautionBail;

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

    public static BailLocation toEntity(BailVillaDto bailMagasinDto) {
        if (bailMagasinDto == null) {
            return null;
        }
        BailLocation bailLocation = new BailLocation();
        bailLocation.setAbrvCodeBail(bailMagasinDto.getAbrvCodeBail());
        bailLocation.setArchiveBail(bailLocation.isArchiveBail());
        bailLocation.setBienImmobilierOperation(VillaDto.toEntity(bailMagasinDto.getVillaDto()));
        bailLocation.setDateDebut(bailMagasinDto.getDateDebut());
        bailLocation.setDateFin(bailMagasinDto.getDateFin());
        bailLocation.setDesignationBail(bailMagasinDto.getDesignationBail());
        bailLocation.setEnCoursBail(bailMagasinDto.isEnCoursBail());
        bailLocation.setId(bailMagasinDto.getId());
        bailLocation.setMontantCautionBail(bailMagasinDto.getMontantCautionBail());
        bailLocation.setNbreMoisCautionBail(bailMagasinDto.getNbreMoisCautionBail());
        bailLocation.setUtilisateurOperation(UtilisateurRequestDto.toEntity(bailMagasinDto.getUtilisateurRequestDto()));

        return bailLocation;
    }
}
