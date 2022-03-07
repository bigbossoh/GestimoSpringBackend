package com.bzdata.gestimospringbackend.validator;

import java.util.ArrayList;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.StudioDto;

import org.springframework.util.StringUtils;

public class StudioDtoValidator {
    public static List<String> validate(StudioDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Aucune données à enregistrer.");
            return errors;
        }
        if (!StringUtils.hasLength(dto.getAbrvNomStudio())) {
            errors.add("Veuillez renseigner l'abréviation.");
        }
        if (!StringUtils.hasLength(dto.getNomStudio())) {
            errors.add("Veuillez renseigner le Nom.");
        }

        return errors;
    }
}
