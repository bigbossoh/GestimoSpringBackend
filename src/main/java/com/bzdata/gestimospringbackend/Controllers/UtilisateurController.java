package com.bzdata.gestimospringbackend.Controllers;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Services.UtilisateurService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bzdata.gestimospringbackend.Utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT+"/utilisateur")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "gestimoapi")
public class UtilisateurController {
    private final UtilisateurService utilisateurService;
    @PostMapping("/locataire/save")
    public ResponseEntity<UtilisateurRequestDto> authenticate(@RequestBody UtilisateurRequestDto request) {
        log.info("We are going to save a new locatire {}",request);
        return ResponseEntity.ok(utilisateurService.saveLocataire(request));
    }
}
