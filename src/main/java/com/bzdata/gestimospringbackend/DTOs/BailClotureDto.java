package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BailClotureDto {
    Long idBail;
    LocalDate dateCloture;
}
