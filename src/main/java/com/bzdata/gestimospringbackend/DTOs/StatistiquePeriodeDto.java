package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatistiquePeriodeDto {
    double impayer;
    double payer;
    double recouvrement;
    String periode;
    String periodeFin;
    double totalLoyer;
}
