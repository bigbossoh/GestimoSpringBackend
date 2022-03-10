package com.bzdata.gestimospringbackend.validator;

import com.bzdata.gestimospringbackend.DTOs.MontantLoyerBailDto;
import com.bzdata.gestimospringbackend.DTOs.PaysDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MontantLoyerBailDtoValidator {
    public static List<String> validate(MontantLoyerBailDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Veuillez renseigner le montant du loyer");
            errors.add("Veuillez renseigner la date d'affection du prix du loyer");
            return errors;
        }
        if (dto.getMontantLoyer() ==Double.parseDouble(null)) {
            errors.add("Veuillez renseigner le montant du loyer");
        }
        if (dto.getDebutLoyer()==null) {
            errors.add("Veuillez renseigner la date d'affection du prix du loyer");
        }
        if (dto.getBailLocation()==null || dto.getBailLocation().getId()==null) {
            errors.add("Veuillez renseigner le bail de location.");
        }
        return errors;
    }
}
