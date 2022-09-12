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
    Long idAgence;
    Long idCreateur;
    String nomEtage;
    String AbrvEtage;
    int numEtage;
    Long idImmeuble;

    public static EtageDto fromEntity(Etage etage) {
        if (etage == null) {
            return null;
        }
        return EtageDto.builder()
                .id(etage.getId())
                .AbrvEtage(etage.getAbrvEtage())
                .nomEtage(etage.getNomEtage())
                .numEtage(etage.getNumEtage())
                .idImmeuble(etage.getImmeuble().getId())
                .build();
    }

    // public static Etage toEntity(EtageDto etageDto) {
    // if (etageDto == null) {
    // return null;
    // }
    // Etage etage = new Etage();
    // etage.setId(etageDto.getId());
    // etage.setAbrvEtage(etageDto.getAbrvEtage());
    // etage.setNomEtage(etageDto.getNomEtage());
    // etage.setNumEtage(etageDto.getNumEtage());
    // etage.setImmeuble(ImmeubleDto.toEntity(etageDto.getImmeubleDto()));
    // return etage;
    // }
}
