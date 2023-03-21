package com.bzdata.gestimospringbackend.Models.hotel;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.bzdata.gestimospringbackend.Models.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EncaissementAccessoire extends AbstractEntity {
    double ancienSoldeAcce;
    double montantEncaisserAcce;
    double nouveauSoldeAcce;
    String modeEncaissement;
    @ManyToOne
    ClientHotel clientHotel;
    @ManyToOne
    PrestationHotel prestation;
    @ManyToOne
    Caissiere caissiere;
    @ManyToOne
    ContratReservation contratReservation;
}
