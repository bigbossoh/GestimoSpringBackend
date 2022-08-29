package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnneeAppelLoyersDto {

    int anneeAppelLoyer;

}
