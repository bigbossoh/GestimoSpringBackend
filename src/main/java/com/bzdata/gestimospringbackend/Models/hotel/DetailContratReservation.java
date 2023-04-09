package com.bzdata.gestimospringbackend.Models.hotel;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.bzdata.gestimospringbackend.Models.AbstractEntity;
import com.bzdata.gestimospringbackend.Models.Appartement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DetailContratReservation extends AbstractEntity {
    LocalDate dateDetailContrat;
    double prixUnitaireDetailContrat;
    double montantSejourDetailContrat;
    String deatilParChambre;
    @ManyToOne
    Appartement appartementDetail;
    @ManyToOne
    ContratReservation contratReservation;
    @ManyToOne
    CategorieAppartement categorieAppartement;
}
