package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.AUTHENTICATION_ENDPOINT;
import static com.bzdata.gestimospringbackend.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.DTOs.Auth.AuthRequestDto;
import com.bzdata.gestimospringbackend.Models.UserPrincipal;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.AgenceImmobilierService;
import com.bzdata.gestimospringbackend.Services.UtilisateurService;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.utility.JWTTokenProvider;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = { AUTHENTICATION_ENDPOINT, "/" })
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "gestimoapi")
public class AuthenticationController {

    private final AgenceImmobilierService AgenceImmobilierService;
    private final UtilisateurService utilisateurService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;
    private final UtilisateurRepository utilisateurRepository;
    @PostMapping("/login")
    public ResponseEntity<Utilisateur> login(@RequestBody AuthRequestDto request) {

        authenticate(request.getUsername(), request.getPassword());
        UtilisateurRequestDto utilisateurByUsername = utilisateurService
                .findUtilisateurByUsername(request.getUsername());
        log.info("get back id of the user {}", utilisateurByUsername.getId());
        Utilisateur userCreate = utilisateurRepository.findById(utilisateurByUsername.getId()).orElseThrow(
                () -> new InvalidEntityException(
                        "Aucun Utilisateur has been found with Code " + utilisateurByUsername.getId(),
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
        Utilisateur loginUser = userCreate;
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        log.info("depuis la method principale login : {}", userPrincipal.toString());
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
       //  log.info("we are going lo launch sms to the user ");
//         SmsRequest sms =new SmsRequest("+2550103833350","Le "+loginUser.getUrole()+" "+loginUser.getNom() +" "+"a été connecté avec succès");
//         twilioSmsSender.sendSms(sms);
       //  log.info("Sms sent");
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    private void authenticate(String username, String password) {
        log.info("we are here in the login resource {} ", username);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        log.info("depuis la fonction getJwtHeader {}", userPrincipal.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
        return headers;
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        AgenceImmobilierService.verifyAccount(token);
        return new ResponseEntity<>("Le compte est activé avec succès", OK);
    }

}
