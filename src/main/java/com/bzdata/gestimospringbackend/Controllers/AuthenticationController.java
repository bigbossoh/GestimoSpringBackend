package com.bzdata.gestimospringbackend.Controllers;


import com.bzdata.gestimospringbackend.DTOs.Auth.AuthRequestDto;
import com.bzdata.gestimospringbackend.DTOs.Auth.AuthResponseDto;
import com.bzdata.gestimospringbackend.Models.auth.ExtentedUser;
import com.bzdata.gestimospringbackend.Services.Auth.ApplicationUserDetailsService;
import com.bzdata.gestimospringbackend.Services.AuthRequestService;
import com.bzdata.gestimospringbackend.Utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.bzdata.gestimospringbackend.Utils.Constants.APP_ROOT;


@RestController
@RequestMapping(APP_ROOT+"/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthRequestService authRequestService;

    @PostMapping("/login")

    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthRequestDto request) {

        return ResponseEntity.ok(authRequestService.authenticate(request));

    }


}
