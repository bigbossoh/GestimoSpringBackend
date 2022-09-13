package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Villa;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data

@FieldDefaults(level = AccessLevel.PRIVATE)
public class VillaDto {
    Long id;
    Long idAgence;
    Long idCreateur;
    int nbrChambreVilla;
    int nbrePiece;
    int nbrSalonVilla;
    int nbrSalleEauVilla;
    String nomVilla;
    String abrvVilla;
    boolean garageVilla;
    int nbreVoitureGarageVilla;
    Long numBien;
    String statutBien;
    boolean isArchived;
    String abrvBienimmobilier;
    String description;
    String nomBien;
    double superficieBien;
    boolean isOccupied;
    
    Long idSite;
    Long idUtilisateur;
    String proprietaire;
}
