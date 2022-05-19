package com.bzdata.gestimospringbackend.Services.Auth;

import static com.bzdata.gestimospringbackend.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

import java.util.Date;

import com.bzdata.gestimospringbackend.DTOs.RoleRequestDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import com.bzdata.gestimospringbackend.Models.Role;
import com.bzdata.gestimospringbackend.Models.UserPrincipal;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.LoginAttemptService;
import com.bzdata.gestimospringbackend.Services.UtilisateurService;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.AgenceImmobiliereRepository;
import com.bzdata.gestimospringbackend.repository.RoleRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {
    private final UtilisateurService service;
    private final UtilisateurRepository utilisateurRepository;
    private final LoginAttemptService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(username);
        UtilisateurRequestDto utilisateurDto = service.findUtilisateurByUsername(username);
        Utilisateur utilisateur = UtilisateurRequestDto.toEntity(utilisateurDto);
        if (utilisateur == null) {
            // log.error(NO_USER_FOUND_BY_USERNAME +username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(utilisateur);
            utilisateur.setLastLoginDateDisplay(utilisateur.getLastLoginDate());
            utilisateur.setLastLoginDate(new Date());
            utilisateurRepository.save(utilisateur);
            UserPrincipal userPrincipal = new UserPrincipal(utilisateur);
            log.info("Returning found user by username {} , and userPrincipal {}", username,
                    userPrincipal.getUsername());
            return userPrincipal;
        }
    }

    private void validateLoginAttempt(Utilisateur user) {
        if (user.isNonLocked()) {
            if (loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setNonLocked(false);
            } else {
                user.setNonLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }
}