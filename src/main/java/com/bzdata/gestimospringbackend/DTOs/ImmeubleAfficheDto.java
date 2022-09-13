package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ImmeubleAfficheDto {
    Long id;
    int nbrEtage;
    int nbrePieceImmeuble;
    String abrvNomImmeuble;
    String descriptionImmeuble;
    int numeroImmeuble;
    boolean isGarrage;
    String nomPropio;
    String prenomProprio;

}
