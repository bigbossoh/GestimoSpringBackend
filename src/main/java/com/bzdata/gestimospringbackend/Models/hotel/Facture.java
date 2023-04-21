package com.bzdata.gestimospringbackend.Models.hotel;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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

public class Facture extends AbstractEntity {
    double totalAmount;
    boolean paymentStatus;
    @ManyToOne
    Reservation reservation;

}
