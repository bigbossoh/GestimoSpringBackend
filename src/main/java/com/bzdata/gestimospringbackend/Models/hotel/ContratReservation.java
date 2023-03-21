package com.bzdata.gestimospringbackend.Models.hotel;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
public class ContratReservation extends AbstractEntity {
    LocalDate dateDebut;
    LocalDate dateFin;
    int nombreJour;
    double montantSejour;
    double tauxReduction;
    double montantReduction;
    double solde;
    String nomReserveation;
    int numeroReservation;
    int nbrPersonSejour;
    String detailContrat;
    @OneToMany(mappedBy = "contratReservation")
    List<EncaissementAccessoire> encaissementAccessoires;
    @ManyToOne
    ClientHotel clientHotelContart;
    @ManyToOne
    Caissiere caissiere;
    @OneToMany(mappedBy = "contratReservation")
    List<DetailContratReservation> detailContratReservationsContrat;
}
