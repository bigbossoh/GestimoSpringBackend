package com.bzdata.gestimospringbackend.validator;

import java.util.ArrayList;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.AppartementDto;

import org.springframework.util.StringUtils;

public class AppartementDtoValidator {
    public static List<String> validate(AppartementDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Aucune données à enregistrer.");
            errors.add("Veuillez selectionner un etage");
            errors.add("Veuillez renseigner le nom.");
            errors.add("Veuillez renseigner l'abréviation.");
            return errors;
        }
        if (!StringUtils.hasLength(dto.getAbrvNomApp())) {
            errors.add("Veuillez renseigner l'abréviation.");
        }
        if (!StringUtils.hasLength(dto.getNomApp())) {
            errors.add("Veuillez renseigner le nom.");
        }
        if (dto.getIdEtage() == null) {
            errors.add("Veuillez selectionner un etage");
        }

        return errors;
    }
}
