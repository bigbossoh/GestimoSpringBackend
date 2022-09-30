package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
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
    boolean active;



    public static AgenceRequestDto fromEntity(AgenceImmobiliere agenceImmobiliere) {
        if (agenceImmobiliere == null) {
            return null;
        }
        return AgenceRequestDto.builder()
                .id(agenceImmobiliere.getId())
                .nomAgence(agenceImmobiliere.getNomAgence())
                .telAgence(agenceImmobiliere.getTelAgence())
                .mobileAgence(agenceImmobiliere.getMobileAgence())
                .compteContribuable(agenceImmobiliere.getCompteContribuable())
                .capital(agenceImmobiliere.getCapital())
                .emailAgence(agenceImmobiliere.getEmailAgence())
                .regimeFiscaleAgence(agenceImmobiliere.getRegimeFiscaleAgence())
                .faxAgence(agenceImmobiliere.getFaxAgence())
                .sigleAgence(agenceImmobiliere.getSigleAgence())
                // .idUtilisateurCreateur(agenceImmobiliere.getCreateur().getId())
                .idAgence(agenceImmobiliere.getIdAgence())
                .build();
    }

//    public static AgenceImmobiliere toEntity(AgenceRequestDto dto) {
//        if (dto == null) {
//            return null;
//        }
//        AgenceImmobiliere newAgenceImmobiliere = new AgenceImmobiliere();
//        newAgenceImmobiliere.setId(dto.getId());
//        newAgenceImmobiliere.setIdAgence(dto.getId());
//        newAgenceImmobiliere.setNomAgence(dto.getNomAgence());
//        newAgenceImmobiliere.setMobileAgence(dto.getMobileAgence());
//        newAgenceImmobiliere.setFaxAgence(dto.getFaxAgence());
//        newAgenceImmobiliere.setEmailAgence(dto.getEmailAgence());
//        newAgenceImmobiliere.setCapital(dto.getCapital());
//        newAgenceImmobiliere.setRegimeFiscaleAgence(dto.getRegimeFiscaleAgence());
//        newAgenceImmobiliere.setCompteContribuable(dto.getCompteContribuable());
//        newAgenceImmobiliere.setSigleAgence(dto.getSigleAgence());
//        newAgenceImmobiliere.setTelAgence(dto.getTelAgence());
//        newAgenceImmobiliere.setCreateur(UtilisateurRequestDto.toEntity(dto.getUtilisateurCreateur()));
//        return newAgenceImmobiliere;
//    }
}