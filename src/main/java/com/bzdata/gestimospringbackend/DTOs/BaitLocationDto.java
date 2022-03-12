package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.Bienimmobilier;
import com.bzdata.gestimospringbackend.Models.Charges;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;

import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

public class BaitLocationDto {
    String designationBail;
    String abrvCodeBail;
    boolean enCoursBail;
    boolean archiveBail;
    double montantCautionBail;
    int nbreMoisCautionBail;

    Date dateDebut;
    Date dateFin;
    //BienimmobilierDto bienimmobilierDto;
    UtilisateurRequestDto utilisateurRequestDto;
}
