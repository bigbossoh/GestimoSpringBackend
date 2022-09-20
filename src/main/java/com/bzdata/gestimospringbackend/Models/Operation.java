package com.bzdata.gestimospringbackend.Models;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "typeOperation", discriminatorType = DiscriminatorType.STRING)
public abstract class Operation extends AbstractEntity {
    LocalDate dateDebut;
    LocalDate dateFin;
    @ManyToOne
    Utilisateur utilisateurOperation;
    @ManyToOne
    Bienimmobilier bienImmobilierOperation;

}