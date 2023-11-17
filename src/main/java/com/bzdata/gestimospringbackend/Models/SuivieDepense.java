package com.bzdata.gestimospringbackend.Models;

import com.bzdata.gestimospringbackend.enumeration.ModePaiement;
import com.bzdata.gestimospringbackend.enumeration.OperationType;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SuivieDepense extends AbstractEntity {

  private LocalDate dateEncaissement;
  private String designation;
  private String codeTransaction;
  private double montantDepense;
  String cloturerSuivi;

  @Enumerated(EnumType.STRING)
  private ModePaiement modePaiement;

  @Enumerated(EnumType.STRING)
  private OperationType operationType;
  @ManyToOne
  private Chapitre chapitreSuivis;
}
