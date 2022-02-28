package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Services.UtilisateurService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
@Service
@Transactional
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    public final PasswordEncoder passwordEncoderUser;
    @Override
    public UtilisateurRequestDto save(UtilisateurRequestDto dto) {
        return null;
    }

    @Override
    public UtilisateurRequestDto findById(Long id) {
        return null;
    }

    @Override
    public List<UtilisateurRequestDto> listOfUtilisateur() {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public UtilisateurRequestDto findByEmail(String email) {
        return utilisateurRepository.findUtilisateurByEmail(email)
                .map(UtilisateurRequestDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'email = " + email + " n' ete trouve dans la BDD",
                        ErrorCodes.UTILISATEUR_NOT_FOUND)
                );
    }
}
