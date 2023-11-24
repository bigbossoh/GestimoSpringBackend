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

  
   Long idUtilisateur;
   String nom;
   String prenom;


   double pourcentageReduction;
   double montantReduction;
   double soldReservation;
   double montantPaye;
   int nmbreAdulte;
   
   int nmbrEnfant;
}
