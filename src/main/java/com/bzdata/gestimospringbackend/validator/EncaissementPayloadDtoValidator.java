package com.bzdata.gestimospringbackend.validator;

import com.bzdata.gestimospringbackend.DTOs.EncaissementPayloadDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EncaissementPayloadDtoValidator {
    public static List<String> validate(EncaissementPayloadDto dto) {
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Aucun encaissement à enregistrer");
        }
        if (dto.getIdAppelLoyer()== null) {
            errors.add("Veuillez selectionner l'appel loyer");
        }
        if (dto.getMontantEncaissement()== 0) {
            errors.add("Veuillez enreigner le montant du moler payé");
        }

        return errors;
    }
}
