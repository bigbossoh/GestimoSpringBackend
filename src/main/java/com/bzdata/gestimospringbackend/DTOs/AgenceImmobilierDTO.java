package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.AbstractEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AgenceImmobilierDTO {
    Long id;
    String nomAgence;
    String telAgence;
    String compteContribuable;
    double capital;
    String emailAgence;
    String mobileAgence;
    String regimeFiscaleAgence;
    String faxAgence;
    String sigleAgence;
    Long idAgence;




}
