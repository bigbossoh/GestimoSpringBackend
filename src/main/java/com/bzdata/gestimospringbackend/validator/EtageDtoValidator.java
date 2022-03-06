package com.bzdata.gestimospringbackend.validator;

import java.util.ArrayList;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.EtageDto;

import org.springframework.util.StringUtils;

public class EtageDtoValidator {
    public static List<String> validate(EtageDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("VCertain champs sont vide");

            return errors;
        }
        if (!StringUtils.hasLength(dto.getAbrvEtage())) {
            errors.add("Veuillez renseigner l'abreviation");
        }
        if (!StringUtils.hasLength(dto.getNomEtage())) {
            errors.add("Veuillez renseigner le nom");
        }
        return errors;
    }

}
