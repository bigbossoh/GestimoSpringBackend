package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.Utils.Constants.APP_ROOT;

import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.Services.AgenceImmobilierService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(APP_ROOT+"/agences")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "gestimoapi")
public class AgenceController {
    private final AgenceImmobilierService AgenceImmobilierService;

    @PostMapping("/signup")
    public ResponseEntity<AgenceResponseDto> authenticate(@RequestBody AgenceRequestDto request) {
        log.info("We are going to save a new agence {}",request);
        return ResponseEntity.ok(AgenceImmobilierService.save(request));
    }
    @GetMapping("/getagencebyid/{id}")
    public ResponseEntity<AgenceResponseDto> getAgenceByID(@PathVariable("id") Long id) {
        log.info("We are going to get back one agence by ID {}",id);
        return ResponseEntity.ok(AgenceImmobilierService.findAgenceById(id));
    }
}

