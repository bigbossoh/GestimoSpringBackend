package com.bzdata.gestimospringbackend.Models;

import javax.persistence.DiscriminatorValue;
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
@DiscriminatorValue("Magasin")
public class Magasin extends Bienimmobilier {
    boolean isUnderBuilding;
    @ManyToOne
    Etage etageMagasin;

}
