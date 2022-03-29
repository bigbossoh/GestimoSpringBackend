package com.bzdata.gestimospringbackend.DTOs;

import java.time.LocalDate;

import com.bzdata.gestimospringbackend.Models.EspeceEncaissement;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EspeceEncaissementDto {
    LocalDate dateEncaissement;
    double montantEncaissement;
    Long idUtilisateurEncaissement;
    Long idAppelLoyerEncaissement;

    public static EspeceEncaissementDto fromEntity(EspeceEncaissement especeEncaissement) {
        if (especeEncaissement == null) {
            return null;
        }
        return EspeceEncaissementDto.builder()
                .dateEncaissement(especeEncaissement.getDateEncaissement())
                .montantEncaissement(especeEncaissement.getMontantEncaissement())
                .idUtilisateurEncaissement(especeEncaissement.getUtilisateurEncaissement().getId())
                .idAppelLoyerEncaissement(especeEncaissement.getAppelLoyerEncaissement().getId())
                .build();
    }

}
