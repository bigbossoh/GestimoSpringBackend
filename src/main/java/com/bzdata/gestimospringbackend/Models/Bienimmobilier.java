package com.bzdata.gestimospringbackend.Models;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "typeBienImmobilier", discriminatorType = DiscriminatorType.STRING)
public abstract class Bienimmobilier extends AbstractEntity {

    String codeAbrvBienImmobilier;
    String nomCompletBienImmobilier;
    String nomBaptiserBienImmobilier;
    String description;
    double superficieBien;
    boolean bienMeublerResidence;
    boolean isOccupied= false;
    @ManyToOne
    Utilisateur utilisateurProprietaire;
    int nombrePieceBien;

}
