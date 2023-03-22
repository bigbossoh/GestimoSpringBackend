package com.bzdata.gestimospringbackend.Models.hotel;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

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
public class CategorieAppartement extends AbstractEntity{
    String nomCategorieAppart;
    String descCatAppart;
    @OneToMany(mappedBy = "categorieAppartement")
    List<Appartement> appartementCategorie;
    @OneToMany(mappedBy = "categorieAppartement")
    List<DetailContratReservation> detailContratReservationsCat;
    @OneToMany(mappedBy = "categorieAppartement")
    List<PrixParCategorie> prixParCategoriesCat;
}
