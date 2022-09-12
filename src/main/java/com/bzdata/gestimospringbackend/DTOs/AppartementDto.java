package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
// @Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppartementDto {
    Long id;
    Long idAgence;
    Long idCreateur;
    boolean meubleApp;
    int nbrPieceApp;
    int nbreChambreApp;
    int nbreSalonApp;
    int nbreSalleEauApp;
    boolean isResidence;
    boolean occupied;
    int numeroApp;
    String abrvNomApp;
    String nomApp;
    Long idEtage;
    String proprietaire;
    

    // public static AppartementDto fromEntity(Appartement appartement) {
    //     if (appartement == null) {
    //         return null;
    //     }
    //     return AppartementDto.builder()
    //             .id(appartement.getId())
    //             .abrvNomApp(appartement.getAbrvNomApp())
    //             .idEtage(appartement.getEtageAppartement().getId())
    //             .isResidence(appartement.isResidence())
    //             .occupied(appartement.isOccupied())
    //             .meubleApp(appartement.isMeubleApp())
    //             .nbrPieceApp(appartement.getNbrPieceApp())
    //             .nbreChambreApp(appartement.getNbreChambreApp())
    //             .nbreSalleEauApp(appartement.getNbreSalleEauApp())
    //             .nbreSalonApp(appartement.getNbreSalonApp())
    //             .nomApp(appartement.getNomApp())
    //             .numeroApp(appartement.getNumeroApp())
    //             .proprietaire(appartement.getEtageAppartement().getImmeuble().getUtilisateur().getNom()+" "+appartement.getEtageAppartement().getImmeuble().getUtilisateur().getPrenom())
    //             .build();

    // }
}
