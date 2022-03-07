package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Appartement;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppartementDto {
    Long id;
    boolean meubleApp;
    int nbrPieceApp;
    int nbreChambreApp;
    int nbreSalonApp;
    int nbreSalleEauApp;
    boolean isResidence;
    int numeroApp;
    String abrvNomApp;
    String nomApp;
    EtageDto etageDto;

    public static AppartementDto fromEntity(Appartement appartement) {
        if (appartement == null) {
            return null;
        }
        return AppartementDto.builder()
                .id(appartement.getId())
                .abrvNomApp(appartement.getAbrvNomApp())
                .etageDto(EtageDto.fromEntity(appartement.getEtageAppartement()))
                .isResidence(appartement.isResidence())
                .meubleApp(appartement.isMeubleApp())
                .nbrPieceApp(appartement.getNbrPieceApp())
                .nbreChambreApp(appartement.getNbreChambreApp())
                .nbreSalleEauApp(appartement.getNbreSalleEauApp())
                .nbreSalonApp(appartement.getNbreSalonApp())
                .nomApp(appartement.getNomApp())
                .numeroApp(appartement.getNumeroApp())
                .build();
    }

    public static Appartement toEntity(AppartementDto dto) {
        if (dto == null) {
            return null;
        }
        Appartement appartement = new Appartement();
        appartement.setAbrvNomApp(dto.getAbrvNomApp());
        appartement.setEtageAppartement(EtageDto.toEntity(dto.getEtageDto()));
        appartement.setId(dto.getId());
        appartement.setMeubleApp(dto.isMeubleApp());
        appartement.setNbrPieceApp(dto.getNbrPieceApp());
        appartement.setNbreChambreApp(dto.getNbreChambreApp());
        appartement.setNbreSalleEauApp(dto.getNbreSalleEauApp());
        appartement.setNumeroApp(dto.getNumeroApp());
        appartement.setNomApp(dto.getNomApp());
        appartement.setResidence(dto.isResidence());

        return appartement;
    }
}
