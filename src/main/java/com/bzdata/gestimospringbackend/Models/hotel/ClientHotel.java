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
public class ClientHotel extends AbstractEntity {
    String nomClient;
    String prenomClient;
    String portableClient;
    String emailClient;
    String villeClient;
    String addres;
    String fixeClient;
    String civiliteClient;
    double soldeClient;
    String professionClient;
    String lieuNaissanceClient;
    String nomMereClient;
    String nomPereClient;
    String nationaliteClient;
    int nbrEnfantClient;
    String pieceIdentiteClient;
    String naturePieceClient;
    @OneToMany(mappedBy = "clientHotel")
    List<EncaissementAccessoire> encaissementAccessoires;
    @OneToMany(mappedBy = "clientHotel")
    List<ServicesHotel> servicesHotelsClient;
    @OneToMany(mappedBy = "clientHotelContart")
    List<ContratReservation> lContratReservationsClient;

}
