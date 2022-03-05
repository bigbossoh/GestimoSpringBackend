package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Quartier;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuartierDto {
    Long id;
    String abrvQuartier;
    String nomQuartier;
    CommuneDto communeDto;

    public static QuartierDto fromEntity(Quartier quartier) {
        if (quartier == null) {
            return null;
        }
        return QuartierDto.builder()
                .id(quartier.getId())
                .abrvQuartier(quartier.getAbrvQuartier())
                .nomQuartier(quartier.getNomQuartier())
                .communeDto(CommuneDto.fromEntity(quartier.getCommune()))
                .build();
    }

    public static Quartier toEntity(QuartierDto dto) {
        if (dto == null) {
            return null;
        }
        Quartier quartier = new Quartier();
        quartier.setId(dto.getId());
        quartier.setAbrvQuartier(dto.getAbrvQuartier());
        quartier.setNomQuartier(dto.getNomQuartier());
        quartier.setCommune(CommuneDto.toEntity(dto.getCommuneDto()));
        return quartier;

    }
}
