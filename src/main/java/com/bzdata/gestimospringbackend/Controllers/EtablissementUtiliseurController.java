package com.bzdata.gestimospringbackend.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bzdata.gestimospringbackend.DTOs.EtablissementUtilisateurDto;
import com.bzdata.gestimospringbackend.Services.EtablissementUtilsateurService;
@RestController
@RequestMapping(APP_ROOT + "/etablissement")
@RequiredArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
@CrossOrigin(origins="*")
public class EtablissementUtiliseurController {
    final EtablissementUtilsateurService etablissementUtilsateurService;

    @GetMapping("/getDefaultEtable/{id}")
    public ResponseEntity<EtablissementUtilisateurDto> getDefaultEtable(@PathVariable("id")  Long id) {
        return ResponseEntity.ok(etablissementUtilsateurService.findDefaultChapitreUserByIdUser(id));
    }

}
