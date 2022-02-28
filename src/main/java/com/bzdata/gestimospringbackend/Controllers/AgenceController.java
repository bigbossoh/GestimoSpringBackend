package com.bzdata.gestimospringbackend.Controllers;

import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.Services.AgenceImmobilierService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bzdata.gestimospringbackend.Utils.Constants.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT+"/agences")
@RequiredArgsConstructor
public class AgenceController {
    private final AgenceImmobilierService AgenceImmobilierService;

    @PostMapping("/signup")

    public ResponseEntity<AgenceResponseDto> authenticate(@RequestBody AgenceRequestDto request) {

        return ResponseEntity.ok(AgenceImmobilierService.save(request));

    }
}

