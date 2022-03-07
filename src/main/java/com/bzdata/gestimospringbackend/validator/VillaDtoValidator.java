package com.bzdata.gestimospringbackend.validator;
import com.bzdata.gestimospringbackend.DTOs.VillaDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VillaDtoValidator {
    public static List<String> validate(VillaDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {

            errors.add("Veuillez renseigner le numero de la villa");
            errors.add("Veuillez renseignez l'Id de l'agence");
            return errors;
        }

        if(dto.getIdAgence()==null){
            errors.add("Veuillez renseignez l'Id de l'agence");
        }
        if(dto.getSiteRequestDto()==null || dto.getSiteRequestDto().getId()==null){
            errors.add("Veuillez selectionner une le site");
        }
        return errors;
    }

}

