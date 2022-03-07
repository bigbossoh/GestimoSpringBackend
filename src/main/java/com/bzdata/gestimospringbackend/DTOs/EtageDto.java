package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Etage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EtageDto {
    Long id;
    String nomEtage;
    String AbrvEtage;
    int numEtage;
    ImmeubleDto immeubleDto;

    public static EtageDto fromEntity(Etage etage) {
        if (etage == null) {
            return null;
        }
        return EtageDto.builder()
                .id(etage.getId())
                .AbrvEtage(etage.getAbrvEtage())
                .nomEtage(etage.getNomEtage())
                .numEtage(etage.getNumEtage())
                .immeubleDto(ImmeubleDto.fromEntity(etage.getImmeuble()))
                .build();
    }

    public static Etage toEntity(EtageDto etageDto) {
        if (etageDto == null) {
            return null;
        }
        Etage etage = new Etage();
        etage.setId(etageDto.getId());
        etage.setAbrvEtage(etageDto.getAbrvEtage());
        etage.setNomEtage(etageDto.getNomEtage());
        etage.setNumEtage(etageDto.getNumEtage());
        etage.setImmeuble(ImmeubleDto.toEntity(etageDto.getImmeubleDto()));
        return etage;
    }
}
