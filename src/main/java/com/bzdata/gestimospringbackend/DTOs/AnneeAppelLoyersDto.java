package com.bzdata.gestimospringbackend.DTOs;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnneeAppelLoyersDto {

    String periodeLettre;
    String periodeAppelLoyer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnneeAppelLoyersDto)) return false;
        AnneeAppelLoyersDto that = (AnneeAppelLoyersDto) o;
        return Objects.equals(getPeriodeLettre(), that.getPeriodeLettre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPeriodeLettre());
    }
}
