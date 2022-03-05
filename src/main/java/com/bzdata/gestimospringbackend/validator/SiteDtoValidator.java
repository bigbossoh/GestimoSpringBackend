package com.bzdata.gestimospringbackend.validator;
import com.bzdata.gestimospringbackend.DTOs.SiteRequestDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SiteDtoValidator {
    public static List<String> validate(SiteRequestDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("L'objet site ne doit pas etre null");
            errors.add("Veuillez renseigner un quartier");
            return errors;
        }
        if(dto.getQuartierDto()==null || dto.getQuartierDto().getId()==null){
            errors.add("Veuillez renseigner un quartier");
        }

        return errors;
    }
}
