package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.Auth.AuthRequestDto;
import com.bzdata.gestimospringbackend.DTOs.Auth.AuthResponseDto;
import com.bzdata.gestimospringbackend.Models.auth.ExtentedUser;
import com.bzdata.gestimospringbackend.Services.AuthRequestService;
import com.bzdata.gestimospringbackend.Services.Auth.ApplicationUserDetailsService;
import com.bzdata.gestimospringbackend.Utils.JwtUtil;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.validator.AuthRequestDtoValidator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AuthRequestServiceImpl implements AuthRequestService {

    private final AuthenticationManager authenticationManager;

    private final ApplicationUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    @Override
    public AuthResponseDto authenticate(AuthRequestDto dto) {

             log.info("We are going to login into the system {}",dto);
    List<String> errors= AuthRequestDtoValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error("Les informations fournies par l'utilisateur ne sont pas valide {}",errors);
            throw new InvalidEntityException("Les informations fournies par l'utilisateur ne sont pas valide",
                    ErrorCodes.UTILISATEUR_NOT_VALID,errors);
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());

        final String jwt = jwtUtil.generateToken((ExtentedUser)  userDetails);

        return AuthResponseDto.builder().accessToken(jwt).build();

    }

}
