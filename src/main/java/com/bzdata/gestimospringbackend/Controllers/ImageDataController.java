package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bzdata.gestimospringbackend.DTOs.ImageDataDto;
import com.bzdata.gestimospringbackend.Services.ImageDataService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@RestController
@RequestMapping(APP_ROOT + "/image")
@RequiredArgsConstructor
// @Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
@CrossOrigin(origins = "*")
@Api(tags = "Image controlleur")
public class ImageDataController {
    final ImageDataService imageDataService;
    @PostMapping("/upload/{id}/{name}/")
    @ApiOperation("Téléchargder une image")
    public ResponseEntity<Boolean>uploadImage( @PathVariable("id")Long id, @PathVariable("name")String name,@RequestParam("file") MultipartFile file) throws IOException{
        return ResponseEntity.ok().body(imageDataService.saveImageAppartement(id, name, file));
    }
    @GetMapping("/imagesbybien/{id}") 
    public ResponseEntity<List<ImageDataDto> >telechagerImage(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(imageDataService.loadImageFromBien(id));
    }
}
