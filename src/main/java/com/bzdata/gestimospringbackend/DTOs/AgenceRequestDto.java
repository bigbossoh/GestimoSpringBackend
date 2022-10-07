package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
//@Builder
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




//    public static AgenceRequestDto fromEntity(AgenceImmobiliere agenceImmobiliere) {
//        if (agenceImmobiliere == null) {
//            return null;
//        }
//        return AgenceRequestDto.builder()
//                .id(agenceImmobiliere.getId())
//                .nomAgence(agenceImmobiliere.getNomAgence())
//                .telAgence(agenceImmobiliere.getTelAgence())
//                .mobileAgence(agenceImmobiliere.getMobileAgence())
//                .compteContribuable(agenceImmobiliere.getCompteContribuable())
//                .capital(agenceImmobiliere.getCapital())
//                .emailAgence(agenceImmobiliere.getEmailAgence())
//                .regimeFiscaleAgence(agenceImmobiliere.getRegimeFiscaleAgence())
//                .faxAgence(agenceImmobiliere.getFaxAgence())
//                .sigleAgence(agenceImmobiliere.getSigleAgence())
//                .idAgence(agenceImmobiliere.getIdAgence())
//                .build();
//    }


}