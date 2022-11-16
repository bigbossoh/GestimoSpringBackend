package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AgenceRequestDto {
     Long id;
     Long idAgence;
     Long idCreateur;
    String nomAgence;
    String telAgence;
    String compteContribuable;
    double capital;
    String emailAgence;
    String mobileAgence;
    String regimeFiscaleAgence;
    String faxAgence;
    String sigleAgence;
    Long idUtilisateurCreateur;
    String motdepasse;
    String nomPrenomGerant;
    String adresseAgence;
    private Long idImage;
    private String nameImage;
    private String typeImage;
    private String profileAgenceUrl;
    private boolean active;
    String logoAgence;





}