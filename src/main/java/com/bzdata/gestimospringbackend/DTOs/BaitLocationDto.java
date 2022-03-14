package com.bzdata.gestimospringbackend.DTOs;

import java.util.Date;

public class BaitLocationDto {
    String designationBail;
    String abrvCodeBail;
    boolean enCoursBail;
    boolean archiveBail;
    double montantCautionBail;
    int nbreMoisCautionBail;

    Date dateDebut;
    Date dateFin;
    // BienimmobilierDto bienimmobilierDto;
    UtilisateurRequestDto utilisateurRequestDto;
}
