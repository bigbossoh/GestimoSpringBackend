package com.bzdata.gestimospringbackend.Controllers;


import static com.bzdata.gestimospringbackend.Utils.Constants.APP_ROOT;
import static org.springframework.http.HttpStatus.OK;

import com.bzdata.gestimospringbackend.DTOs.Auth.AuthRequestDto;
import com.bzdata.gestimospringbackend.DTOs.Auth.AuthResponseDto;
import com.bzdata.gestimospringbackend.Services.AgenceImmobilierService;
import com.bzdata.gestimospringbackend.Services.AuthRequestService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(APP_ROOT+"/auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "gestimoapi")
public class AuthenticationController {

    private final AuthRequestService authRequestService;
    private final AgenceImmobilierService AgenceImmobilierService;

    @PostMapping("/login")
    @Operation(summary = "Authentification des SUPERVISEUR", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthRequestDto request) {

        return ResponseEntity.ok(authRequestService.authenticate(request));

    }
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        AgenceImmobilierService.verifyAccount(token);
        return new ResponseEntity<>("Le compte est activé avec succès",OK);
    }


}
