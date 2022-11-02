package com.bzdata.gestimospringbackend.DTOs;

import java.time.LocalDate;

import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MontantLoyerBailDto {
    Long id;
    Long idAgence;
    Long idCreateur;
    double ancienMontantLoyer;
    double nouveauMontantLoyer;
    LocalDate debutLoyer;
    LocalDate finLoyer;
    boolean statusLoyer;
    double tauxLoyer;
    double montantAugmentation;
    Long bailLocation;

    public static MontantLoyerBailDto fromEntity(MontantLoyerBail montantLoyerBail) {
        if (montantLoyerBail == null) {
            return null;
        }
        return MontantLoyerBailDto.builder()
                .id(montantLoyerBail.getId())
                .idAgence(montantLoyerBail.getIdAgence())
                .ancienMontantLoyer(montantLoyerBail.getAncienMontantLoyer())
                .nouveauMontantLoyer(montantLoyerBail.getNouveauMontantLoyer())
                .debutLoyer(montantLoyerBail.getDebutLoyer())
                .finLoyer(montantLoyerBail.getFinLoyer())
                .statusLoyer(montantLoyerBail.isStatusLoyer())
                .tauxLoyer(montantLoyerBail.getTauxLoyer())
                .montantAugmentation(montantLoyerBail.getMontantAugmentation())
                .bailLocation(montantLoyerBail.getBailLocation().getId())
                .build();
    }

    public static MontantLoyerBail toEntity(MontantLoyerBailDto dto) {
        if (dto == null) {
            return null;
        }
        MontantLoyerBail m = new MontantLoyerBail();
        m.setId(dto.getId());
        m.setIdAgence(dto.getIdAgence());
        m.setAncienMontantLoyer(dto.getAncienMontantLoyer());
        m.setNouveauMontantLoyer(dto.getNouveauMontantLoyer());
        m.setDebutLoyer(dto.getDebutLoyer());
        m.setFinLoyer(dto.getFinLoyer());
        m.setStatusLoyer(dto.isStatusLoyer());
        m.setTauxLoyer(dto.getTauxLoyer());
        m.setMontantAugmentation(dto.getMontantAugmentation());
        // m.setBailLocation(dto.getBailLocation());
        return m;
    }
}
