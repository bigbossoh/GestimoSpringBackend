package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.AgenceImmobilierDTO;
import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.Services.AgenceImmobilierService;

import org.springframework.http.ResponseEntity;
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
public class AgenceController {
    private final AgenceImmobilierService agenceImmobilierService;
    @PostMapping("/signup")
    public ResponseEntity<AgenceImmobilierDTO> authenticateAgence(@RequestBody AgenceRequestDto request) {
        log.info("We are going to save a new agence {}", request);
        return ResponseEntity.ok(agenceImmobilierService.saveUneAgence(request));
    }
    // @PostMapping("/uploadlogo")
    // public ResponseEntity<String> uploadLog(@RequestBody ImageLogoDto logo) throws IOException {
    //     log.info("We are going to save a new agence {}", logo);
    //     return ResponseEntity.ok(agenceImmobilierService.uploadLogoAgence(logo));
    // }

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
