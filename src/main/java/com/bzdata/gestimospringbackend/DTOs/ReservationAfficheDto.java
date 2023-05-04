package com.bzdata.gestimospringbackend.DTOs;
import java.time.LocalDate;

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

public class ReservationAfficheDto {
    Long id;
    Long idAgence;
    Long idCreateur;

    Long idAppartementdDto;

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
    Long idBienImmobilier;
    long idLocataire;
    String codeAbrvBienImmobilier;
    double advancePayment;
    double remainingPayment;
    double soldReservation;
    int nmbreHomme;
    int nmbreFemme;
    int nmbrEnfant;
}
