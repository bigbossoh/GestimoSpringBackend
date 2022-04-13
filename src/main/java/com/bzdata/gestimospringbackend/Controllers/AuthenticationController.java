package com.bzdata.gestimospringbackend.Controllers;


import static com.bzdata.gestimospringbackend.Utils.Constants.APP_ROOT;
import static com.bzdata.gestimospringbackend.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

import com.bzdata.gestimospringbackend.DTOs.Auth.AuthRequestDto;
import com.bzdata.gestimospringbackend.DTOs.Auth.AuthResponseDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.UserPrincipal;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.AgenceImmobilierService;
import com.bzdata.gestimospringbackend.Services.AuthRequestService;

import com.bzdata.gestimospringbackend.Services.UtilisateurService;
import com.bzdata.gestimospringbackend.utility.JWTTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(path={APP_ROOT+"/auth", "/"})
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "gestimoapi")
public class AuthenticationController {

    private final AuthRequestService authRequestService;
    private final AgenceImmobilierService AgenceImmobilierService;
    private final UtilisateurService utilisateurService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

//    @PostMapping("/login")
//    @Operation(summary = "Authentification des SUPERVISEUR", security = @SecurityRequirement(name = "bearerAuth"))
//    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthRequestDto request) {
//        log.info("we are going to login...");
//        return ResponseEntity.ok(authRequestService.authenticate(request));
//
//    }
@PostMapping("/login")
public ResponseEntity<Utilisateur> login(@RequestBody AuthRequestDto request) {
    log.info("we are here in the login resource {} ",request.getUsername());
    authenticate(request.getUsername(), request.getPassword());
    UtilisateurRequestDto utilisateurByUsername = utilisateurService.findUtilisateurByUsername(request.getUsername());
    Utilisateur loginUser = UtilisateurRequestDto.toEntity(utilisateurByUsername);
    UserPrincipal userPrincipal = new UserPrincipal(loginUser);

    HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
    return new ResponseEntity<>(loginUser, jwtHeader, OK);
}
    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
        return headers;
    }
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        AgenceImmobilierService.verifyAccount(token);
        return new ResponseEntity<>("Le compte est activé avec succès",OK);
    }


}
