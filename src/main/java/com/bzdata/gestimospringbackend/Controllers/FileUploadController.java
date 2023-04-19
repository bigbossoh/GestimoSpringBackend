package com.bzdata.gestimospringbackend.Controllers;
import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(APP_ROOT + "/file")
@RequiredArgsConstructor

@SecurityRequirement(name = "gestimoapi")
@CrossOrigin(origins = "*")
public class FileUploadController {

   // @PostMapping("/upload-file")
   // public ResponseEntity<String> uploadFile(@ RequestParam("file") MultipartFile file){
   //    System.out.println(file.getOriginalFilename());
   //    System.out.println(file.getSize());
   //    System.out.println(file.getName());
   //    System.out.println(file.getContentType());
   //    System.out.println(file.isEmpty());
   //    if(file.isEmpty()){
   //       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Request must not be null");
   //    }
   //    if(!file.getContentType().equals("image/jpeg")){
   //       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Only JPEG is allowed");
   //    }
   //    return ResponseEntity.ok("Working");

   // }

}
