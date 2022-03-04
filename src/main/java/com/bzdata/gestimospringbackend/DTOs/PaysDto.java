package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Pays;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaysDto {
    Long id;
    String abrvPays;
    String nomPays;

    public static PaysDto fromEntity(Pays pays) {
        if (pays == null) {
            return null;
        }
        return PaysDto.builder()
                .id(pays.getId())
                .abrvPays(pays.getAbrvPays())
                .nomPays(pays.getNomPays())
                .build();
    }

    public static Pays toEntity(PaysDto dto) {
        if (dto == null) {
            return null;
        }
        Pays pays = new Pays();
        pays.setAbrvPays(dto.getAbrvPays());
        pays.setId(dto.getId());
        pays.setNomPays(dto.getNomPays());
        return pays;
    }
}