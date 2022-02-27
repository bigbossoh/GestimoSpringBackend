package com.bzdata.gestimospringbackend.Models;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Utilisateur extends AbstractEntity {
    private String nom;
    private String prenom;
    private String email;
    private String mobile;
    private LocalDate dateDeNaissance;
    private String lieuNaissance;
    private String typePieceIdentite;
    private String numeroPieceIdentite;
    private LocalDate dateDebutPiece;
    private LocalDate dateFinPiece;
    private String nationalité;
    private String genre;
    private boolean isActivated;
    private String username;
    private String password;



    @ManyToOne
    AgenceImmobiliere agence;

    @OneToMany(mappedBy = "createur")
    List<AgenceImmobiliere> listeAgence;

    @ManyToOne(fetch = FetchType.EAGER)
    Role urole;
    @ManyToOne
    Utilisateur userCreate;
}
