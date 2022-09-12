package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.enumeration.EntiteOperation;
import com.bzdata.gestimospringbackend.enumeration.ModePaiement;
import com.bzdata.gestimospringbackend.enumeration.OperationType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EncaissementPayloadDto {
   private Long idAgence;
  private  Long idCreateur;
    private Long idAppelLoyer;
    private LocalDate dateEncaissement;
    private ModePaiement modePaiement;
    private OperationType operationType;
    private double montantEncaissement;
    private String intituleDepense;
    private EntiteOperation entiteOperation;

}
