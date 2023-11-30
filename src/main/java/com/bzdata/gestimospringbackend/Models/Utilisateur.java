package com.bzdata.gestimospringbackend.Models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
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
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "id"
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Utilisateur extends AbstractEntity {

  private String utilisateurIdApp;
  private String nom;
  private String prenom;
  private String email;

  @Column(unique = true)
  private String mobile;

  private LocalDate dateDeNaissance;
  private String lieuNaissance;
  private String typePieceIdentite;
  private String numeroPieceIdentite;
  private LocalDate dateDebutPiece;
  private LocalDate dateFinPiece;
  private String nationalite;
  private String genre;
  private boolean isActivated;

  @Column(unique = true)
  private String username;

  private String password;

  private String profileImageUrl;
  private Date lastLoginDate;
  private Date lastLoginDateDisplay;
  private Date joinDate;
  private String roleUsed;
  private String[] authorities;
  private boolean isActive;
  private boolean isNonLocked;

  // @ManyToOne
  // AgenceImmobiliere agenceImmobilier;

  // @OneToMany(mappedBy = "createur")
  // List<AgenceImmobiliere> createurAgenceImmobiliere;

  @ManyToOne(fetch = FetchType.EAGER)
  private Role urole;

  // @ManyToOne
  // Utilisateur userCreate;
  @OneToMany(mappedBy = "utilisateurOperation")
  private List<Operation> operationUser;

  @OneToMany(mappedBy = "utilisateurEncaissement")
  private List<Encaissement> encaissementsUtilisateur;

  @OneToMany(mappedBy = "utilisateurProprietaire")
  private List<Bienimmobilier> biensUtilisateur;

//   @OneToMany(mappedBy = "utilisateurChapitre")
//   @JsonManagedReference
//   private List<EtablissementUtilisateur> utilisateurChap;
}
