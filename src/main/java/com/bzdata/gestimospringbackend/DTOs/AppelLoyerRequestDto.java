package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppelLoyerRequestDto {

    Long idAgence;
    Long idCreateur;
    Long idBailLocation;
    double montantLoyerEnCours;
}
