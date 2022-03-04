package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.Utils.Constants.APP_ROOT;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Services.UtilisateurService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(APP_ROOT + "/utilisateur")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "gestimoapi")
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    @PostMapping("/locataire/save")
    public ResponseEntity<UtilisateurRequestDto> saveLocataire(@RequestBody UtilisateurRequestDto request) {
        log.info("We are going to save a new locatire {}", request);
        return ResponseEntity.ok(utilisateurService.saveLocataire(request));
    }

    @PostMapping("/proprietaire/save")
    public ResponseEntity<UtilisateurRequestDto> saveProprietaire(@RequestBody UtilisateurRequestDto request) {
        log.info("We are going to save a new Proprietaire {}", request);
        return ResponseEntity.ok(utilisateurService.saveProprietaire(request));
    }

    @PostMapping("/gerant/save")
    public ResponseEntity<UtilisateurRequestDto> saveGerant(@RequestBody UtilisateurRequestDto request) {
        log.info("We are going to save a new Gerant {}", request);
        return ResponseEntity.ok(utilisateurService.saveGerant(request));
    }

    @GetMapping("/getutilisateurbyid/{id}")
    public ResponseEntity<UtilisateurRequestDto> getUtilisateurByID(@PathVariable("id") Long id) {
        log.info("We are going to get back one utilisateur by ID {}", id);
        return ResponseEntity.ok(utilisateurService.findById(id));
    }

    @GetMapping("/getutilisateurbyemail/{email}")
    public ResponseEntity<UtilisateurRequestDto> getUtilisateurByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(utilisateurService.findByEmail(email));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UtilisateurRequestDto>> getAllUtilisateurByOrder() {
        return ResponseEntity.ok(utilisateurService.listOfAllUtilisateurOrderbyName());
    }

    @GetMapping("/locataires/all")
    public ResponseEntity<List<UtilisateurRequestDto>> getAllLocataireByOrder() {
        return ResponseEntity.ok(utilisateurService.listOfAllUtilisateurLocataireOrderbyName());
    }

    @GetMapping("/proprietaires/all")
    public ResponseEntity<List<UtilisateurRequestDto>> getAllProprietaireByOrder() {
        return ResponseEntity.ok(utilisateurService.listOfAllUtilisateurProprietaireOrderbyName());
    }

    @GetMapping("/gerants/all")
    public ResponseEntity<List<UtilisateurRequestDto>> getAllGerantByOrder() {
        return ResponseEntity.ok(utilisateurService.listOfAllUtilisateurGerantOrderbyName());
    }

    @GetMapping("/superviseurs/all")
    public ResponseEntity<List<UtilisateurRequestDto>> getAllSuperviseurByOrder() {
        return ResponseEntity.ok(utilisateurService.listOfAllUtilisateurSuperviseurOrderbyName());
    }
}
