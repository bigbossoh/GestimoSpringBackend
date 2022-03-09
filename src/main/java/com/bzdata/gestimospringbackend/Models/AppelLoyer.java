package com.bzdata.gestimospringbackend.Models;

import java.util.Date;

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
public class AppelLoyer extends AbstractEntity {
    String periodeAppelLoyer;
    String statusAppelLoyer;
    Date datePaiementPrevuAppelLoyer;
    Date dateDebutMoisAppelLoyer;
    Date dateFinMoisAppelLoyer;
    int anneeAppelLoyer;
    int moisChiffreAppelLoyer;
    String descAppelLoyer;

    @ManyToOne
    BailLocation bailLocationAppelLoyer;

}
