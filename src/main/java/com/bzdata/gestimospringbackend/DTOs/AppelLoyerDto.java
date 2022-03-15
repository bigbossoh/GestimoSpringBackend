package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
//@FieldDefaults(level = AccessLevel.PRIVATE)
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
    private double montantBailLPeriode;
    private BailLocation bailLocationAppelLoyer;

    public static AppelLoyerDto fromEntity(AppelLoyer AppelLoyer) {
        if (AppelLoyer == null) {
            return null;
        }
        return AppelLoyerDto.builder()
                .id(AppelLoyer.getId())
                .idAgence(AppelLoyer.getIdAgence())
                .periodeAppelLoyer(AppelLoyer.getPeriodeAppelLoyer())
                .statusAppelLoyer(AppelLoyer.getStatusAppelLoyer())
                .datePaiementPrevuAppelLoyer(AppelLoyer.getDatePaiementPrevuAppelLoyer())
                .dateDebutMoisAppelLoyer(AppelLoyer.getDateDebutMoisAppelLoyer())
                .dateFinMoisAppelLoyer(AppelLoyer.getDateFinMoisAppelLoyer())
                .anneeAppelLoyer(AppelLoyer.getAnneeAppelLoyer())
                .moisChiffreAppelLoyer(AppelLoyer.getMoisChiffreAppelLoyer())
                .descAppelLoyer(AppelLoyer.getDescAppelLoyer())
                .montantBailLPeriode(AppelLoyer.getMontantBailLPeriode())
                .bailLocationAppelLoyer(AppelLoyer.getBailLocationAppelLoyer())
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
        return a;
    }
}

