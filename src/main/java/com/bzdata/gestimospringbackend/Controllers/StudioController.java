package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.StudioDto;
import com.bzdata.gestimospringbackend.Services.StudioService;

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
@RequestMapping(APP_ROOT + "/studio")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class StudioController {
    final StudioService studioService;

    @Operation(summary = "Suppression d'un Studio avec l'ID en paramètre", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        log.info("We are going to delete a Studio {}", id);
        return ResponseEntity.ok(studioService.delete(id));
    }

    @PostMapping("/save")
    @Operation(summary = "Creation et mise à jour d'un Studio", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<StudioDto> save(@RequestBody StudioDto dto) {
        log.info("We are going to save a new Studio {}", dto);
        return ResponseEntity.ok(studioService.save(dto));
    }

    @Operation(summary = "Liste de toutes les Studios", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<List<StudioDto>> findAll() {
        return ResponseEntity.ok(studioService.findAll());
    }

    @Operation(summary = "Trouver un Studio par son ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findById/{id}")
    public ResponseEntity<StudioDto> findByID(@PathVariable("id") Long id) {
        log.info("Find Appartement by ID{}", id);
        return ResponseEntity.ok(studioService.findById(id));
    }

    @Operation(summary = "Trouver un Studio par son nom", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findByName/{name}")
    public ResponseEntity<StudioDto> findByName(@PathVariable("name") String name) {
        log.info("Find Studio By nom {}", name);
        return ResponseEntity.ok(studioService.findByName(name));
    }

    @Operation(summary = "Trouver une Studio par l'Id de l'étage", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findByIdEtage/{id}")
    public ResponseEntity<List<StudioDto>> findByIdPays(@PathVariable("id") Long id) {
        log.info("Find Studio By Id Pays {}", id);
        return ResponseEntity.ok(studioService.findAllByIdEtage(id));
    }
}
