package com.bzdata.gestimospringbackend.validator;

import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;
import com.bzdata.gestimospringbackend.DTOs.EspeceEncaissementDto;

import java.util.ArrayList;
import java.util.List;

public class EncaissementPrincipalDTOValidor {
    public static List<String> validate(EncaissementPrincipalDTO dto) {
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Aucun encaissement à enregistrer");
            // return errors;
        }
        return errors;
    }
}

