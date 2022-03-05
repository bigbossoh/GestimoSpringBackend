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
    private Long idAgence;
    String abrvVille;
    String nomVille;
    PaysDto paysDto;

    public static VilleDto fromEntity(Ville ville) {
        if (ville == null) {
            return null;
        }
        return VilleDto.builder()
                .id(ville.getId())
                .idAgence(ville.getIdAgence())
                .abrvVille(ville.getAbrvVille())
                .nomVille(ville.getNomVille())
                .paysDto(PaysDto.fromEntity(ville.getPays()))
                .build();
    }

    public static Ville toEntity(VilleDto dto) {

        if (dto == null) {
            return null;
        }
        Ville v = new Ville();
        v.setId(dto.getId());
        v.setIdAgence(dto.getIdAgence());
        v.setAbrvVille(dto.getAbrvVille());
        v.setNomVille(dto.getNomVille());
        v.setPays(PaysDto.toEntity(dto.getPaysDto()));
        return v;

    }
}
