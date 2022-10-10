package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;
import com.bzdata.gestimospringbackend.DTOs.OperationDto;
import com.bzdata.gestimospringbackend.Services.BailService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

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
    @Operation(summary = "All Bail By BienImmobilier", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/getallbailbybien/{id}")
    public ResponseEntity<List<AppelLoyersFactureDto>> listDesBauxPourUnBienImmobilier(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bailService.findAllByIdBienImmobilier(id));
    }
    @Operation(summary = "All Bail By BienImmobilier", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/getallbailbylocataire/{id}")
    public ResponseEntity<List<OperationDto>> listDesBauxPourUnLocataire(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bailService.findAllByIdLocataire(id));
    }
    @Operation(summary = "Cloture du bail par rapport a son ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/supprimerBail/{id}")
    public ResponseEntity<Boolean> supprimerBail(@PathVariable("id") Long id) {
        log.info("cloture du bail by ID Bail {}", id);
        return ResponseEntity.ok(bailService.deleteOperationById(id));
    }
}
