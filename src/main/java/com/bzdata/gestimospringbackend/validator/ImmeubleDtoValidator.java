package com.bzdata.gestimospringbackend.validator;

import java.util.ArrayList;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.ImmeubleDto;

import org.springframework.util.StringUtils;

public class ImmeubleDtoValidator {
    public static List<String> validate(ImmeubleDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
//            errors.add("Veuillez renseigner l'abréviation.");
//            errors.add("Veuillez renseigner le Statut.");
//            errors.add("Veuillez renseigner l'état.");
            errors.add("Veuillez selectionner un utilisateur");
            errors.add("Veuillez selectionner une le site");
            return errors;
        }
//        if (!StringUtils.hasLength(dto.getAbrvNomImmeuble())) {
//            errors.add("Veuillez renseigner l'abréviation.");
//        }
//        if (!StringUtils.hasLength(dto.getStatutBien())) {
//            errors.add("Veuillez renseigner le Statut.");
//        }
//        if (!StringUtils.hasLength(dto.getEtatBien())) {
//            errors.add("Veuillez renseigner l'état.");
//        }
        if(dto.getSiteRequestDto()==null || dto.getSiteRequestDto().getId()==null){
            errors.add("Veuillez selectionner le site de l'immeuble");
        }
        if(dto.getUtilisateurRequestDto()==null || dto.getUtilisateurRequestDto().getId()==null){
            errors.add("Veuillez selectionner un utilisateur");
        }
        return errors;
    }
}
