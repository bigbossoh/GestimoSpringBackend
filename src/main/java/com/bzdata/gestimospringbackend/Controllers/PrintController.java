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
    @Autowired
    private ServletContext servletContext;
    static final String DIRECTORY = "C:/ETAT_QUITTANCE";
    static final String DEFAULT_FILE_NAME = "appel_loyer_du_.pdf";

    @GetMapping("/quittance/{id}")
    public ResponseEntity<byte[]> sampleQuitance(@PathVariable("id") Long id)
            throws FileNotFoundException, JRException, SQLException {

        byte[] donnees = printService.quittanceLoyer(id);
        System.out.println(donnees);
        return ResponseEntity.ok(donnees);
    }

    @GetMapping("/quittancegrouper/{periodes}")
    public ResponseEntity<InputStreamResource> etatAppelLoyerGroupeParPeriode(
            @RequestParam(defaultValue = DEFAULT_FILE_NAME) String periode)
            throws FileNotFoundException, JRException, SQLException {

        String donnees = printService.quittanceLoyerGrouperParPeriodeString(periode);
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileNane(this.servletContext,
                "appel_loyer_du" + periode + ".pdf");
        File file = new File(donnees);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(mediaType)
                .contentLength(file.length()).body(resource);
    }
}
