package com.bzdata.gestimospringbackend.validator;

import java.util.ArrayList;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.BailVillaDto;

import org.springframework.util.StringUtils;

public class BailVillaDtoValidator {
    public static List<String> validate(BailVillaDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Veuillez renseigner le nom");
            errors.add("Veuillez renseigner l'abréviation ");
            errors.add("Veuillez renseigner le montant du bail");
            return errors;
        }
        if (!StringUtils.hasLength(dto.getAbrvCodeBail())) {
            errors.add("Veuillez renseigner l'abreviation");
        }
        if (!StringUtils.hasLength(dto.getDesignationBail())) {
            errors.add("Veuillez renseigner la désignation");
        }
        if (dto.getNouveauMontantLoyer()==0) {
            errors.add("Veuillez renseigner le montant du bail");
        }
        return errors;
    }
}