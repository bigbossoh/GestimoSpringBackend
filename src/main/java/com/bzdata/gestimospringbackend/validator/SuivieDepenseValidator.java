package com.bzdata.gestimospringbackend.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseDto;
import com.bzdata.gestimospringbackend.Models.SuivieDepense;

public class SuivieDepenseValidator {
   public static List<String> validate(SuivieDepenseDto dto) {
      List<String> errors = new ArrayList<>();

      if (dto == null) {
         errors.add("Veuillez renseigner la raison de la depense.");
          return errors;
      }

     if(!StringUtils.hasLength(dto.getDesignation())){
      errors.add("Veuillez renseigner la raison de la depense.");
     }
      return errors;
  }
   
   
}
