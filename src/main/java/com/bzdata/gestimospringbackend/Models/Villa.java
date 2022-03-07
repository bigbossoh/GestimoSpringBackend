
package com.bzdata.gestimospringbackend.Models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

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
@DiscriminatorValue("Villa")
public class Villa  extends Bienimmobilier {
    int nbrChambreVilla;
    int nbrePiece;
    int nbrSalonVilla;
    int nbrSalleEauVilla;
    String nomVilla;
    String abrvVilla;
    boolean garageVilla= false;
    int nbreVoitureGarageVilla;
}
