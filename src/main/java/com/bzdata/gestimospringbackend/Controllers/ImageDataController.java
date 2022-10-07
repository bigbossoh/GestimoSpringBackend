package com.bzdata.gestimospringbackend.Controllers;

import com.bzdata.gestimospringbackend.Services.StorageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT + "/images")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class ImageDataController {
    final StorageService storageService;
    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = storageService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
        byte[] imageData=storageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }
    @PostMapping("/fileSystem")
    public ResponseEntity<?> uploadImageToFIleSystem(@RequestParam("image")MultipartFile file) throws IOException {
        String uploadImage = storageService.uploadImageToFileSystem(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/fileSystem/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        log.info("We are going to get a file");
        byte[] imageData=storageService.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }

}
