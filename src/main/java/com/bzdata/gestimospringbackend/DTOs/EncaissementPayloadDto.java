package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.enumeration.EntiteOperation;
import com.bzdata.gestimospringbackend.enumeration.ModePaiement;
import com.bzdata.gestimospringbackend.enumeration.OperationType;
import lombok.Data;

@Data
public class EncaissementPayloadDto {
    private Long idAppelLoyer;
    private ModePaiement modePaiement;
    private OperationType operationType;
    private double montantEncaissement;
    private String intituleDepense;
    private EntiteOperation entiteOperation;

}