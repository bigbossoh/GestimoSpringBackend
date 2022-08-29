package com.bzdata.gestimospringbackend.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import com.bzdata.gestimospringbackend.DTOs.EmailRequestDto;
import com.bzdata.gestimospringbackend.Services.EmailService;

@RestController
@RequestMapping(APP_ROOT + "/envoimail")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class EmailController {

    final EmailService emailService;

    public ResponseEntity<Boolean> sendMailWithAttachment(@RequestBody EmailRequestDto dto) {
        return ResponseEntity.ok(emailService.sendMailWithAttachment(dto.getPeriode(),dto.getTo(), dto.getSubject(), dto.getBody(), dto.getFileToAttache()));
    }
}
