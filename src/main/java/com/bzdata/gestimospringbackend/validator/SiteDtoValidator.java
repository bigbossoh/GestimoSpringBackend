package com.bzdata.gestimospringbackend.validator;
import com.bzdata.gestimospringbackend.DTOs.SiteDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SiteDtoValidator {
    public static List<String> validate(SiteDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Veuillez renseigner le nom du Pays");
            return errors;
        }
        if (!StringUtils.hasLength(dto.getNomSite())) {
            errors.add("Veuillez renseigner le nom du site");
        }

        return errors;
    }
}
