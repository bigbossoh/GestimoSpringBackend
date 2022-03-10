package com.bzdata.gestimospringbackend.Controllers;

import com.bzdata.gestimospringbackend.DTOs.MagasinDto;
import com.bzdata.gestimospringbackend.DTOs.VillaDto;
import com.bzdata.gestimospringbackend.Services.MagasinService;
import com.bzdata.gestimospringbackend.Services.VillaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bzdata.gestimospringbackend.Utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "/magasin")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class MagasinController {
    final MagasinService magasinService;

    @PostMapping("/save")
    @Operation(summary = "Creation et mise à jour d'une Magasin", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<MagasinDto> save(@RequestBody MagasinDto dto) {
        log.info("We are going to save a new Magasin {}", dto);
        return ResponseEntity.ok(magasinService.save(dto));
    }
    // TOUT LES VILLA
    @Operation(summary = "Liste de tous les Magasins", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")

    public ResponseEntity<List<MagasinDto>>findAll(){
        return ResponseEntity.ok(magasinService.findAll());
    }
}
