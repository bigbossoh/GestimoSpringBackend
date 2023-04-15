package com.bzdata.gestimospringbackend.Models.hotel;

import java.util.List;

import javax.persistence.Entity;
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

public class Caissiere extends AbstractEntity {
    String nomCaisse;
    String prenomCaisse;
    String login;
    @OneToMany(mappedBy = "caissiere")
    List<EncaissementAccessoire> encaissementAccessoiresCaisse;
    @OneToMany(mappedBy = "caissiere")
    List<ContratReservation> contratReservationsCaisse;

}
