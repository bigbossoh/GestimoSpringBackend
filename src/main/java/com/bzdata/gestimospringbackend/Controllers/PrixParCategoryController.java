package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import com.bzdata.gestimospringbackend.DTOs.PrixParCategorieChambreDto;
import com.bzdata.gestimospringbackend.Services.PrixParCategorieChambreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APP_ROOT + "/prixparcategorie")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
//@SecurityRequirement(name = "gestimoapi")
@CrossOrigin(origins = "*")
public class PrixParCategoryController {

  final PrixParCategorieChambreService parCategorieChambreService;

  @Operation(
    summary = "ajouter d'un Quartier avec l'ID en paramètre",
    security = @SecurityRequirement(name = "bearerAuth")
  )
  @PostMapping("/saveOrUpDatePrixParCategorie")
  public ResponseEntity<PrixParCategorieChambreDto> saveOrUpDatePrixParCategorie(
    @RequestBody PrixParCategorieChambreDto dto
  ) {
    log.info("We are going to save a new Ville {}", dto);
    return ResponseEntity.ok(
      parCategorieChambreService.saveOrUpDatePrixPArCategoryChambre(dto)
    );
  }
}
