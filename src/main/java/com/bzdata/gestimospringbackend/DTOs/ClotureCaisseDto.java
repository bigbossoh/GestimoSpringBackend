package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClotureCaisseDto {

  Long id;
  Long idAgence;
  Long idCreateur;

  String entiteCloturer;
  double montantEncaissement;
  double soldeEncaissement;
  double encienSoldReservation;
  double nvoSoldeReservation;
  double totalEncaisse;
  String clientCloture;
  String statutCloture;
}
