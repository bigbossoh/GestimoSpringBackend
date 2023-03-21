package com.bzdata.gestimospringbackend.Models.hotel;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.bzdata.gestimospringbackend.Models.AbstractEntity;
import com.bzdata.gestimospringbackend.Models.Etage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Chambre extends AbstractEntity {
    String telChambre;
    String descChambre;
    boolean occuper;
    @ManyToOne
    Etage etage;
    @ManyToOne
    CategorieAppartement categorieAppartement;
    @OneToMany(mappedBy = "chambreDetail")
    List<DetailContratReservation> detailContratReservationsChambre;
}
