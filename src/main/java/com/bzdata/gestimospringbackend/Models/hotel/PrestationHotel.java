package com.bzdata.gestimospringbackend.Models.hotel;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.bzdata.gestimospringbackend.Models.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PrestationHotel extends AbstractEntity {
    String designPrestation;
    double prixPrestation;
    @OneToMany(mappedBy = "prestation")
    List<EncaissementAccessoire> encaissementAccessoiresPresta;
    @OneToMany(mappedBy = "prestationHotel")
    List<ServicesHotel> prestationServices;
}
