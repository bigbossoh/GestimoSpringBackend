package com.bzdata.gestimospringbackend.Services.Impl;

import static com.bzdata.gestimospringbackend.enumeration.Role.*;

import java.util.*;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.LocataireEncaisDTO;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.*;
import com.bzdata.gestimospringbackend.Services.UtilisateurService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.GestimoWebExceptionGlobal;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.mappers.BailMapperImpl;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
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
    // private final AgenceImmobiliereRepository agenceImmobiliereRepository;
    private final UtilisateurRepository utilisateurRepository;
    public final PasswordEncoder passwordEncoderUser;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final GestimoWebMapperImpl gestimoWebMapperImpl;
    private final BailLocationRepository bailrepository;
    private final BailMapperImpl bailMapper;

    @Override
    public UtilisateurAfficheDto saveUtilisateur(UtilisateurRequestDto dto) {
        if (dto.getId() == 0 || dto.getId() == null) {
            // log.info("We are going to create a new user with the role Locataire {}",
            // dto);
            Utilisateur newUser = new Utilisateur();
            List<String> errors = UtilisateurDtoValiditor.validate(dto);
            if (!errors.isEmpty()) {
                log.error("l'utilisateur n'est pas valide {}", errors);
                throw new InvalidEntityException(
                        "Certain attributs de l'object utiliateur avec pour role locataire sont null.",
                        ErrorCodes.UTILISATEUR_NOT_VALID, errors);
            }
            utilisateurRepository.findById(dto.getUserCreate()).orElseThrow(
                    () -> new InvalidEntityException(
                            "Aucun Createur has been found with Code " + dto.getUserCreate(),
                            ErrorCodes.UTILISATEUR_NOT_FOUND));
            // newUser.setUserCreate(userCreate);
            // GERER LES ROLES
            Role leRole = roleRepository.findRoleByRoleName(dto.getRoleUsed())
                    .orElseThrow(() -> new InvalidEntityException(
                            "Aucun role has been found with Code " + dto.getRoleUsed(),
                            ErrorCodes.ROLE_NOT_FOUND));

            newUser.setUrole(leRole);
            switch (leRole.getRoleName()) {
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
                case "CLIENT HOTEL":
                    newUser.setRoleUsed(ROLE_CLIENT_HOTEL.name());
                    newUser.setAuthorities(ROLE_CLIENT_HOTEL.getAuthorities());
                    break;
                default:
                    log.error(
                            "You should give a role in this list (superviseur, gerant, proprietaire,locataire) but in this cas the role is not wel given {}",
                            leRole.getRoleName());
                    break;
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
            newUser.setNationalite(dto.getNationalite());
            newUser.setLieuNaissance(dto.getLieuNaissance());
            newUser.setGenre(dto.getGenre());
            newUser.setDateFinPiece(dto.getDateFinPiece());
            newUser.setDateDeNaissance(dto.getDateDeNaissance());
            newUser.setDateDebutPiece(dto.getDateDebutPiece());
            newUser.setActive(true);
            newUser.setActivated(true);
            newUser.setNonLocked(true);
            newUser.setIdCreateur(dto.getIdCreateur());
            Utilisateur userSave = utilisateurRepository.save(newUser);
            return gestimoWebMapperImpl.fromUtilisateur(userSave);

        } else {
            // log.info("WE ARE GOING TO MAKE A UDPDATE OF utilisateur");
            Utilisateur utilisateurUpdate = utilisateurRepository.findById(dto.getId())
                    .orElseThrow(() -> new InvalidEntityException(
                            "Aucun Utlisateur has been found with id " + dto.getId(),
                            ErrorCodes.UTILISATEUR_NOT_FOUND));
            utilisateurUpdate.setIdAgence(dto.getIdAgence());
            utilisateurUpdate.setNom(dto.getNom());
            utilisateurUpdate.setUtilisateurIdApp(generateUserId());
            utilisateurUpdate.setPrenom(dto.getPrenom());
            utilisateurUpdate.setEmail(dto.getEmail());
            utilisateurUpdate.setUsername(dto.getMobile());
            utilisateurUpdate.setProfileImageUrl(dto.getProfileImageUrl());
            utilisateurUpdate.setTypePieceIdentite(dto.getTypePieceIdentite());
            utilisateurUpdate.setNumeroPieceIdentite(dto.getNumeroPieceIdentite());
            utilisateurUpdate.setNationalite(dto.getNationalite());
            utilisateurUpdate.setLieuNaissance(dto.getLieuNaissance());
            utilisateurUpdate.setGenre(dto.getGenre());
            utilisateurUpdate.setDateFinPiece(dto.getDateFinPiece());
            utilisateurUpdate.setDateDeNaissance(dto.getDateDeNaissance());
            utilisateurUpdate.setDateDebutPiece(dto.getDateDebutPiece());
            utilisateurUpdate.setActive(dto.isActive());
            utilisateurUpdate.setActivated(dto.isActivated());
            utilisateurUpdate.setNonLocked(dto.isNonLocked());
            Utilisateur UpdateUtilisateur = utilisateurRepository.save(utilisateurUpdate);
            return gestimoWebMapperImpl.fromUtilisateur(UpdateUtilisateur);
        }

    }

    private String generateUserId() {
        return "User-" + RandomStringUtils.randomAlphanumeric(5);
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
    public List<UtilisateurAfficheDto> listOfAllUtilisateurLocataireOrderbyNameByAgence(Long idAgence) {
        return utilisateurRepository.findAll().stream()
                .filter(user -> user.getUrole().getRoleName().equals("LOCATAIRE"))
                .filter(agence -> agence.getIdAgence().equals(idAgence))
                .sorted(Comparator.comparing(Utilisateur::getNom))
                .map(gestimoWebMapperImpl::fromUtilisateur)
                .collect(Collectors.toList());
    }

    @Override
    public UtilisateurRequestDto findById(Long id) {
        if (id == null) {
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
        // log.info("Le User est {}", utilisateurByUsername.getUsername());
        if (utilisateurByUsername != null) {
            return UtilisateurRequestDto.fromEntity(utilisateurByUsername);
        } else {
            return null;
        }
    }

    @Override
    public List<UtilisateurAfficheDto> listOfAllUtilisateurOrderbyName(Long idAgence) {
        // log.info("We are going to take back all the utilisateurs");

        return utilisateurRepository.findAll().stream()
                .sorted(Comparator.comparing(Utilisateur::getNom))
                .filter(agence -> agence.getIdAgence() == idAgence)
                .map(gestimoWebMapperImpl::fromUtilisateur)
                .collect(Collectors.toList());
    }

    @Override
    public List<UtilisateurAfficheDto> listOfAllUtilisateurLocataireOrderbyName(Long idAgence) {
        return utilisateurRepository.findAll().stream()
                .filter(agence -> agence.getIdAgence() == idAgence)
                .filter(user -> user.getUrole().getRoleName().equals("LOCATAIRE"))
                .sorted(Comparator.comparing(Utilisateur::getNom))
                .map(gestimoWebMapperImpl::fromUtilisateur)
                .collect(Collectors.toList());

    }

    @Override
    public List<UtilisateurAfficheDto> listOfAllUtilisateurProprietaireOrderbyName(Long idAgence) {
              return utilisateurRepository.findAll().stream()
                .filter(agence -> agence.getIdAgence() == idAgence)
                .filter(user -> user.getUrole().getRoleName().equals("PROPRIETAIRE"))
                .sorted(Comparator.comparing(Utilisateur::getNom))
                .map(gestimoWebMapperImpl::fromUtilisateur)
                .collect(Collectors.toList());
    }

    @Override
    public List<UtilisateurAfficheDto> listOfAllUtilisateurGerantOrderbyName(Long idAgence) {
        // log.info("We are going to take back all the GERANT order by GERANT name");

        return utilisateurRepository.findAll().stream()
                .filter(agence -> agence.getIdAgence() == idAgence)
                .filter(user -> user.getUrole().getRoleName().equals("GERANT"))
                .sorted(Comparator.comparing(Utilisateur::getNom))
                .map(gestimoWebMapperImpl::fromUtilisateur)
                .collect(Collectors.toList());
    }

    @Override
    public List<UtilisateurAfficheDto> listOfAllUtilisateurSuperviseurOrderbyName() {
        // log.info("We are going to take back all the SUPERVISEUR order by SUPERVISEUR
        // name");

        return utilisateurRepository.findAllByOrderByNomAsc().stream()
                .filter(user -> user.getUrole().getRoleName().equals("SUPERVISEUR"))
                .map(gestimoWebMapperImpl::fromUtilisateur)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLocatire(Long id) {

    }

    @Override
    public void deleteProprietaire(Long id) {

    }

    @Override
    public List<LocataireEncaisDTO> listOfLocataireAyantunbail(Long idAgence) {

        return bailrepository.findAll().stream()
                .filter(agence -> agence.getIdAgence() == idAgence)
                .filter(bailActif -> bailActif.isEnCoursBail() == true)
                
                .map(bailMapper::fromOperationBailLocation)
                .sorted(Comparator.comparing(LocataireEncaisDTO::getCodeDescBail))
                .collect(Collectors.toList());
    }

}
