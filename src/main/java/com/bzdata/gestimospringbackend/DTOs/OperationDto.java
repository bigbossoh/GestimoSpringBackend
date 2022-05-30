package com.bzdata.gestimospringbackend.DTOs;

import java.time.Instant;
import java.time.LocalDate;

import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.Operation;

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

    String designationBail;
    String abrvCodeBail;
    boolean enCoursBail;
    boolean archiveBail;
    double montantCautionBail;
    int nbreMoisCautionBail;
    double nouveauMontantLoyer;

    public static OperationDto fromEntity(BailLocation operation) {
        if (operation == null) {

        }
        return OperationDto.builder()
                .id(operation.getId())
                .idAgence(operation.getIdAgence())
                .enCoursBail(operation.isEnCoursBail())
                .abrvCodeBail(operation.getAbrvCodeBail())
                .archiveBail(operation.isArchiveBail())
                .bienImmobilierOperation(operation.getBienImmobilierOperation().getAbrvBienimmobilier())
                .creationDate(operation.getCreationDate())
                .designationBail(operation.getDesignationBail())
                .montantCautionBail(operation.getMontantCautionBail())
                .nbreMoisCautionBail(operation.getNbreMoisCautionBail())
                .utilisateurOperation(operation.getUtilisateurOperation().getNom() + " "
                        + operation.getUtilisateurOperation().getPrenom())
                .dateDebut(operation.getDateDebut())
                .dateFin(operation.getDateFin())
                .lastModifiedDate(operation.getLastModifiedDate())

                .build();
    }
}
