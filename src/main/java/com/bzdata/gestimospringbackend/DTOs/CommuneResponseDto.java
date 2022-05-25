package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Commune;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommuneResponseDto {
    Long id;
    Long idAgence;
    String abrvCommune;
    String nomCommune;
    VilleDto villeDto;
    public static CommuneResponseDto fromEntity(Commune commune) {
        if (commune == null) {
            return null;
        }
        return CommuneResponseDto.builder()
                .id(commune.getId())
                .abrvCommune(commune.getAbrvCommune())
                .nomCommune(commune.getNomCommune())
                .idAgence(commune.getIdAgence())
                .villeDto(VilleDto.fromEntity(commune.getVille()))
                .build();
    }
}
