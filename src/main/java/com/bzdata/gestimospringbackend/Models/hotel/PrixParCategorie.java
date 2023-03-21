package com.bzdata.gestimospringbackend.Models.hotel;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
public class PrixParCategorie extends AbstractEntity{
    String nombreJour;
    double prix;
    int intervalle;
    String descriptionPrixCategorie;
    int nbrDiffJour;
    @ManyToOne
    CategorieAppartement categorieAppartement;

}
