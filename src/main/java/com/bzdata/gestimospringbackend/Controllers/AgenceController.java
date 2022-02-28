package com.bzdata.gestimospringbackend.Controllers;

import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.Services.AgenceImmobilierService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bzdata.gestimospringbackend.Utils.Constants.APP_ROOT;

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
}

