package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.bzdata.gestimospringbackend.Services.PrintService;
import com.bzdata.gestimospringbackend.Utils.MediaTypeUtils;
import com.google.common.net.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class PrintController {
    final PrintService printService;


    @GetMapping("/quittance/{id}")
    public ResponseEntity<byte[]> sampleQuitance(@PathVariable("id") Long id)
            throws FileNotFoundException, JRException, SQLException {

        byte[] donnees = printService.quittanceLoyer(id);
        System.out.println(donnees);
        return ResponseEntity.ok(donnees);
    }

    @GetMapping(path = "/quittancegrouper/{periode}",produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<String> quittancePeriode(
            @PathVariable("periode") String periode)
            throws FileNotFoundException, JRException, SQLException {
                log.info("Periode {}", periode);
        return ResponseEntity.ok(this.printService.quittancePeriodeString(periode));
    }
}
