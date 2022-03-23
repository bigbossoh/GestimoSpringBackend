package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
// @FieldDefaults(level = AccessLevel.PRIVATE)
public class AppelLoyerDto {
    private Long id;
    private Long idAgence;
    private String periodeAppelLoyer;
    private String statusAppelLoyer;
    private LocalDate datePaiementPrevuAppelLoyer;
    private LocalDate dateDebutMoisAppelLoyer;
    private LocalDate dateFinMoisAppelLoyer;
    private int anneeAppelLoyer;
    private int moisChiffreAppelLoyer;
    private String descAppelLoyer;
    private double soldeAppelLoyer;
    private boolean isSolderAppelLoyer;
    private double montantBailLPeriode;
    private BailLocation bailLocationAppelLoyer;

    public static AppelLoyerDto fromEntity(AppelLoyer appelLoyer) {
        if (appelLoyer == null) {
            return null;
        }
        return AppelLoyerDto.builder()
                .id(appelLoyer.getId())
                .idAgence(appelLoyer.getIdAgence())
                .periodeAppelLoyer(appelLoyer.getPeriodeAppelLoyer())
                .statusAppelLoyer(appelLoyer.getStatusAppelLoyer())
                .datePaiementPrevuAppelLoyer(appelLoyer.getDatePaiementPrevuAppelLoyer())
                .dateDebutMoisAppelLoyer(appelLoyer.getDateDebutMoisAppelLoyer())
                .dateFinMoisAppelLoyer(appelLoyer.getDateFinMoisAppelLoyer())
                .anneeAppelLoyer(appelLoyer.getAnneeAppelLoyer())
                .moisChiffreAppelLoyer(appelLoyer.getMoisChiffreAppelLoyer())
                .descAppelLoyer(appelLoyer.getDescAppelLoyer())
                .montantBailLPeriode(appelLoyer.getMontantBailLPeriode())
                .bailLocationAppelLoyer(appelLoyer.getBailLocationAppelLoyer())
                .soldeAppelLoyer(appelLoyer.getSoldeAppelLoyer())
                .isSolderAppelLoyer(appelLoyer.isSolderAppelLoyer())
                .build();
    }

    public static AppelLoyer toEntity(AppelLoyerDto dto) {
        if (dto == null) {
            return null;
        }
        AppelLoyer a = new AppelLoyer();
        a.setId(dto.getId());
        a.setIdAgence(dto.getIdAgence());
        a.setPeriodeAppelLoyer(dto.getPeriodeAppelLoyer());
        a.setStatusAppelLoyer(dto.getStatusAppelLoyer());
        a.setDatePaiementPrevuAppelLoyer(dto.getDatePaiementPrevuAppelLoyer());
        a.setDateDebutMoisAppelLoyer(dto.getDateDebutMoisAppelLoyer());
        a.setDateFinMoisAppelLoyer(dto.getDateFinMoisAppelLoyer());
        a.setAnneeAppelLoyer(dto.getAnneeAppelLoyer());
        a.setMoisChiffreAppelLoyer(dto.getMoisChiffreAppelLoyer());
        a.setDescAppelLoyer(dto.getDescAppelLoyer());
        a.setMontantBailLPeriode(dto.getMontantBailLPeriode());
        a.setBailLocationAppelLoyer(dto.getBailLocationAppelLoyer());
        a.setSoldeAppelLoyer(dto.getSoldeAppelLoyer());
        a.setSolderAppelLoyer(dto.isSolderAppelLoyer);
        return a;
    }
}
