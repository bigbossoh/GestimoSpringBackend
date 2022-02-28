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
    private Long id;
    String nomAgence;
    String telAgence;
    String compteContribuable;
    double capital;
    String emailAgence;
    String regimeFiscaleAgence;
    String faxAgence;
    String sigleAgence;
    UtilisateurRequestDto utilisateurRequestDto;

    public static AgenceRequestDto fromEntity(AgenceImmobiliere agenceImmobiliere) {
        if (agenceImmobiliere == null) {
            return null;
        }
        return AgenceRequestDto.builder()
                .id(agenceImmobiliere.getId())
                .nomAgence(agenceImmobiliere.getNomAgence())
                .telAgence(agenceImmobiliere.getTelAgence())
                .compteContribuable(agenceImmobiliere.getCompteContribuable())
                .capital(agenceImmobiliere.getCapital())
                .emailAgence(agenceImmobiliere.getEmailAgence())
                .regimeFiscaleAgence(agenceImmobiliere.getRegimeFiscaleAgence())
                .faxAgence(agenceImmobiliere.getFaxAgence())
                .sigleAgence(agenceImmobiliere.getSigleAgence())
                .utilisateurRequestDto(UtilisateurRequestDto.fromEntity(agenceImmobiliere.getCreateur()))
                .build();
    }
    public static AgenceImmobiliere toEntity(AgenceRequestDto dto){
        if(dto==null){
            return null;
        }
        AgenceImmobiliere newAgenceImmobiliere=new AgenceImmobiliere();
        newAgenceImmobiliere.setId(dto.getId());
        newAgenceImmobiliere.setNomAgence(dto.getNomAgence());
        newAgenceImmobiliere.setFaxAgence(dto.getFaxAgence());
        newAgenceImmobiliere.setEmailAgence(dto.getEmailAgence());
        newAgenceImmobiliere.setCapital(dto.getCapital());
        newAgenceImmobiliere.setRegimeFiscaleAgence(dto.getRegimeFiscaleAgence());
        newAgenceImmobiliere.setCompteContribuable(dto.getCompteContribuable());
        newAgenceImmobiliere.setSigleAgence(dto.getSigleAgence());
        newAgenceImmobiliere.setTelAgence(dto.getTelAgence());
        newAgenceImmobiliere.setCreateur(UtilisateurRequestDto.toEntity(dto.getUtilisateurRequestDto()));
        return newAgenceImmobiliere;
    }
}