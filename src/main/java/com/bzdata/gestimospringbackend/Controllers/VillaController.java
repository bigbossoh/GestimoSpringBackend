package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.VillaDto;
import com.bzdata.gestimospringbackend.Services.VillaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping(APP_ROOT + "/villa")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class VillaController {
    final VillaService villaService;

    @PostMapping("/save")
    @Operation(summary = "Creation et mise à jour d'une Villa", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Boolean> saveVilla(@RequestBody VillaDto dto) {
        log.info("We are going to save a new Villa {}", dto);
        return ResponseEntity.ok(villaService.save(dto));
    }

    // TOUT LES VILLA
    @Operation(summary = "Liste de tous les villas", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")

    public ResponseEntity<List<VillaDto>> findAllVillaDto() {
        return ResponseEntity.ok(villaService.findAll());
    }
}
