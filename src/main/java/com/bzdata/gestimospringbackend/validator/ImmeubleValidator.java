package com.bzdata.gestimospringbackend.validator;

import java.util.ArrayList;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.ImmeubleDto;

import org.springframework.util.StringUtils;

public class ImmeubleValidator {
    public static List<String> validate(ImmeubleDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Aucune données à enregistrer.");
            return errors;
        }
        if (!StringUtils.hasLength(dto.getAbrvNomImmeuble())) {
            errors.add("Veuillez renseigner l'abréviation.");
        }
        if (!StringUtils.hasLength(dto.getStatutBien())) {
            errors.add("Veuillez renseigner le Statut.");
        }
        if (!StringUtils.hasLength(dto.getEtatBien())) {
            errors.add("Veuillez renseigner l'état.");
        }
        return errors;
    }
}
