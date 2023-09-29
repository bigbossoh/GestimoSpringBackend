package com.bzdata.gestimospringbackend.DTOs;

import java.time.LocalDate;
import javax.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationRequestDto {

  Long id;
  Long idAgence;
  Long idCreateur;

  Long idAppartementdDto;

  LocalDate dateDebut;
  LocalDate dateFin;
  Long idClient;
  Long idBien;

   String utilisateurIdApp;
   Long idUtilisateur;
   String nom;
   String prenom;
   String email;
   String username;

  @Column(unique = true)
   String mobile;

   LocalDate dateDeNaissance;
   String lieuNaissance;
   String typePieceIdentite;
   String numeroPieceIdentite;
   LocalDate dateDebutPiece;
   LocalDate dateFinPiece;
   String nationalite;
   String genre;

  double montantCautionBail;
  int nbreMoisCautionBail;
  double nouveauMontantLoyer;
  Long idBienImmobilier;

  double advancePayment;
  double remainingPayment;
  double soldReservation;
  int nmbreHomme;
  int nmbreFemme;
  int nmbrEnfant;
}
