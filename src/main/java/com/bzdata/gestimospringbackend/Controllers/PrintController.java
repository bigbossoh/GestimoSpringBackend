package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import com.bzdata.gestimospringbackend.Services.PrintService;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping(APP_ROOT + "/print")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
@Slf4j
@CrossOrigin(origins = "*")
public class PrintController {
    final PrintService printService;

    @GetMapping("/quittance/{id}")
    public ResponseEntity<byte[]> sampleQuitance(@PathVariable("id") Long id)
            throws FileNotFoundException, JRException, SQLException {

        byte[] donnees = printService.quittanceLoyer(id);
        System.out.println(donnees);
        return ResponseEntity.ok(donnees);
    }

    @GetMapping(path = "/quittancegrouper/{periode}/{idAgence}/{proprio}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> quittancePeriode(
            @PathVariable("periode") String periode, @PathVariable("idAgence") Long idAgence,
            @PathVariable("proprio") String proprio)
            throws FileNotFoundException, JRException, SQLException, IOException {
        byte[] bytes = this.printService.quittancePeriodeString(periode, idAgence, proprio);
        String path = "src/main/resources/templates/depot_etat/appel_loyer_du_" + periode + ".pdf";
        File leFichierTelecharger = new File(path);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(leFichierTelecharger));
        log.info("Periode Pour le Test de AMAZON Periode , chemin,Nom du fichier,{},{},{}", periode, path,
                leFichierTelecharger.getName());
        return ResponseEntity.ok()
                // CONTENT-DISPOSITION
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + leFichierTelecharger.getName())
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(leFichierTelecharger.length())
                .body(resource);
    }
}
