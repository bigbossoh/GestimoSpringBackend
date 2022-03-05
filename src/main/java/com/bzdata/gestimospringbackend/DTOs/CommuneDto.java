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
    private Long idAgence;
    String abrvCommune;
    String nomCommune;
    VilleDto villeDto;

    public static CommuneDto fromEntity(Commune commune) {
        if (commune == null) {
            return null;
        }
        return CommuneDto.builder()
                .id(commune.getId())
                .abrvCommune(commune.getAbrvCommune())
                .nomCommune(commune.getNomCommune())
                .idAgence(commune.getIdAgence())
                .villeDto(VilleDto.fromEntity(commune.getVille())).build();
    }

    public static Commune toEntity(CommuneDto dto) {
        if (dto == null) {
            return null;
        }
        Commune commune = new Commune();
        commune.setId(dto.getId());
        commune.setAbrvCommune(dto.getAbrvCommune());
        commune.setNomCommune(dto.getNomCommune());
        commune.setIdAgence(dto.getIdAgence());
        commune.setVille(VilleDto.toEntity(dto.getVilleDto()));
        return commune;
    }
}
