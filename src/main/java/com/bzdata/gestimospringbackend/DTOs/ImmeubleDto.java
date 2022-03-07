package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Immeuble;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImmeubleDto {
    Long id;
    String numBien;
    String statutBien;
    String denominationBien;
    String nomBien;
    String etatBien;
    double superficieBien;
    boolean isOccupied;

    SiteRequestDto siteRequestDto;

    int nbrEtage;
    int nbrePieceImmeuble;
    String abrvNomImmeuble;
    String descriptionImmeuble;
    int numeroImmeuble;
    boolean isGarrage;

    public static ImmeubleDto fromEntity(Immeuble immeuble) {
        if (immeuble == null) {
            return null;
        }
        return ImmeubleDto.builder()
                .id(immeuble.getId())
                .abrvNomImmeuble(immeuble.getAbrvNomImmeuble())
                .denominationBien(immeuble.getDenominationBien())
                .descriptionImmeuble(immeuble.getDescriptionImmeuble())
                .denominationBien(immeuble.getDenominationBien())
                .etatBien(immeuble.getEtatBien())
                .isGarrage(immeuble.isGarrage())
                .isOccupied(immeuble.isOccupied())
                .nbrEtage(immeuble.getNbrEtage())
                .nbrePieceImmeuble(immeuble.getNbrePieceImmeuble())
                .nomBien(immeuble.getNomBien())
                .numBien(immeuble.getNumBien())
                .numeroImmeuble(immeuble.getNumeroImmeuble())
                .siteRequestDto(SiteRequestDto.fromEntity(immeuble.getSite()))
                .build();
    }

    public static Immeuble toEntity(ImmeubleDto dto) {
        if (dto == null) {
            return null;
        }
        Immeuble immeuble = new Immeuble();
        immeuble.setAbrvNomImmeuble(dto.getAbrvNomImmeuble());
        immeuble.setDenominationBien(dto.getDenominationBien());
        immeuble.setDescriptionImmeuble(dto.getDescriptionImmeuble());
        immeuble.setEtatBien(dto.getEtatBien());
        immeuble.setGarrage(dto.isGarrage());
        immeuble.setId(dto.getId());
        immeuble.setNbrEtage(dto.getNbrEtage());
        immeuble.setNbrePieceImmeuble(dto.getNbrePieceImmeuble());
        immeuble.setNomBien(dto.getNomBien());
        immeuble.setNumBien(dto.getNumBien());
        immeuble.setNumeroImmeuble(dto.getNumeroImmeuble());
        immeuble.setOccupied(dto.isOccupied());
        immeuble.setStatutBien(dto.getStatutBien());
        immeuble.setSuperficieBien(dto.getSuperficieBien());
        immeuble.setSite(SiteRequestDto.toEntity(dto.getSiteRequestDto()));

        return immeuble;
    }
}