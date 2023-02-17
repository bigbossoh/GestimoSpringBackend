package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.EncaissementPayloadDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;
import com.bzdata.gestimospringbackend.DTOs.LocataireEncaisDTO;
import com.bzdata.gestimospringbackend.Services.EncaissementPrincipalService;

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
@RequestMapping(APP_ROOT + "/encaissement")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class EncaissementPrincipalController {

    final EncaissementPrincipalService encaissementPrincipalService;

    @PostMapping("/saveencaissement")
    @Operation(summary = "Creation et encaissement", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Boolean> saveEncaissement(@RequestBody EncaissementPayloadDto dto) {
        // log.info("We are going to save a new encaissement {}", dto);
        return ResponseEntity.ok(encaissementPrincipalService.saveEncaissement(dto));
    }

    @PostMapping("/saveencaissementavecretour")
    @Operation(summary = "Creation et encaissement", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<EncaissementPrincipalDTO>> saveEncaissementAvecretourDeListe(
            @RequestBody EncaissementPayloadDto dto) {
        // log.info("We are going to save a new encaissement {}", dto);
        return ResponseEntity.ok(encaissementPrincipalService.saveEncaissementAvecRetourDeList(dto));
    }

    @PostMapping("/saveencaissementmasse")
    @Operation(summary = "Creation d'un encaissement e masse", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Boolean> saveEncaissementMasse(@RequestBody List<EncaissementPayloadDto> dtos) {
        // log.info("We are going to save a new encaissement {}", dto);
        return ResponseEntity.ok(encaissementPrincipalService.saveEncaissementMasse(dtos));
    }

    @Operation(summary = "Listés tous les envaissements de lae BD", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAllEncaissementPrincipal/{idAgence}")
    public ResponseEntity<List<EncaissementPrincipalDTO>> listTousEncaissementsPrincipal(
            @PathVariable("idAgence") Long idAgence) {
        log.info("Liste de tous les envaissements");
        return ResponseEntity.ok(encaissementPrincipalService.findAllEncaissement(idAgence));
    }

    @Operation(summary = "Total des encaissements par Id d'appel de loyer", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/totalencaissement/{id}")
    public ResponseEntity<Double> totalencaissementParIdAppelLoyer(@PathVariable("id") Long id) {
        log.info("Find totalencaissement by ID AppelLoyer {}", id);
        return ResponseEntity.ok(encaissementPrincipalService.getTotalEncaissementByIdAppelLoyer(id));
    }

    // GET Encaissement BY ID
    @Operation(summary = "Trouver un encaissement par son ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findByIdEncaissement/{id}")
    public ResponseEntity<EncaissementPrincipalDTO> findByIdEncaissement(@PathVariable("id") Long id) {
        log.info("Find by ID{}", id);
        return ResponseEntity.ok(encaissementPrincipalService.findEncaissementById(id));
    }

    // GET ALL ENCAISSEMENTS BY IDLOCATAIRE
    @Operation(summary = "Trouver tous les encaissements par son ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/allEncaissementByIdLocatire/{idLocatire}")
    public ResponseEntity<List<EncaissementPrincipalDTO>> findAllEncaissementByIdLocatire(
            @PathVariable("idLocatire") Long idLocatire) {
        log.info("Find by ID {}", idLocatire);
        return ResponseEntity.ok(encaissementPrincipalService.findAllEncaissementByIdLocataire(idLocatire));
    }

    // GET ALL ENCAISSEMENTS BY ID BIEN IMMOBILIER
    @Operation(summary = "Total des encaissements par IdBienImmobilioer d'appel de loyer", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/allencaissementByIdBien/{id}")
    public ResponseEntity<List<EncaissementPrincipalDTO>> findAllEncaissementByIdBienImmobilier(
            @PathVariable("id") Long id) {
        log.info(" find All Encaissement By IdBienImmobilier  {}", id);
        return ResponseEntity.ok(encaissementPrincipalService.findAllEncaissementByIdBienImmobilier(id));
    }

    @Operation(summary = "Total des encaissements par Id d'appel de loyer", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/totalencaissementjournalier/{jour}/{idAgence}/{chapitre}")
    public ResponseEntity<Double> totalEncaissementParJour(@PathVariable("jour") String jour,
            @PathVariable("idAgence") Long idAgence,@PathVariable("chapitre") Long chapitre) {
        log.info("Find totalencaissement by ID AppelLoyer {}", jour);
        return ResponseEntity.ok(encaissementPrincipalService.sommeEncaisserParJour(jour, idAgence,chapitre));
    }

    @GetMapping("/listeLocataireImpayerParAgenceEtPeriode/{agence}/{periode}")
    public ResponseEntity<List<LocataireEncaisDTO>> listeLocataireImpayerParAgenceEtPeriode(
            @PathVariable("agence") Long agence, @PathVariable("periode") String periode) {
        log.info("Find totalencaissement by ID AppelLoyer {}", periode);
        return ResponseEntity.ok(encaissementPrincipalService.listeLocataireImpayerParAgenceEtPeriode(agence, periode));
    }
}