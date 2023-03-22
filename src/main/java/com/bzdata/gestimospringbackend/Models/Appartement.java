package com.bzdata.gestimospringbackend.Models;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.bzdata.gestimospringbackend.Models.hotel.CategorieAppartement;
import com.bzdata.gestimospringbackend.Models.hotel.DetailContratReservation;

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
    Long numApp;
//    @OneToMany(mappedBy = "appartementBail")
//    List<Operation> operationsApp;
    @ManyToOne(fetch = FetchType.LAZY)
    Etage etageAppartement;
    @ManyToOne
    CategorieAppartement categorieAppartement;
    @OneToMany(mappedBy = "appartementDetail")
    List<DetailContratReservation> detailContratReservationsChambre;
}
