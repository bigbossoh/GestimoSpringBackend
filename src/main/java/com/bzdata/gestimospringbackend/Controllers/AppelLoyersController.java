package com.bzdata.gestimospringbackend.Controllers;

import com.bzdata.gestimospringbackend.DTOs.AppartementDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyerDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyerRequestDto;
import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Services.AppartementService;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
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
@RequestMapping(APP_ROOT + "/appelloyer")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class AppelLoyersController {

    final AppelLoyerService appelLoyerService;

    @PostMapping("/save")
    @Operation(summary = "Creation et mise à jour d'un appel", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<String>> save(@RequestBody AppelLoyerRequestDto dto) {
        log.info("We are going to save a new appel {}", dto);
        return ResponseEntity.ok(appelLoyerService.save(dto));
    }
}
