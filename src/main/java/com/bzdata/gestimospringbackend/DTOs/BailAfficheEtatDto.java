package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BailAfficheEtatDto {
    String locataire;
    String biebImmo;
}
