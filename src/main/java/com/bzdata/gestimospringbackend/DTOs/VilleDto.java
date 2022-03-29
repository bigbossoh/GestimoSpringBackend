package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Ville;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class VilleDto {
    Long id;
    Long idAgence;
    String abrvVille;
    String nomVille;
    Long idPays;

    public static VilleDto fromEntity(Ville ville) {
        if (ville == null) {
            return null;
        }
        return VilleDto.builder()
                .id(ville.getId())
                .idAgence(ville.getIdAgence())
                .abrvVille(ville.getAbrvVille())
                .nomVille(ville.getNomVille())
                .idPays(ville.getPays().getId())
                .build();
    }
}
