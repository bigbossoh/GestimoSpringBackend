package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.LocataireEncaisDTO;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Services.UtilisateurService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(APP_ROOT + "/utilisateur")
@RequiredArgsConstructor
 @Slf4j
@SecurityRequirement(name = "gestimoapi")
@CrossOrigin(origins="*")
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    @PostMapping("/save")
    public ResponseEntity<UtilisateurAfficheDto> saveUtilisateur(@RequestBody UtilisateurRequestDto request) {
         log.info("We are going to save a new locatire {}", request);
        return ResponseEntity.ok(utilisateurService.saveUtilisateur(request));
    }

    @GetMapping("/getutilisateurbyid/{id}")
    public ResponseEntity<UtilisateurRequestDto> getUtilisateurByID(@PathVariable("id") Long id) {
        // log.info("We are going to get back one utilisateur by ID {}", id);
        return ResponseEntity.ok(utilisateurService.findById(id));
    }

    @GetMapping("/getutilisateurbyemail/{email}")
    public ResponseEntity<UtilisateurRequestDto> getUtilisateurByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(utilisateurService.findUtilisateurByEmail(email));
    }
    @GetMapping("/getutilisateurbyusername/{username}")
    public ResponseEntity<UtilisateurRequestDto> getUtilisateurByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(utilisateurService.findUtilisateurByUsername(username));
    }
    @GetMapping("/getAllutilisateurbyAgence/{idAgence}")
    public ResponseEntity<List<UtilisateurAfficheDto>> getUtilisateurByAgence(@PathVariable("idAgence") Long idAgence) {
        return ResponseEntity.ok(utilisateurService.listOfAllUtilisateurLocataireOrderbyNameByAgence(idAgence));
    }
    //findUtilisateurByUsername
    @GetMapping("/all/{idAgence}")
    public ResponseEntity<List<UtilisateurAfficheDto>> getAllUtilisateursByOrder(@PathVariable("idAgence") Long idAgence) {
        return ResponseEntity.ok(utilisateurService.listOfAllUtilisateurOrderbyName(idAgence));
    }

    @GetMapping("/locataires/all/{idAgence}")
    public ResponseEntity<List<UtilisateurAfficheDto>> getAllLocatairesByOrder(@PathVariable("idAgence") Long idAgence) {
        return ResponseEntity.ok(utilisateurService.listOfAllUtilisateurLocataireOrderbyName(idAgence));
    }

    @GetMapping("/locataires/ayanbail/{idAgence}")
    public ResponseEntity<List<LocataireEncaisDTO>> getAllLocatairesAvecBail(@PathVariable("idAgence") Long idAgence) {
        return ResponseEntity.ok(utilisateurService.listOfLocataireAyantunbail(idAgence));
    }

    @GetMapping("/proprietaires/all/{idAgence}")
    public ResponseEntity<List<UtilisateurAfficheDto>> getAllProprietaireByOrder(@PathVariable("idAgence") Long idAgence) {
        return ResponseEntity.ok(utilisateurService.listOfAllUtilisateurProprietaireOrderbyName(idAgence));
    }

    @GetMapping("/gerants/all/{idAgence}")
    public ResponseEntity<List<UtilisateurAfficheDto>> getAllGerantsByOrder(@PathVariable("idAgence") Long idAgence) {
        return ResponseEntity.ok(utilisateurService.listOfAllUtilisateurGerantOrderbyName(idAgence));
    }

    @GetMapping("/superviseurs/all")
    public ResponseEntity<List<UtilisateurAfficheDto>> getAllSuperviseursByOrder() {
        return ResponseEntity.ok(utilisateurService.listOfAllUtilisateurSuperviseurOrderbyName());
    }
      @GetMapping("/clienthotel/all/{idAgence}")
    public ResponseEntity<List<UtilisateurAfficheDto>> listOfAllUtilisateurClientHotelOrderbyNameByAgence(@PathVariable("idAgence") Long idAgence) {
        return ResponseEntity.ok(utilisateurService.listOfAllUtilisateurClientHotelOrderbyNameByAgence(idAgence));
    }
}
