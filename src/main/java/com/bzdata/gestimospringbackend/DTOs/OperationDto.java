package com.bzdata.gestimospringbackend.DTOs;

import java.time.Instant;
import java.time.LocalDate;

import com.bzdata.gestimospringbackend.Models.Appartement;
import com.bzdata.gestimospringbackend.Models.Bienimmobilier;
import com.bzdata.gestimospringbackend.Models.Magasin;
import com.bzdata.gestimospringbackend.Models.Operation;
import com.bzdata.gestimospringbackend.Models.Studio;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Models.Villa;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperationDto {

    Long id;
    private Long idAgence;
    Instant creationDate;
    Instant lastModifiedDate;
    LocalDate dateDebut;
    LocalDate dateFin;
    Utilisateur utilisateurOperation;
    Bienimmobilier bienImmobilierOperation;
    Studio studioBail;
    Appartement appartementBail;
    Magasin magasinBail;
    Villa villaBail;

    public static OperationDto fromEntity(Operation operation) {
        if (operation == null) {

        }
        return OperationDto.builder()
                .id(operation.getId())
                .idAgence(operation.getIdAgence())
                .appartementBail(operation.getAppartementBail())
                .bienImmobilierOperation(operation.getBienImmobilierOperation())
                .creationDate(operation.getCreationDate())
                .dateDebut(operation.getDateDebut())
                .dateFin(operation.getDateFin())
                .lastModifiedDate(operation.getLastModifiedDate())
                .magasinBail(operation.getMagasinBail())
                .studioBail(operation.getStudioBail())
                .utilisateurOperation(operation.getUtilisateurOperation())
                .villaBail(operation.getVillaBail())
                .build();
    }
}
