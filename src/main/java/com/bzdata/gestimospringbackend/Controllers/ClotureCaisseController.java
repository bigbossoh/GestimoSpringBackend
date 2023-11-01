package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import com.bzdata.gestimospringbackend.DTOs.ClotureCaisseDto;
import com.bzdata.gestimospringbackend.Services.ClotureCaisseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APP_ROOT + "/cloturecaisse")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClotureCaisseController {

  final ClotureCaisseService clotureCaisseService;

  @PostMapping("/savecloturecaisse")
  public ResponseEntity<Boolean> saveClotureCaisse(
    @RequestBody ClotureCaisseDto dto
  ) {
    return ResponseEntity.ok(clotureCaisseService.saveClotureCaisse(dto));
  }

  @GetMapping("/countcloturecaisse/{idCaisse}")
  public ResponseEntity<Integer> countClotureCaisse(
    @PathVariable("idCaisse") Long idCaisse
  ) {
    return ResponseEntity.ok(clotureCaisseService.countClotureCaisse(idCaisse));
  }
}
