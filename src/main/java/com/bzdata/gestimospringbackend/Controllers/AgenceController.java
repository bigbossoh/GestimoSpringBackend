package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.FileConstant.USER_FOLDER;
import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import com.bzdata.gestimospringbackend.DTOs.AgenceImmobilierDTO;
import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import com.bzdata.gestimospringbackend.Services.AgenceImmobilierService;

import com.bzdata.gestimospringbackend.repository.AgenceImmobiliereRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping(APP_ROOT + "/agences")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "gestimoapi")
public class AgenceController {
    private final AgenceImmobilierService agenceImmobilierService;
    private final AgenceImmobiliereRepository agenceImmobiliereRepository;

    @PostMapping("/signup")
    public ResponseEntity<AgenceImmobilierDTO> authenticateAgence(@RequestBody AgenceRequestDto request) {
        log.info("We are going to save a new agence {}", request);
        return ResponseEntity.ok(agenceImmobilierService.saveUneAgence(request));
    }

    @GetMapping("/getagencebyid/{id}")
    public ResponseEntity<AgenceResponseDto> getAgenceByIDAgence(@PathVariable("id") Long id) {
        log.info("We are going to get back one agence by ID {}", id);
        return ResponseEntity.ok(agenceImmobilierService.findAgenceById(id));
    }

    @GetMapping("/getagencebyemail/{email}")
    public ResponseEntity<AgenceImmobilierDTO> getAgenceByEmailAgence(@PathVariable("email") String email) {
        return ResponseEntity.ok(agenceImmobilierService.findAgenceByEmail(email));
    }

//    @GetMapping(path="/imageFilm/{idFilm}",produces = MediaType.IMAGE_JPEG_VALUE)
//    public byte[] images(@PathVariable(name="idAgence") Long idAgence) throws Exception {
//        AgenceImmobiliere agence=agenceImmobiliereRepository.findById(idAgence).get();
//        String photoAgence=agence.getProfileImageUrl();
//        File file=new File(USER_FOLDER+photoAgence);
//        Path path= Paths.get(file.toURI());
//        return Files.readAllBytes(path);
//    }

    @GetMapping("/all")
    public ResponseEntity<List<AgenceImmobilierDTO>> getAllAgenceByOrderAgence() {
        log.info("get all agence by Order");
        return ResponseEntity.ok(agenceImmobilierService.listOfAgenceOrderByNomAgenceAsc());
    }

    @DeleteMapping("/deleteagence/{id}")
    public String deleteAgenceByIdAgence(@PathVariable("id") Long id) {
        agenceImmobilierService.deleteAgence(id);
        return "procedure of deleting is executing....";
    }
}
