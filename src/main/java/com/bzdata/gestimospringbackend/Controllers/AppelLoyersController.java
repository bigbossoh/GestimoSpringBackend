package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bzdata.gestimospringbackend.DTOs.AnneeAppelLoyersDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyerDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyerRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;
import com.bzdata.gestimospringbackend.DTOs.PeriodeDto;
import com.bzdata.gestimospringbackend.DTOs.PourcentageAppelDto;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;

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

    @Operation(summary = "Trouver un appel loyer par son ID Bail", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAppelsByIdBail/{id}")
    public ResponseEntity<List<AppelLoyerDto>> listDesLoyersParBail(@PathVariable("id") Long id) {
        log.info("Find Appel by ID Bail {}", id);
        return ResponseEntity.ok(appelLoyerService.findAllAppelLoyerByBailId(id));
    }

    @Operation(summary = "Cloture un appel Loyer par rapporta son ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/clotureOfAppelDtoByID/{id}")
    public ResponseEntity<Boolean> deleteAppelDto(@PathVariable("id") Long id) {
        log.info("cloture de l' Appel by ID Bail {}", id);
        return ResponseEntity.ok(appelLoyerService.cloturerAppelDto(id));
    }

    @Operation(summary = "Trouver un appel loyer impayé par son ID Bail", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAppelsImpayerByIdBail/{id}")
    public ResponseEntity<List<AppelLoyersFactureDto>> listDesLoyersImpayerParBail(@PathVariable("id") Long id) {
        log.info("Find Appel by ID Bail {}", id);
        return ResponseEntity.ok(appelLoyerService.findAllAppelLoyerImpayerByBailId(id));
    }

    @Operation(summary = "Trouver un appel loyer par son ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAppelloyer/{id}")
    public ResponseEntity<AppelLoyersFactureDto> AppelLoyersParId(@PathVariable("id") Long id) {
        log.info("Find Appel by ID Bail {}", id);
        return ResponseEntity.ok(appelLoyerService.findById(id));
    }

    @Operation(summary = "Trouver tous les appels loyers par periode", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAllAppelloyerByPeriode/{periode}/{idAgence}")
    public ResponseEntity<List<AppelLoyersFactureDto>> AppelLoyersParPeriode(@PathVariable("periode") String periode,
            @PathVariable("idAgence") Long idAgence) {
        log.info("Find Appel by periode {}", periode);
        return ResponseEntity.ok(appelLoyerService.findAllAppelLoyerByPeriode(periode, idAgence));
    }

    @Operation(summary = "Trouver tous les appels loyers par annee", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAllPeriodeByAnnee/{annee}/{idAgence}")
    public ResponseEntity<List<PeriodeDto>> findAllPeriodeByAnnee(@PathVariable("annee") Integer annee,
            @PathVariable("idAgence") Long idAgence) {
        log.info("Find Appelperiode by annee {}", annee);
        return ResponseEntity.ok(appelLoyerService.listOfPerodesByAnnee(annee, idAgence));
    }

    @Operation(summary = "Trouver tous les période des appel", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAllPeriodeAppel/{idAgence}")
    public ResponseEntity<List<PeriodeDto>> findAllPeriode(@PathVariable("idAgence") Long idAgence) {
        log.info("Find Appelperiode by annee {}");
        return ResponseEntity.ok(appelLoyerService.findAllPeriode(idAgence));
    }

    @Operation(summary = "Trouver tous les appels loyers par annee", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAllPeriodeChiffreEtLettreByAnnee/{annee}/{idAgence}")
    public ResponseEntity<List<AnneeAppelLoyersDto>> findAllPeriodeChiffreEtLettreByAnnee(
            @PathVariable("annee") Integer annee, @PathVariable("idAgence") Long idAgence) {
        log.info("Find Appelperiode by annee {}", annee);
        return ResponseEntity.ok(appelLoyerService.listOfAppelLoyerByAnnee(annee, idAgence));
    }

    @Operation(summary = "Trouver toutes les années appels loyers ", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/listOfDistinctAnneeAppel/{idAgence}")
    public ResponseEntity<List<Integer>> listOfDistinctAnneeAppel(@PathVariable("idAgence") Long idAgence) {
        log.info("Find liste des annees Appel periode.");
        return ResponseEntity.ok(appelLoyerService.listOfDistinctAnnee(idAgence));
    }

    @Operation(summary = "Lister tous les appels de BD", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAllAppelsLoyer/{idAgence}")
    public ResponseEntity<List<AppelLoyersFactureDto>> listTousAppelsLoyers(@PathVariable("idAgence") Long idAgence) {
        log.info("Liste de tous les appels ");
        return ResponseEntity.ok(appelLoyerService.findAll(idAgence));
    }

    @Operation(summary = "Trouver tous les appels loyers par periode", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findAllAppelloyerBybien/{id}")
    public ResponseEntity<AppelLoyersFactureDto> getFirstLoyerImpayerByBien(@PathVariable("id") Long id) {
        log.info("Find Appel by loy {}", id);
        return ResponseEntity.ok(appelLoyerService.getFirstLoyerImpayerByBien(id));
    }


    @Operation(summary = "Trouver tous les appels loyers par periode", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findByIdAndBail/{idBien}/{periode}")
    public ResponseEntity<AppelLoyersFactureDto> findByIdAndBail(@PathVariable("idBien") Long idBien,
            @PathVariable("periode") String periode) {
        log.info("Find Appel by loy {},{}", idBien, periode);
        return ResponseEntity.ok(appelLoyerService.findByIdAndBail(periode, idBien));
    }
    @Operation(summary = "Trouver tous les appels loyers par periode", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/listeDesloyerSuperieurAUnePeriode/{idBien}/{periode}")
    public ResponseEntity<List<AppelLoyersFactureDto>> listeDesloyerSuperieurAUnePeriode(@PathVariable("idBien") Long idBien,@PathVariable("periode") String periode) {
        log.info("Find Appel by loy {},{}", idBien, periode);
        return ResponseEntity.ok(appelLoyerService.listeDesloyerSuperieurAUnePeriode( periode,idBien));
    }
    @Operation(summary = "Trouver tous les appels loyers impayé par periode", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/impayeParMois/{periode}/{idAgence}/{chapitre}")
    public ResponseEntity<Double> impayeLoyerParMois(@PathVariable("periode") String periode,
            @PathVariable("idAgence") Long idAgence,@PathVariable("chapitre")Long chapitre) {
        log.info("Find Appel by loyer en fonction de la periode {}, et de l id AGENCE {}", periode, idAgence);
        return ResponseEntity.ok(appelLoyerService.impayeParPeriode(periode, idAgence,chapitre));
    }

    @Operation(summary = "Trouver tous les appels loyers payé par periode", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/payeParMois/{periode}/{idAgence}/{chapitre}")
    public ResponseEntity<Double> payeLoyerParMois(@PathVariable("periode") String periode,
            @PathVariable("idAgence") Long idAgence,@PathVariable("chapitre")Long chapitre) {
        log.info("Find Appel by loy {}", periode);
        return ResponseEntity.ok(appelLoyerService.payeParPeriode(periode, idAgence,chapitre));
    }

    @Operation(summary = "Trouver tous les appels loyers payé par Année", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/payeParAnnee/{annee}/{idAgence}/{chapitre}")
    public ResponseEntity<Double> payeLoyerParAnnee(@PathVariable("annee") int annee,
            @PathVariable("idAgence") Long idAgence,@PathVariable("chapitre")Long chapitre) {
        log.info("Find Appel by loy {}", annee);
        return ResponseEntity.ok(appelLoyerService.payeParAnnee(annee, idAgence,chapitre));
    }

    @Operation(summary = "Trouver tous les appels loyers payé par Année", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/impayeParAnnee/{annee}/{idAgence}/{chapitre}")
    public ResponseEntity<Double> impayeLoyerParAnnee(@PathVariable("annee") int annee,
            @PathVariable("idAgence") Long idAgence,@PathVariable("chapitre")Long chapitre) {
        log.info("Find Appel by loy {}", annee);
        return ResponseEntity.ok(appelLoyerService.impayeParAnnee(annee, idAgence,chapitre));
    }

    @Operation(summary = "Trouver tous les appels loyers par periode", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/reductionLoyerByPeriode")
    public ResponseEntity<List<AppelLoyersFactureDto>> ReductionLoyerByPeriode(
            @RequestBody PourcentageAppelDto pourcentageAppelDto) {
        log.info("Modif Appel by periode {}", pourcentageAppelDto.getPeriodeAppelLoyer());
        return ResponseEntity.ok(appelLoyerService.reductionLoyerByPeriode(pourcentageAppelDto));
    }
}
