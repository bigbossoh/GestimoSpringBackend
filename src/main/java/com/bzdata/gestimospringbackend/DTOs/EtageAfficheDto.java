package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EtageAfficheDto {
    String nomEtage;
    String AbrvEtage;
    int numEtage;
    String nomImmeuble;
    String nomPropio;
    String prenomProprio;
}
