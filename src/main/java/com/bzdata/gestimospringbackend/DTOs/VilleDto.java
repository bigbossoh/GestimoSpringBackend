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
    String abrvVille;
    String nomVille;
    PaysDto pays;

    public static VilleDto fromEntity(Ville ville) {
        if (ville == null) {
            return null;
        }
        return VilleDto.builder()
                .id(ville.getId())
                .abrvVille(ville.getAbrvVille())
                .nomVille(ville.getNomVille())
                .pays(PaysDto.fromEntity(ville.getPays()))
                .build();
    }

    public static Ville toEntity(VilleDto dto) {

        if (dto == null) {
            return null;
        }
        Ville v = new Ville();
        v.setId(dto.getId());
        v.setAbrvVille(dto.getAbrvVille());
        v.setNomVille(dto.getNomVille());
        v.setPays(PaysDto.toEntity(dto.getPays()));
        return v;

    }
}
