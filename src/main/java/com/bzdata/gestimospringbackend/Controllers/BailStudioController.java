package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.Utils.Constants.APP_ROOT;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.BailStudioDto;
import com.bzdata.gestimospringbackend.Services.BailStudioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(APP_ROOT + "/bailsudio")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class BailStudioController {
    final BailStudioService bailStudioService;

    @Operation(summary = "Suppression d'un Bail Studio avec l'ID en paramètre", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        log.info("We are going to delete a Bail Studio {}", id);
        return ResponseEntity.ok(bailStudioService.delete(id));
    }

    @PostMapping("/save")
    @Operation(summary = "Creation et mise à jour d'un Bail Studio", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BailStudioDto> save(@RequestBody BailStudioDto dto) {
        log.info("We are going to save a new Bail Studio {}", dto);
        return ResponseEntity.ok(bailStudioService.save(dto));
    }

    @Operation(summary = "Liste de toutes les Baux Studio", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<List<BailStudioDto>> findAll() {
        return ResponseEntity.ok(bailStudioService.findAll());
    }

    @Operation(summary = "Trouver un Bail Studio par son ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findById/{id}")
    public ResponseEntity<BailStudioDto> findByID(@PathVariable("id") Long id) {
        log.info("Find Bail Studio by ID{}", id);
        return ResponseEntity.ok(bailStudioService.findById(id));
    }

    @Operation(summary = "Trouver un Bail par son nom", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findByName/{name}")
    public ResponseEntity<BailStudioDto> findByName(@PathVariable("name") String name) {
        log.info("Find Bail Magain By nom {}", name);
        return ResponseEntity.ok(bailStudioService.findByName(name));
    }
}
