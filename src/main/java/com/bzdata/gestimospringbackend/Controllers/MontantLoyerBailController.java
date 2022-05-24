package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import com.bzdata.gestimospringbackend.Services.MontantLoyerBailService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    // @PostMapping("/save")
    // @Operation(summary = "Creation et mise à jour d'un Montant loyer bail ",
    // security = @SecurityRequirement(name = "bearerAuth"))
    // public ResponseEntity<Boolean> saveMontantLoyerBail(@RequestBody
    // MontantLoyerBailDto dto) {
    // log.info("We are going to save a new MontantLoyerBailDto {}", dto);
    // return ResponseEntity.ok(montantLoyerBailService.saveNewMontantLoyerBail(
    // @RequestParam("currentUsername") String currentUsername,
    // @RequestParam("firstName") String firstName,
    // @RequestParam("lastName") String lastName,
    // @RequestParam("username") String username,
    // @RequestParam("email") String email));
    // }
}
