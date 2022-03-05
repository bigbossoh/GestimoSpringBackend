package com.bzdata.gestimospringbackend.validator;

import java.util.ArrayList;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.QuartierDto;

import org.springframework.util.StringUtils;

public class QuartierValidator {
    public static List<String> validate(QuartierDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Veuillez renseigner le nom du Quartier");
            errors.add("Veuillez renseigner l'abréviation du Quartier");
            return errors;
        }
        if (!StringUtils.hasLength(dto.getNomQuartier())) {
            errors.add("Veuillez renseigner le nom du Quartier");
        }
        if (!StringUtils.hasLength(dto.getAbrvQuartier())) {
            errors.add("Veuillez renseigner l'abréviation du Quartier");
        }
        return errors;
    }
}
