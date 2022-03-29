package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Commune;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommuneDto {
    Long id;
    Long idAgence;
    String abrvCommune;
    String nomCommune;
    Long idVille;

    public static CommuneDto fromEntity(Commune commune) {
        if (commune == null) {
            return null;
        }
        return CommuneDto.builder()
                .id(commune.getId())
                .abrvCommune(commune.getAbrvCommune())
                .nomCommune(commune.getNomCommune())
                .idAgence(commune.getIdAgence())
                .idVille(commune.getVille().getId()).build();
    }
}
