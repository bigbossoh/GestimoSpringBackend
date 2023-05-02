package com.bzdata.gestimospringbackend.Models.hotel;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.bzdata.gestimospringbackend.Models.AbstractEntity;
import com.bzdata.gestimospringbackend.Models.EncaissementPrincipal;

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
@DiscriminatorValue("Reservation")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reservation extends AbstractEntity {
    // LocalDate checkinDate;
    // LocalDate checkoutDate;
    // String reservationtest;
    double advancePayment;
    double remainingPayment;
    double soldReservation;
    int nmbreHomme;
    int nmbreFemme;
    int nmbrEnfant;

    @OneToMany
    @JoinColumn(name = "idResvationServiceAdditionnel")
    List<ServiceAdditionnelreservation> serviceAdditionnelreservations;

    @OneToMany
    @JoinColumn(name = "idResvationEncaissement")
    List<EncaissementPrincipal> encaisssementsreservation;

}
