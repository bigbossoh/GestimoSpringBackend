package com.bzdata.gestimospringbackend.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseDto;
import com.bzdata.gestimospringbackend.Services.SuivieDepenseService;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.util.List;
@RestController
@RequestMapping(APP_ROOT + "/suiviedepense")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "gestimoapi")
@CrossOrigin(origins = "*")
public class SuivieDepenseController {
   private final SuivieDepenseService suivieDepenseService;
   @PostMapping("/saveSuivieDepense")
   public ResponseEntity<List<SuivieDepenseDto>> saveSuivieDepense(@RequestBody SuivieDepenseDto dto) {
System.out.println("On n'est ici : ");
        log.info("We are going to save a new suive depense from controller {}", dto);
        return ResponseEntity.ok(suivieDepenseService.saveNewDepense(dto));
    }

    @GetMapping("/getSuivieDepenseById/{id}")
    public ResponseEntity<SuivieDepenseDto> getSuivieDepenseById(
            @PathVariable("id") Long id) {
        log.info("get a depense by id {}", id);
        return ResponseEntity.ok(suivieDepenseService.findById(id));
    }

    @GetMapping("/getSuivieDepenseByCodeTransaction/{codeTransaction}")
    public ResponseEntity<SuivieDepenseDto> getSuivieDepenseByCodeTransaction(
            @PathVariable("codeTransaction") String codeTransaction) {
        log.info("get a depense by codeTransaction {}", codeTransaction);
        return ResponseEntity.ok(suivieDepenseService.findByCodeTransaction(codeTransaction));
    }

    @GetMapping("/allSuivieDepense/{idAgence}")
    public ResponseEntity<List<SuivieDepenseDto>> getAllEncaissementSuivieDepenseParAgence(
            @PathVariable("idAgence") Long idAgence) {
        log.info("get all agence by Order");
        return ResponseEntity.ok(suivieDepenseService.findAlEncaissementParAgence(idAgence));
    }
}
