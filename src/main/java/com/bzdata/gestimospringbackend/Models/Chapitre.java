package com.bzdata.gestimospringbackend.Models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

public class Chapitre  {
    @Id
    @GeneratedValue
    Long id;
    String libelleChapitre;
    @OneToMany(mappedBy = "chapitre")
    List<Bienimmobilier> biens;
     @OneToMany(mappedBy = "chapitreSuivis")
    List<SuivieDepense> suivisDepenseChapitre;
}
