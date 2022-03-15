package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppelLoyerRequestDto {

    Long idAgence;
    Long idBailLocation;
    double montantLoyerEnCours;
}
