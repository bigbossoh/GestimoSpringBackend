package com.bzdata.gestimospringbackend.DTOs;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MontantLoyerBailDto {
    Long id;
    Long idAgence;
    double montantLoyer;
    LocalDate debutLoyer;
    LocalDate finLoyer;
    boolean statusLoyer;
    double tauxLoyer;
    double montantAugmentation;
    BailLocation bailLocation;

    public static MontantLoyerBailDto fromEntity(MontantLoyerBail montantLoyerBail) {
        if (montantLoyerBail == null) {
            return null;
        }
        return MontantLoyerBailDto.builder()
                .id(montantLoyerBail.getId())
                .idAgence(montantLoyerBail.getIdAgence())
                .montantLoyer(montantLoyerBail.getMontantLoyer())
                .debutLoyer(montantLoyerBail.getDebutLoyer())
                .finLoyer(montantLoyerBail.getFinLoyer())
                .statusLoyer(montantLoyerBail.isStatusLoyer())
                .tauxLoyer(montantLoyerBail.getTauxLoyer())
                .montantAugmentation(montantLoyerBail.getMontantAugmentation())
                .bailLocation(montantLoyerBail.getBailLocation())
                .build();
    }

    public static MontantLoyerBail toEntity(MontantLoyerBailDto dto) {
        if (dto == null) {
            return null;
        }
        MontantLoyerBail m = new MontantLoyerBail();
        m.setId(dto.getId());
        m.setIdAgence(dto.getIdAgence());
        m.setMontantLoyer(dto.getMontantLoyer());
        m.setDebutLoyer(dto.getDebutLoyer());
        m.setFinLoyer(dto.getFinLoyer());
        m.setStatusLoyer(dto.isStatusLoyer());
        m.setTauxLoyer(dto.getTauxLoyer());
        m.setMontantAugmentation(dto.getMontantAugmentation());
        m.setBailLocation(dto.getBailLocation());
        return m;
    }
}