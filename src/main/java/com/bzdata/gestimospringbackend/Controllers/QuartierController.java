package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.QuartierDto;
import com.bzdata.gestimospringbackend.Services.QuartierService;

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
@RequestMapping(APP_ROOT + "/quartier")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class QuartierController {
    final QuartierService quartierService;

    @Operation(summary = "Suppression d'un Quartier avec l'ID en paramètre", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteQuartier(@PathVariable("id") Long id) {
        log.info("We are going to save a new Ville {}", id);
        return ResponseEntity.ok(quartierService.delete(id));
    }

    @PostMapping("/save")
    @Operation(summary = "Creation et mise à jour d'un Quartier", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<QuartierDto> saveQuartier(@RequestBody QuartierDto dto) {
        log.info("We are going to save a new Commune {}", dto);
        return ResponseEntity.ok(quartierService.save(dto));
    }

    @Operation(summary = "Liste de tous les Quartiers", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<List<QuartierDto>> findAllQuartiers() {
        return ResponseEntity.ok(quartierService.findAll());
    }

    @Operation(summary = "Trouver un Quartier par son ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findById/{id}")
    public ResponseEntity<QuartierDto> findByIDQuartiers(@PathVariable("id") Long id) {
        log.info("Find Commune by ID{}", id);
        return ResponseEntity.ok(quartierService.findById(id));
    }

    @Operation(summary = "Trouver un Quartier par son nom", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findByName/{name}")
    public ResponseEntity<QuartierDto> findByNameQuartier(@PathVariable("name") String name) {
        log.info("Find Quartier By nom {}", name);
        return ResponseEntity.ok(quartierService.findByName(name));
    }

    @Operation(summary = "Trouver un Quartier par l'Id de la Ville", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findByIdCommune/{id}")
    public ResponseEntity<List<QuartierDto>> findAllQuartierByIdCommune(@PathVariable("id") Long id) {
        log.info("Find Quartier By Id Commune {}", id);
        return ResponseEntity.ok(quartierService.findAllByIdCommune(id));
    }
}
