package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.Utils.Constants.APP_ROOT;

import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.Services.AgenceImmobilierService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping(APP_ROOT+"/agences")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "gestimoapi")
public class AgenceController {
    private final AgenceImmobilierService agenceImmobilierService;

    @PostMapping("/signup")
    public ResponseEntity<AgenceResponseDto> authenticate(@RequestBody AgenceRequestDto request) {
        log.info("We are going to save a new agence {}",request);
        return ResponseEntity.ok(agenceImmobilierService.save(request));
    }
    @GetMapping("/getagencebyid/{id}")
    public ResponseEntity<AgenceResponseDto> getAgenceByID(@PathVariable("id") Long id) {
        log.info("We are going to get back one agence by ID {}",id);
        return ResponseEntity.ok(agenceImmobilierService.findAgenceById(id));
    }
    @GetMapping("/getagencebyemail/{email}")
    public ResponseEntity<AgenceResponseDto> getAgenceByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(agenceImmobilierService.findAgenceByEmail(email));
    }
    @GetMapping("/all")
    public ResponseEntity<List<AgenceResponseDto>> getAllAgenceByOrder(){
        return ResponseEntity.ok(agenceImmobilierService.listOfAgenceOrderByNomAgenceAsc());
    }
    @DeleteMapping("/deleteagence/{id}")
    public String deleteAgenceById(@PathVariable("id") Long id) {
        agenceImmobilierService.deleteAgence(id);
        return "procedure of deleting is executing....";
    }
}

