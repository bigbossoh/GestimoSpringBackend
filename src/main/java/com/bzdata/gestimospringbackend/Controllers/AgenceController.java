package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.io.IOException;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.AgenceImmobilierDTO;
import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.Services.AgenceImmobilierService;
import com.bzdata.gestimospringbackend.Services.ImagesService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(APP_ROOT + "/agences")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "gestimoapi")
@CrossOrigin(origins = "*")
public class AgenceController {
    private final AgenceImmobilierService agenceImmobilierService;
    private final ImagesService imagesService;

    @PostMapping("/signup")
    public ResponseEntity<AgenceImmobilierDTO> authenticateAgence(@RequestBody AgenceRequestDto request) {
        log.info("We are going to save a new agence {}", request);
        return ResponseEntity.ok(agenceImmobilierService.saveUneAgence(request));
    }

    @PostMapping("/savelogo")
    public ResponseEntity<byte[]> saveLogo(@RequestBody AgenceRequestDto request) throws IOException {
        log.info("We are going to save a logo {}", request);
        return ResponseEntity.ok(imagesService.saveLogo(request));
    }

    @GetMapping("/getagencebyid/{id}")
    public ResponseEntity<AgenceResponseDto> getAgenceByIDAgence(@PathVariable("id") Long id) {
        log.info("We are going to get back one agence by ID {}", id);
        return ResponseEntity.ok(agenceImmobilierService.findAgenceById(id));
    }
    @GetMapping("/getlogo/{id}")
    public ResponseEntity<byte[]> getlogo(@PathVariable("id") Long id) {
        log.info("We are going to get back one agence by ID {}", id);
        return ResponseEntity.ok(imagesService.getLogo(id));
    }
    @GetMapping("/getagencebyemail/{email}")
    public ResponseEntity<AgenceImmobilierDTO> getAgenceByEmailAgence(@PathVariable("email") String email) {
        return ResponseEntity.ok(agenceImmobilierService.findAgenceByEmail(email));
    }

    @GetMapping("/all/{idAgence}")
    public ResponseEntity<List<AgenceImmobilierDTO>> getAllAgenceByOrderAgence(@PathVariable("idAgence") Long idAgence) {
        log.info("get all agence by Order");
        return ResponseEntity.ok(agenceImmobilierService.listOfAgenceOrderByNomAgenceAsc(idAgence));
    }

    @DeleteMapping("/deleteagence/{id}")
    public String deleteAgenceByIdAgence(@PathVariable("id") Long id) {
        agenceImmobilierService.deleteAgence(id);
        return "procedure of deleting is executing....";
    }
}
