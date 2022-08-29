package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.*;

import com.bzdata.gestimospringbackend.Services.AppelLoyerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(APP_ROOT + "/appelloyer")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class AppelLoyersController {

    final AppelLoyerService appelLoyerService;

    @PostMapping("/save")
    @Operation(summary = "Creation et mise à jour d'un appel", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<String>> saveAppelLoyers(@RequestBody AppelLoyerRequestDto dto) {
        log.info("We are going to save a new appel {}", dto);
        return ResponseEntity.ok(appelLoyerService.save(dto));
    }

    @Operation(summary = "Trouver un Quartier par son ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAppelsByIdBail/{id}")
    public ResponseEntity<List<AppelLoyerDto>> listDesLoyersParBail(@PathVariable("id") Long id) {
        log.info("Find Appel by ID Bail {}", id);
        return ResponseEntity.ok(appelLoyerService.findAllAppelLoyerByBailId(id));
    }

    @Operation(summary = "Trouver un appel loyer par son ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAppelloyer/{id}")
    public ResponseEntity<AppelLoyersFactureDto> AppelLoyersParId(@PathVariable("id") Long id) {
        log.info("Find Appel by ID Bail {}", id);
        return ResponseEntity.ok(appelLoyerService.findById(id));
    }

    @Operation(summary = "Trouver tous les appels loyers par periode", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAllAppelloyerByPeriode/{periode}")
    public ResponseEntity<List<AppelLoyersFactureDto>> AppelLoyersParPeriode(@PathVariable("periode") String periode) {
        log.info("Find Appel by periode {}", periode);
        return ResponseEntity.ok(appelLoyerService.findAllAppelLoyerByPeriode(periode));
    }
    @Operation(summary = "Trouver tous les appels loyers par annee", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAllPeriodeByAnnee/{annee}")
    public ResponseEntity<List<String>> findAllPeriodeByAnnee(@PathVariable("annee") Integer annee) {
        log.info("Find Appelperiode by annee {}", annee);
        return ResponseEntity.ok(appelLoyerService.listOfPerodesByAnnee(annee));
    }
    @Operation(summary = "Trouver toutes les années appels loyers ", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/listOfDistinctAnneeAppel")
    public ResponseEntity<List<Integer>> listOfDistinctAnneeAppel() {
        log.info("Find liste des annees Appel periode.");
        return ResponseEntity.ok(appelLoyerService.listOfDistinctAnnee());
    }

    @Operation(summary = "Lister tous les appels de BD", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAllAppelsLoyer")
    public ResponseEntity<List<AppelLoyersFactureDto>> listTousAppelsLoyers() {
        log.info("Liste de tous les appels ");
        return ResponseEntity.ok(appelLoyerService.findAll());
    }

}
