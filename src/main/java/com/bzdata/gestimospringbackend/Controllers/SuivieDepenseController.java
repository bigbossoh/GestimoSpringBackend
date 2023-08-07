package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseDto;
import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseEncaisPeriodeDto;
import com.bzdata.gestimospringbackend.Services.SuivieDepenseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APP_ROOT + "/suiviedepense")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "gestimoapi")
@CrossOrigin(origins = "*")
public class SuivieDepenseController {

  private final SuivieDepenseService suivieDepenseService;

  @PostMapping("/saveSuivieDepense")
  public ResponseEntity<List<SuivieDepenseDto>> saveSuivieDepense(
    @RequestBody SuivieDepenseDto dto
  ) {
    return ResponseEntity.ok(suivieDepenseService.saveNewDepense(dto));
  }

  @GetMapping("/getSuivieDepenseById/{id}")
  public ResponseEntity<SuivieDepenseDto> getSuivieDepenseById(
    @PathVariable("id") Long id
  ) {
    return ResponseEntity.ok(suivieDepenseService.findById(id));
  }

  @GetMapping("/totalSortieDeuxDate/{idAgence}/{debut}/{fin}")
  public ResponseEntity<SuivieDepenseEncaisPeriodeDto> totalSortieDeuxDate(
    @PathVariable("idAgence") Long idAgence,
    @PathVariable("debut") String debut,
    @PathVariable("fin") String fin
  ) {
    LocalDate dateDebutLocalDate = LocalDate.parse(
      debut,
      DateTimeFormatter.ofPattern("dd-MM-yyyy")
    );
    LocalDate dateFinLocalDate = LocalDate.parse(
      fin,
      DateTimeFormatter.ofPattern("dd-MM-yyyy")
    );
    log.info("Les Data sont suis : : :  {} {} {}", idAgence, dateDebutLocalDate, dateFinLocalDate);
    return ResponseEntity.ok(
      suivieDepenseService.totalSuiviDepenseEntreDeuxDate(
        idAgence,
        dateDebutLocalDate,
        dateFinLocalDate
      )
    );
  }

  @GetMapping("/getSuivieDepenseByCodeTransaction/{codeTransaction}")
  public ResponseEntity<SuivieDepenseDto> getSuivieDepenseByCodeTransaction(
    @PathVariable("codeTransaction") String codeTransaction
  ) {
    return ResponseEntity.ok(
      suivieDepenseService.findByCodeTransaction(codeTransaction)
    );
  }

  //SUPPRIMER UN SUIVI DEPENSE

  @PostMapping("/suprimerSuiviParId/{id}/{idAgence}")
  public ResponseEntity<List<SuivieDepenseDto>> suprimerSuiviParId(
    @PathVariable("id") Long id,
    @PathVariable("idAgence") Long idAgence
  ) {
    log.info("le id est le suivant : " + id);
    return ResponseEntity.ok(
      suivieDepenseService.supprimerUneEcritureById(id, idAgence)
    );
  }

  @GetMapping("/allSuivieDepense/{idAgence}")
  public ResponseEntity<List<SuivieDepenseDto>> getAllEncaissementSuivieDepenseParAgence(
    @PathVariable("idAgence") Long idAgence
  ) {
    return ResponseEntity.ok(
      suivieDepenseService.findAlEncaissementParAgence(idAgence)
    );
  }
}
