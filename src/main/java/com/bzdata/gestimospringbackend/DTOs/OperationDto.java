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
    String utilisateurOperation;
    String bienImmobilierOperation;
    Long studioBail;
    // String appartementBail;
    // String magasinBail;
    // String villaBail;

    public static OperationDto fromEntity(Operation operation) {
        if (operation == null) {

        }
        return OperationDto.builder()
                .id(operation.getId())
                .idAgence(operation.getIdAgence())
                // .appartementBail(operation.getAppartementBail().getAbrvNomApp())
                // .bienImmobilierOperation(operation.getBienImmobilierOperation().getAbrvBienimmobilier())
                .creationDate(operation.getCreationDate())
                .dateDebut(operation.getDateDebut())
                .dateFin(operation.getDateFin())
                .lastModifiedDate(operation.getLastModifiedDate())
                // .magasinBail(operation.getMagasinBail().getAbrvNomMagasin())
                .studioBail(operation.getStudioBail().getId())
                .utilisateurOperation(operation.getUtilisateurOperation().getNom() + " " +
                        operation
                                .getUtilisateurOperation()
                                .getPrenom())
                // .villaBail(operation.getVillaBail().getAbrvVilla())
                .build();
    }
}
