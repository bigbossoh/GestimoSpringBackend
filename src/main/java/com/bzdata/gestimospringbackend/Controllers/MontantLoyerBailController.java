package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.Utils.Constants.APP_ROOT;

import com.bzdata.gestimospringbackend.DTOs.MontantLoyerBailDto;
import com.bzdata.gestimospringbackend.Services.MontantLoyerBailService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(APP_ROOT + "/montantloyerbail")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class MontantLoyerBailController {
    final MontantLoyerBailService montantLoyerBailService;

    // CREATION ET MODIFICATION D'UN PAYS
    @PostMapping("/save")
    @Operation(summary = "Creation et mise à jour d'un Montant loyer bail ", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<MontantLoyerBailDto> save(@RequestBody MontantLoyerBailDto dto) {
        log.info("We are going to save a new MontantLoyerBailDto {}", dto);
        return ResponseEntity.ok(montantLoyerBailService.saveNewMontantLoyerBail(dto));
    }
}
