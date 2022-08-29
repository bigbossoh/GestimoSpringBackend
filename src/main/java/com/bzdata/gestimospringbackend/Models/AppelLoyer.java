package com.bzdata.gestimospringbackend.Models;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
public class AppelLoyer extends AbstractEntity {
    String periodeAppelLoyer;
    String periodeLettre;
    String statusAppelLoyer;
    LocalDate datePaiementPrevuAppelLoyer;
    LocalDate dateDebutMoisAppelLoyer;
    LocalDate dateFinMoisAppelLoyer;
    int anneeAppelLoyer;
    int moisChiffreAppelLoyer;
    String moisUniquementLettre;
    String descAppelLoyer;
    double montantBailLPeriode;
    double soldeAppelLoyer;
    boolean isSolderAppelLoyer;

    @ManyToOne
    BailLocation bailLocationAppelLoyer;
    @OneToMany(mappedBy = "appelLoyerEncaissement")
    List<Encaissement> encaissementsAppelLoyer;

}
