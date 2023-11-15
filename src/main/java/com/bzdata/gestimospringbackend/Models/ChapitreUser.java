package com.bzdata.gestimospringbackend.Models;

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

public class ChapitreUser extends AbstractEntity {
    boolean chapitreDefault;
    @ManyToOne
    Chapitre chapitre;
    @ManyToOne
    Utilisateur utilisateurChapitre;
}
