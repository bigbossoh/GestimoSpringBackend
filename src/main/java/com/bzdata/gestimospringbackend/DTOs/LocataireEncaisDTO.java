package com.bzdata.gestimospringbackend.DTOs;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocataireEncaisDTO {
    Long id;
     String nom;
     String prenom;

}
