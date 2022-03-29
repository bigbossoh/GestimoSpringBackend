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
    Long idCommune;
    private Long idAgence;

    public static QuartierDto fromEntity(Quartier quartier) {
        if (quartier == null) {
            return null;
        }
        return QuartierDto.builder()
                .id(quartier.getId())
                .abrvQuartier(quartier.getAbrvQuartier())
                .nomQuartier(quartier.getNomQuartier())
                .idCommune(quartier.getCommune().getId())
                .idAgence(quartier.getIdAgence())
                .build();
    }
}
