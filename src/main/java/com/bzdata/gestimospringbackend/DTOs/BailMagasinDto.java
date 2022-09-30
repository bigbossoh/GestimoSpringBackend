package com.bzdata.gestimospringbackend.DTOs;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BailMagasinDto {
   
    Long id;
    Long idAgence;
    Long idCreateur;
    String designationBail;
    String abrvCodeBail;
    boolean enCoursBail;
    boolean archiveBail;
    double montantCautionBail;
    int nbreMoisCautionBail;
    double nouveauMontantLoyer;

    LocalDate dateDebut;
    LocalDate dateFin;
    Long idMagasin;
    Long idLocataire;
    String nomLocataire;    
    String codeBien;
}
