package com.bzdata.gestimospringbackend.Models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.bzdata.gestimospringbackend.Models.hotel.CategorieChambre;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@DiscriminatorValue("Appartement")
public class Appartement extends Bienimmobilier {

    int nbrPieceApp;
    int nbreChambreApp;
    int nbreSalonApp;
    int nbreSalleEauApp;
    Double prixReservation=0.0;
    Long numApp;
    @ManyToOne(fetch = FetchType.LAZY)
    Etage etageAppartement;
    @ManyToOne(fetch = FetchType.LAZY)
     CategorieChambre categorieApartement;

}
