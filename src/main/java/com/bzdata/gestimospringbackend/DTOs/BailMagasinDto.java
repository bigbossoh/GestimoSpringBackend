package com.bzdata.gestimospringbackend.DTOs;

import java.time.LocalDate;
import java.util.Date;

import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.Magasin;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BailMagasinDto {
    Long id;
    String designationBail;
    String abrvCodeBail;
    boolean enCoursBail;
    boolean archiveBail;
    double montantCautionBail;
    int nbreMoisCautionBail;

    LocalDate dateDebut;
    LocalDate dateFin;
    MagasinDto magasinDto;
    UtilisateurRequestDto utilisateurRequestDto;

    public static BailMagasinDto fromEntity(BailLocation bailLocation) {
        if (bailLocation == null) {
            return null;
        }
        return BailMagasinDto.builder()
                .abrvCodeBail(bailLocation.getAbrvCodeBail())
                .archiveBail(bailLocation.isArchiveBail())
                .dateDebut(bailLocation.getDateDebut())
                .dateFin(bailLocation.getDateFin())
                .designationBail(bailLocation.getDesignationBail())
                .enCoursBail(bailLocation.isEnCoursBail())
                .id(bailLocation.getId())
                .magasinDto(MagasinDto.fromEntity((Magasin) bailLocation.getBienImmobilierOperation()))
                .montantCautionBail(bailLocation.getMontantCautionBail())
                .nbreMoisCautionBail(bailLocation.getNbreMoisCautionBail())
                .utilisateurRequestDto(UtilisateurRequestDto.fromEntity(bailLocation.getUtilisateurOperation()))
                .build();
    }

    public static BailLocation toEntity(BailMagasinDto bailMagasinDto) {
        if (bailMagasinDto == null) {
            return null;
        }
        BailLocation bailLocation = new BailLocation();
        bailLocation.setAbrvCodeBail(bailMagasinDto.getAbrvCodeBail());
        bailLocation.setArchiveBail(bailLocation.isArchiveBail());
        bailLocation.setBienImmobilierOperation(MagasinDto.toEntity(bailMagasinDto.getMagasinDto()));
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
