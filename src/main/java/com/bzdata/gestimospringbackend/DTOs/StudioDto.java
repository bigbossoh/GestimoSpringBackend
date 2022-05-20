package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Studio;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudioDto {
    Long id;
    String descStudio;
    int numeroStudio;
    String abrvNomStudio;
    String nomStudio;
    Long idEtage;

    public static StudioDto fromEntity(Studio studio) {
        if (studio == null) {
            return null;
        }
        return StudioDto.builder()
                .id(studio.getId())
                .abrvNomStudio(studio.getAbrvNomStudio())
                .descStudio(studio.getDescStudio())
                .nomStudio(studio.getNomStudio())
                .numeroStudio(studio.getNumeroStudio())
                // .idEtage(studio.getEtageStudio().getId())
                .build();
    }

    // public static Studio toEntity(StudioDto dto) {
    // if (dto == null) {
    // return null;
    // }
    // Studio studio = new Studio();
    // studio.setAbrvNomStudio(dto.getAbrvNomStudio());
    // studio.setDescStudio(dto.getDescStudio());
    // studio.setId(dto.getId());
    // studio.setNomStudio(dto.getNomStudio());
    // studio.setNumeroStudio(dto.getNumeroStudio());
    // studio.setEtageStudio(EtageDto.toEntity(dto.getEtageDto()));
    // return studio;
    // }
}
