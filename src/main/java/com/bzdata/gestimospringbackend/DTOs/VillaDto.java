package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Villa;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VillaDto {
    Long id;
    Long idAgence;
    Long idCreateur;
    int nbrChambreVilla;
    int nbrePiece;
    int nbrSalonVilla;
    int nbrSalleEauVilla;
    String nomVilla;
    String abrvVilla;
    boolean garageVilla;
    int nbreVoitureGarageVilla;
    Long numBien;
    String statutBien;
    boolean isArchived;
    String abrvBienimmobilier;
    String description;
    String nomBien;
    double superficieBien;
    boolean isOccupied;
    Long idSite;
    Long idUtilisateur;
    String proprietaire;

    public static VillaDto fromEntity(Villa villa) {
        if (villa == null) {
            return null;
        }
        return VillaDto.builder()
                .id(villa.getId())
                .idAgence(villa.getIdAgence())
                .nbrChambreVilla(villa.getNbrChambreVilla())
                .nomBien(villa.getNomBien())
                .superficieBien(villa.getSuperficieBien())
                .nbrePiece(villa.getNbrePiece())
                .nbrSalonVilla(villa.getNbrSalonVilla())
                .isOccupied(villa.isOccupied())
                .isArchived(villa.isArchived())
                .garageVilla(villa.isGarageVilla())
                .nbreVoitureGarageVilla(villa.getNbreVoitureGarageVilla())
                .abrvVilla(villa.getAbrvVilla())
                .nomVilla(villa.getNomVilla())
                .nbrSalleEauVilla(villa.getNbrSalleEauVilla())
                .description(villa.getDescription())
                .abrvBienimmobilier(villa.getAbrvBienimmobilier())
                .statutBien(villa.getStatutBien())
                .numBien(villa.getNumBien())
                .idSite(villa.getSite().getId())
                .idUtilisateur(villa.getUtilisateur().getId())
                .proprietaire(villa.getUtilisateur().getNom()+" "+villa.getUtilisateur().getPrenom())
                .build();
    }

    // public static Villa toEntity(VillaDto dto) {

    // if (dto == null) {
    // return null;
    // }
    // Villa v = new Villa();
    // v.setId(dto.getId());
    // v.setIdAgence(dto.getIdAgence());
    // v.setNbrChambreVilla(dto.getNbrChambreVilla());
    // v.setNomBien(dto.getNomBien());
    // v.setSuperficieBien(dto.getSuperficieBien());
    // v.setNbrePiece(dto.getNbrePiece());
    // v.setNbrSalonVilla(dto.getNbrSalonVilla());
    // v.setOccupied(dto.isOccupied());
    // v.setArchived(dto.isArchived());
    // v.setGarageVilla(dto.isGarageVilla());
    // v.setNbreVoitureGarageVilla(dto.getNbreVoitureGarageVilla());
    // v.setAbrvVilla(dto.getAbrvVilla());
    // v.setNomVilla(dto.getNomVilla());
    // v.setNbrSalleEauVilla(dto.getNbrSalleEauVilla());
    // v.setDescription(dto.getDescription());
    // v.setAbrvBienimmobilier(dto.getAbrvBienimmobilier());
    // v.setStatutBien(dto.getStatutBien());
    // v.setNumBien(dto.getNumBien());
    // v.setSite(SiteRequestDto.toEntity(dto.getSiteRequestDto()));
    // v.setUtilisateur(UtilisateurRequestDto.toEntity(dto.getUtilisateurRequestDto()));
    // return v;

    // }
}
