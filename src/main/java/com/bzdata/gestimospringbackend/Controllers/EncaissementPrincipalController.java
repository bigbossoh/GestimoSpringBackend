package com.bzdata.gestimospringbackend.Controllers;

import com.bzdata.gestimospringbackend.DTOs.CommuneRequestDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPayloadDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;
import com.bzdata.gestimospringbackend.Services.CommuneService;
import com.bzdata.gestimospringbackend.Services.EncaissementPrincipalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "/encaissement")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class EncaissementPrincipalController {

    final EncaissementPrincipalService encaissementPrincipalService;


    @PostMapping("/saveencaissement")
    @Operation(summary = "Creation et encaissement", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<EncaissementPrincipalDTO> saveCommune(@RequestBody EncaissementPayloadDto dto) {
        log.info("We are going to save a new Commune {}", dto);
        return ResponseEntity.ok(encaissementPrincipalService.saveEncaissement(dto));
    }
}
