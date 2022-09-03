package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.AbstractEntity;
import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.enumeration.EntiteOperation;
import com.bzdata.gestimospringbackend.enumeration.ModePaiement;
import com.bzdata.gestimospringbackend.enumeration.OperationType;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.time.LocalDate;

@Data
public class EncaissementPrincipalDTO {
    private Long id;
    private Long idAgence;
    private Instant creationDate;
    private Long idCreateur;
    private ModePaiement modePaiement;
    private OperationType operationType;
    private LocalDate dateEncaissement;
    private double montantEncaissement;
    private String intituleDepense;
    private EntiteOperation entiteOperation;
    private AppelLoyersFactureDto appelLoyersFactureDto;
}
