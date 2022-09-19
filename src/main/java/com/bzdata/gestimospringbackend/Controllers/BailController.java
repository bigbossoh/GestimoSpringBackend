package com.bzdata.gestimospringbackend.Controllers;

import com.bzdata.gestimospringbackend.Services.BailService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "/bail")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class BailController {

    final BailService bailService;
    @Operation(summary = "Cloture du bail par rapport a son ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/clotureBail/{id}")
    public ResponseEntity<Boolean> clotureBail(@PathVariable("id") Long id) {
        log.info("cloture du bail by ID Bail {}", id);
        return ResponseEntity.ok(bailService.closeBail(id));
    }
    @Operation(summary = "nombre de bail actif", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/nombrebailactif")
    public ResponseEntity<Integer> nombrebailactif() {
        log.info("nombre de baux actifs");
        return ResponseEntity.ok(bailService.nombreBauxActifs());
    }
}
