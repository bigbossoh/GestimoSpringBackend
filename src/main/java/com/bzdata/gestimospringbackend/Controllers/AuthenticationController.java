package com.bzdata.gestimospringbackend.Controllers;


import static com.bzdata.gestimospringbackend.Utils.Constants.APP_ROOT;

import com.bzdata.gestimospringbackend.DTOs.Auth.AuthRequestDto;
import com.bzdata.gestimospringbackend.DTOs.Auth.AuthResponseDto;
import com.bzdata.gestimospringbackend.Services.AuthRequestService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(APP_ROOT+"/auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "gestimoapi")
public class AuthenticationController {

    private final AuthRequestService authRequestService;

    @PostMapping("/login")
    @Operation(summary = "Authentification des SUPERVISEUR", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthRequestDto request) {

        return ResponseEntity.ok(authRequestService.authenticate(request));

    }


}
