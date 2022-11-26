package com.bzdata.gestimospringbackend.Controllers;
import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import javax.mail.Multipart;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(APP_ROOT + "/file")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "gestimoapi")
@CrossOrigin(origins = "*")
public class FileUploadController {

   @PostMapping("/upload-file")
   public ResponseEntity<String> uploadFile(@ RequestParam("file") MultipartFile file){
      System.out.println(file.getOriginalFilename());
      System.out.println(file.getSize());
      System.out.println(file.getName());
      System.out.println(file.getContentType());
      System.out.println(file.isEmpty());
      if(file.isEmpty()){
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Request must not be null");
      }
      if(!file.getContentType().equals("image/jpeg")){
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Only JPEG is allowed");
      }
      return ResponseEntity.ok("Working");

   }
   
}
