package com.bzdata.gestimospringbackend.Controllers;

import com.bzdata.gestimospringbackend.DTOs.SiteResponseDto;
import com.bzdata.gestimospringbackend.DTOs.VillaDto;
import com.bzdata.gestimospringbackend.DTOs.VilleDto;
import com.bzdata.gestimospringbackend.Services.VillaService;
import com.bzdata.gestimospringbackend.Services.VilleService;
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
@RequestMapping(APP_ROOT + "/villa")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class VillaController {
    final VillaService villaService;

    @PostMapping("/save")
    @Operation(summary = "Creation et mise à jour d'une Villa", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<VillaDto> save(@RequestBody VillaDto dto) {
        log.info("We are going to save a new Villa {}", dto);
        return ResponseEntity.ok(villaService.save(dto));
    }
    // TOUT LES VILLA
    @Operation(summary = "Liste de tous les villas", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")

    public ResponseEntity<List<VillaDto>>findAll(){
        return ResponseEntity.ok(villaService.findAll());
    }
}
