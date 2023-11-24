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
public class ReservationSaveOrUpdateDto {

    Long id;
    Long idAgence;
    Long idCreateur;

    UtilisateurRequestDto utilisateurRequestDto;
    Long idAppartementdDto;
Long idUtilisateur;
    LocalDate dateDebut;
    LocalDate dateFin;
   // UtilisateurAfficheDto utilisateurOperation;
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
    public void setUtilisateurRequestDto(String string) {
    }
}
