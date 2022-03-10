package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Magasin;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MagasinDto {
    Long id;
    Long idAgence;
    Long numBien;
    String statutBien;
    boolean isArchived= false;
    String abrvBienimmobilier;
    String description;
    String nomBien;
    double superficieBien;
    boolean isOccupied= false;
    boolean isUnderBuildingMagasin= false;
    String abrvNomMagasin;
    int nmbrPieceMagasin;
    String nomMagasin;
    EtageDto etageMagasinDto;
    SiteRequestDto siteRequestDto;
    UtilisateurRequestDto utilisateurRequestDto;

    public static MagasinDto fromEntity(Magasin magasin) {
        if (magasin == null) {
            return null;
        }

        return MagasinDto.builder()
                .id(magasin.getId())
                .idAgence(magasin.getIdAgence())
                .numBien(magasin.getNumBien())
                .statutBien(magasin.getStatutBien())
                .isArchived(magasin.isArchived())
                .abrvBienimmobilier(magasin.getAbrvBienimmobilier())
                .description(magasin.getDescription())
                .nomBien(magasin.getNomBien())
                .superficieBien(magasin.getSuperficieBien())
                .isOccupied(magasin.isOccupied())
                .isUnderBuildingMagasin(magasin.isUnderBuildingMagasin())
                .abrvNomMagasin(magasin.getAbrvNomMagasin())
                .nmbrPieceMagasin(magasin.getNmbrPieceMagasin())
                .nomMagasin(magasin.getNomMagasin())
                .etageMagasinDto(EtageDto.fromEntity(magasin.getEtageMagasin()))
                .siteRequestDto(SiteRequestDto.fromEntity(magasin.getSite()))
                .utilisateurRequestDto(UtilisateurRequestDto.fromEntity(magasin.getUtilisateur()))
                .build();
    }
    public static Magasin toEntity(MagasinDto dto) {

        if (dto == null) {
            return null;
        }
        Magasin m = new Magasin();
        m.setId(dto.getId());
        m.setIdAgence(dto.getIdAgence());
        m.setNumBien(dto.getNumBien());
        m.setStatutBien(dto.getStatutBien());
        m.setArchived(dto.isArchived());
        m.setAbrvBienimmobilier(dto.getAbrvBienimmobilier());
        m.setDescription(dto.getDescription());
        m.setNomBien(dto.getNomBien());
        m.setSuperficieBien(dto.getSuperficieBien());
        m.setOccupied(dto.isOccupied());
        m.setUnderBuildingMagasin(dto.isUnderBuildingMagasin());
        m.setAbrvNomMagasin(dto.getAbrvNomMagasin());
        m.setNmbrPieceMagasin(dto.getNmbrPieceMagasin());
        m.setNomMagasin(dto.getNomMagasin());
       m.setEtageMagasin(EtageDto.toEntity(dto.getEtageMagasinDto()));
       m.setSite(SiteRequestDto.toEntity(dto.getSiteRequestDto()));
       m.setUtilisateur(UtilisateurRequestDto.toEntity(dto.getUtilisateurRequestDto()));
        return m;

    }
}
