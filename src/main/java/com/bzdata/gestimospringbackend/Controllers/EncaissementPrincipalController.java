package com.bzdata.gestimospringbackend.Controllers;

import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;
import com.bzdata.gestimospringbackend.DTOs.CommuneRequestDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPayloadDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;
import com.bzdata.gestimospringbackend.Services.CommuneService;
import com.bzdata.gestimospringbackend.Services.EncaissementPrincipalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "/encaissement")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class EncaissementPrincipalController {

    final EncaissementPrincipalService encaissementPrincipalService;


    @PostMapping("/saveencaissement")
    @Operation(summary = "Creation et encaissement", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Boolean> saveCommune(@RequestBody EncaissementPayloadDto dto) {
        //log.info("We are going to save a new encaissement {}", dto);
        return ResponseEntity.ok(encaissementPrincipalService.saveEncaissement(dto));
    }
    @Operation(summary = "Listés tous les envaissements de lae BD", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAllEncaissementPrincipal")
    public ResponseEntity<List<EncaissementPrincipalDTO>> listTousEncaissementsPrincipal() {
        log.info("Liste de tous les envaissements");
        return ResponseEntity.ok(encaissementPrincipalService.findAllEncaissement());
    }
    @Operation(summary = "Total des encaissements par Id d'appel de loyer", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/totalencaissement/{id}")
    public ResponseEntity<Double> totalencaissementParIdAppelLoyer(@PathVariable("id") Long id) {
        log.info("Find totalencaissement by ID AppelLoyer {}", id);
        return ResponseEntity.ok(encaissementPrincipalService.getTotalEncaissementByIdAppelLoyer(id));
    }
}
