package com.bzdata.gestimospringbackend.Services.Auth;

import java.util.ArrayList;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.auth.ExtentedUser;
import com.bzdata.gestimospringbackend.Services.UtilisateurService;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UtilisateurService service;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UtilisateurRequestDto utilisateurDto=service.findUtilisateurByEmail(email);
        List<SimpleGrantedAuthority> authorities=new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(utilisateurDto.getRoleRequestDto().getRoleName());
        authorities.add(authority);
        return new ExtentedUser(utilisateurDto.getEmail(),utilisateurDto.getPassword(), authorities);
    }
}
