package com.bzdata.gestimospringbackend.Services.Impl;

import static com.bzdata.gestimospringbackend.enumeration.Role.ROLE_GERANT;
import static com.bzdata.gestimospringbackend.enumeration.Role.ROLE_LOCATAIRE;
import static com.bzdata.gestimospringbackend.enumeration.Role.ROLE_PROPRIETAIRE;
import static com.bzdata.gestimospringbackend.enumeration.Role.ROLE_SUPERVISEUR;

import java.util.*;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.*;
import com.bzdata.gestimospringbackend.Services.UtilisateurService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.GestimoWebExceptionGlobal;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.AgenceImmobiliereRepository;
import com.bzdata.gestimospringbackend.repository.RoleRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.repository.VerificationTokenRepository;
import com.bzdata.gestimospringbackend.validator.UtilisateurDtoValiditor;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {
    private final AgenceImmobiliereRepository agenceImmobiliereRepository;
    private final UtilisateurRepository utilisateurRepository;
    public final PasswordEncoder passwordEncoderUser;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean saveUtilisateur(UtilisateurRequestDto dto) {
        log.info("We are going to create a new user with the role Locataire {}",
                dto);
        Utilisateur newUser = new Utilisateur();
        List<String> errors = UtilisateurDtoValiditor.validate(dto);
        if (!errors.isEmpty()) {
            log.error("l'utilisateur n'est pas valide {}", errors);
            throw new InvalidEntityException(
                    "Certain attributs de l'object utiliateur avec pour role locataire sont null.",
                    ErrorCodes.UTILISATEUR_NOT_VALID, errors);
        }
        try {

            Optional<Utilisateur> userExiste = utilisateurRepository.findById(dto.getId());
            if (userExiste.isPresent()) {
                newUser.setId(userExiste.get().getId());
            }

            Utilisateur userCreate = utilisateurRepository.findById(dto.getUserCreate()).orElseThrow(
                    () -> new InvalidEntityException(
                            "Aucun Utilisateur has been found with Code " + dto.getUserCreate(),
                            ErrorCodes.UTILISATEUR_NOT_FOUND));
            newUser.setUserCreate(userCreate);
            // GERER LES ROLES

            Optional<Role> leRole = roleRepository.findRoleByRoleName(dto.getRoleUsed());
            if (leRole.isPresent()) {
                newUser.setUrole(leRole.get());
                switch (leRole.get().getRoleName()) {
                    case "SUPERVISEUR":
                        newUser.setRoleUsed(ROLE_SUPERVISEUR.name());
                        newUser.setAuthorities(ROLE_SUPERVISEUR.getAuthorities());
                        break;
                    case "GERANT":
                        newUser.setRoleUsed(ROLE_GERANT.name());
                        newUser.setAuthorities(ROLE_GERANT.getAuthorities());
                        break;
                    case "PROPRIETAIRE":
                        newUser.setRoleUsed(ROLE_PROPRIETAIRE.name());
                        newUser.setAuthorities(ROLE_PROPRIETAIRE.getAuthorities());
                        break;
                    case "LOCATAIRE":
                        newUser.setRoleUsed(ROLE_LOCATAIRE.name());
                        newUser.setAuthorities(ROLE_LOCATAIRE.getAuthorities());
                        break;
                    default:
                        log.error(
                                "You should give a role in this list (superviseur, gerant, proprietaire,locataire) but in this cas the role is not wel given {}",
                                leRole.get().getRoleName());
                        break;
                }
            }

            Optional<AgenceImmobiliere> agenceImmobiliere = agenceImmobiliereRepository.findById(dto.getAgenceDto());
            if (agenceImmobiliere.isPresent()) {
               // newUser.setAgenceImmobilier(agenceImmobiliere.get());
            }

            newUser.setIdAgence(dto.getIdAgence());
            newUser.setNom(dto.getNom());
            newUser.setUtilisateurIdApp(generateUserId());
            newUser.setPrenom(dto.getPrenom());
            newUser.setEmail(dto.getEmail());
            newUser.setMobile(dto.getMobile());
            newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            newUser.setUsername(dto.getMobile());
            newUser.setProfileImageUrl(dto.getProfileImageUrl());
            newUser.setTypePieceIdentite(dto.getTypePieceIdentite());
            newUser.setJoinDate(new Date());
            newUser.setNumeroPieceIdentite(dto.getNumeroPieceIdentite());
            newUser.setNationalité(dto.getNationalité());
            newUser.setLieuNaissance(dto.getLieuNaissance());
            newUser.setGenre(dto.getGenre());
            newUser.setDateFinPiece(dto.getDateFinPiece());
            newUser.setDateDeNaissance(dto.getDateDeNaissance());
            newUser.setDateDebutPiece(dto.getDateDebutPiece());
            newUser.setActive(true);
            newUser.setActivated(true);
            newUser.setNonLocked(true);
            newUser.setIdCreateur(dto.getIdCreateur());
            utilisateurRepository.save(newUser);
            return true;
        } catch (Exception e) {
            log.error("Save Error", e.getMessage());
            throw new EntityNotFoundException("Error in Saving " +
                    e.getMessage(),
                    ErrorCodes.UTILISATEUR_NOT_VALID);
        }

    }

    private String generateUserId() {
        return "User-"+RandomStringUtils.randomAlphanumeric(5);
    }


    private String generateVerificationToken(Utilisateur utilisateur) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUtilisateur(utilisateur);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    @Override
    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new GestimoWebExceptionGlobal("Invalid Token"));
        feachUserAndEnable(verificationTokenOptional.get());
    }

    @Override
    public void feachUserAndEnable(VerificationToken verificationToken) {
        String email = verificationToken.getUtilisateur().getEmail();
        Utilisateur utilisateur = utilisateurRepository.findUtilisateurByEmail(email).orElseThrow(
                () -> new GestimoWebExceptionGlobal("Utilisateur avec l'username " + email + " n'exise pas."));
        utilisateur.setActivated(true);
        utilisateurRepository.save(utilisateur);

    }


    @Override
    public UtilisateurRequestDto findById(Long id) {
        log.info("We are going to get back the utilisateur en fonction de l'ID {} de l'utilisateur ", id);
        if (id == null) {
            log.error("you are provided a null ID for the user");
            return null;
        }
        return utilisateurRepository.findById(id)
                .map(UtilisateurRequestDto::fromEntity)
                .orElseThrow(() -> new InvalidEntityException("Aucun utilisateur has been found with ID " + id,
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
    }

    @Override
    public UtilisateurRequestDto findUtilisateurByEmail(String email) {
        return utilisateurRepository.findUtilisateurByEmail(email)

                .map(UtilisateurRequestDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'email = " + email + " n' ete trouve dans la BDD",
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
    }

    @Override
    public UtilisateurRequestDto findUtilisateurByUsername(String username) {
        Utilisateur utilisateurByUsername = utilisateurRepository.findUtilisateurByUsername(username);
        if (utilisateurByUsername != null) {
            return UtilisateurRequestDto.fromEntity(utilisateurByUsername);
        } else {
            return null;
        }
    }

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurOrderbyName() {
        log.info("We are going to take back all the utilisateurs");

        return utilisateurRepository.findAll().stream()
                .sorted(Comparator.comparing(Utilisateur::getNom))
                .map(UtilisateurRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurLocataireOrderbyName() {
        log.info("We are going to take back all the locataires order by locataires name");

        return utilisateurRepository.findAll().stream()
                .filter(user -> user.getUrole().getRoleName().equals("LOCATAIRE"))
                .sorted(Comparator.comparing(Utilisateur::getNom))
                .map(UtilisateurRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurProprietaireOrderbyName() {
        log.info("We are going to take back all the PROPRIETAIRE order by PROPRIETAIRE name");

        return utilisateurRepository.findAll().stream()
                .filter(user -> user.getUrole().getRoleName().equals("PROPRIETAIRE"))
                .sorted(Comparator.comparing(Utilisateur::getNom))
                .map(UtilisateurRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurGerantOrderbyName() {
        log.info("We are going to take back all the GERANT order by GERANT name");

        return utilisateurRepository.findAll().stream()
                .filter(user -> user.getUrole().getRoleName().equals("GERANT"))
                .sorted(Comparator.comparing(Utilisateur::getNom))
                .map(UtilisateurRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurSuperviseurOrderbyName() {
        log.info("We are going to take back all the SUPERVISEUR order by SUPERVISEUR name");

        return utilisateurRepository.findAllByOrderByNomAsc().stream()
                .filter(user -> user.getUrole().getRoleName().equals("SUPERVISEUR"))
                .map(UtilisateurRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLocatire(Long id) {

    }

    @Override
    public void deleteProprietaire(Long id) {

    }

}
