package com.bzdata.gestimospringbackend.DTOs;

import java.time.LocalDate;

import com.bzdata.gestimospringbackend.enumeration.ModePaiement;
import com.bzdata.gestimospringbackend.enumeration.OperationType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuivieDepenseDto {
   Long id;
   Long idAgence;
   Long idCreateur;
   LocalDate dateEncaissement;
   String designation;
   String codeTransaction;
   double montantDepense;
   ModePaiement modePaiement;  
   OperationType operationType;
   String cloturerSuivi;
   Long idChapitre;

}
