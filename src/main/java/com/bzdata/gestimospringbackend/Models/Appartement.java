package com.bzdata.gestimospringbackend.Models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
public class Appartement extends AbstractEntity {
    boolean meubleApp;
    int nbrPieceApp;
    int nbreChambreApp;
    int nbreSalonApp;
    int nbreSalleEauApp;
    boolean isResidence;
    int numeroApp;
    String abrvNomApp;
    String nomApp;
    @ManyToOne
    Etage etageAppartement;
}
