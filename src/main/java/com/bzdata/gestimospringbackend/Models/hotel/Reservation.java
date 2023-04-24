package com.bzdata.gestimospringbackend.Models.hotel;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.bzdata.gestimospringbackend.Models.AbstractEntity;

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
public class Reservation extends AbstractEntity{
    LocalDate checkinDate;
    LocalDate checkoutDate;
    String reservationtest;
    double advancePayment;
    double remainingPayment;
    @OneToMany( mappedBy = "reservation")
    List<Facture> factureReservations;
}
