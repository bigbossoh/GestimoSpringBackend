package com.bzdata.gestimospringbackend.Services.Impl;

import static com.bzdata.gestimospringbackend.Utils.Constants.ACTIVATION_EMAIL;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.RoleRequestDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.NotificationEmail;
import com.bzdata.gestimospringbackend.Models.Role;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Models.VerificationToken;
import com.bzdata.gestimospringbackend.Services.UtilisateurService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.GestimoWebExceptionGlobal;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.RoleRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.repository.VerificationTokenRepository;
import com.bzdata.gestimospringbackend.validator.UtilisateurDtoValiditor;

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

    private final UtilisateurRepository utilisateurRepository;
    public final PasswordEncoder passwordEncoderUser;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RoleRepository roleRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    @Override
    public UtilisateurRequestDto saveLocataire(UtilisateurRequestDto dto) {
        log.info("We are going to create a new user with the role Locataire {}", dto);
        List<String> errors = UtilisateurDtoValiditor.validate(dto);
        if (!errors.isEmpty()) {
            log.error("l'utilisateur n'est pas valide {}", errors);
            throw new InvalidEntityException(
                    "Certain attributs de l'object utiliateur avec pour role locataire sont null.",
                    ErrorCodes.UTILISATEUR_NOT_VALID, errors);
        }
        Optional<Utilisateur> utilisateurByEmail = utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
        if (!utilisateurByEmail.isPresent()) {
            // ROLES
            Optional<Role> roles = Optional.empty();
            roles = roleRepository.findRoleByRoleName("LOCATAIRE");
            dto.setRoleRequestDto(RoleRequestDto.fromEntity(roles.get()));
            dto.setPassword(passwordEncoderUser.encode(dto.getPassword()));
            // UTILISATEUR
            Optional<Utilisateur> user = Optional.empty();
            user = utilisateurRepository.findById(dto.getUserCreateDto().getId());
            dto.setUserCreateDto(UtilisateurRequestDto.fromEntity(user.get()));
            Utilisateur saveLocataire = utilisateurRepository.save(UtilisateurRequestDto.toEntity(dto));
            String token = generateVerificationToken(saveLocataire);
            String message = mailContentBuilder.build("Activez votre compte locataire enc cliquant sur le lien "
                    + ACTIVATION_EMAIL + "/" + token + "\n");
            mailService.sendMail(new NotificationEmail("Veuillez activer votre compte en cliquant sur ce lien: ",
                    saveLocataire.getEmail(), message));
            return UtilisateurRequestDto.fromEntity(saveLocataire);
        } else {
            log.error("We cannot save this locataire because this user is already exist");
            return null;
        }
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
    public UtilisateurRequestDto saveProprietaire(UtilisateurRequestDto dto) {

        log.info("We are going to create a new user with the role Proprietaire {}", dto);
        List<String> errors = UtilisateurDtoValiditor.validate(dto);
        if (!errors.isEmpty()) {
            log.error("l'utilisateur n'est pas valide {}", errors);
            throw new InvalidEntityException(
                    "Certain attributs de l'object utiliateur avec pour role proprietaire sont null.",
                    ErrorCodes.UTILISATEUR_NOT_VALID, errors);
        }
        Optional<Utilisateur> utilisateurByEmail = utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
        if (!utilisateurByEmail.isPresent()) {
            // ROLES
            Optional<Role> roles = Optional.empty();
            roles = roleRepository.findRoleByRoleName("PROPRIETAIRE");
            dto.setRoleRequestDto(RoleRequestDto.fromEntity(roles.get()));
            dto.setPassword(passwordEncoderUser.encode(dto.getPassword()));
            // UTILISATEUR
            Optional<Utilisateur> user = Optional.empty();
            user = utilisateurRepository.findById(dto.getUserCreateDto().getId());
            dto.setUserCreateDto(UtilisateurRequestDto.fromEntity(user.get()));
            Utilisateur saveProprietaire = utilisateurRepository.save(UtilisateurRequestDto.toEntity(dto));
            String token = generateVerificationToken(saveProprietaire);
            String message = mailContentBuilder.build("Activez votre compte propriétaire enc cliquant sur le lien "
                    + ACTIVATION_EMAIL + "/" + token + "\n");
            mailService.sendMail(new NotificationEmail("Veuillez activer votre compte en cliquant sur ce lien: ",
                    saveProprietaire.getEmail(), message));
            return UtilisateurRequestDto.fromEntity(saveProprietaire);
        } else {
            log.error("We cannot save this propriétaire because this user is already exist");
            return null;
        }
    }

    @Override
    public UtilisateurRequestDto saveGerant(UtilisateurRequestDto dto) {
        log.info("We are going to create a new user with the role gérant {}", dto);
        List<String> errors = UtilisateurDtoValiditor.validate(dto);
        if (!errors.isEmpty()) {
            log.error("l'utilisateur n'est pas valide {}", errors);
            throw new InvalidEntityException(
                    "Certain attributs de l'object utiliateur avec pour role gérant sont null.",
                    ErrorCodes.UTILISATEUR_NOT_VALID, errors);
        }
        Optional<Utilisateur> utilisateurByEmail = utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
        if (!utilisateurByEmail.isPresent()) {
            // ROLES
            Optional<Role> roles = Optional.empty();
            roles = roleRepository.findRoleByRoleName("GERANT");
            dto.setRoleRequestDto(RoleRequestDto.fromEntity(roles.get()));
            dto.setPassword(passwordEncoderUser.encode(dto.getPassword()));
            // UTILISATEUR
            Optional<Utilisateur> user = Optional.empty();
            user = utilisateurRepository.findById(dto.getUserCreateDto().getId());
            dto.setUserCreateDto(UtilisateurRequestDto.fromEntity(user.get()));
            Utilisateur saveGerant = utilisateurRepository.save(UtilisateurRequestDto.toEntity(dto));
            String token = generateVerificationToken(saveGerant);
            String message = mailContentBuilder.build("Activez votre compte gérant enc cliquant sur le lien "
                    + ACTIVATION_EMAIL + "/" + token + "\n");
            mailService.sendMail(new NotificationEmail("Veuillez activer votre compte en cliquant sur ce lien: ",
                    saveGerant.getEmail(), message));
            return UtilisateurRequestDto.fromEntity(saveGerant);
        } else {
            log.error("We cannot save this gerant because this user is already exist");
            return null;
        }
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
    public UtilisateurRequestDto findByEmail(String email) {
        return utilisateurRepository.findUtilisateurByEmail(email)
                .map(UtilisateurRequestDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'email = " + email + " n' ete trouve dans la BDD",
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
    }

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurOrderbyName() {
        log.info("We are going to take back all the utilisateur order by utilisateur name");

        return utilisateurRepository.findAllByOrderByNomAsc().stream()
                .map(UtilisateurRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurLocataireOrderbyName() {
        log.info("We are going to take back all the locataires order by locataires name");

        return utilisateurRepository.findAllByOrderByNomAsc().stream()
                .filter(user -> user.getUrole().getRoleName().equals("LOCATAIRE"))
                .map(UtilisateurRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurProprietaireOrderbyName() {
        log.info("We are going to take back all the PROPRIETAIRE order by PROPRIETAIRE name");

        return utilisateurRepository.findAllByOrderByNomAsc().stream()
                .filter(user -> user.getUrole().getRoleName().equals("PROPRIETAIRE"))
                .map(UtilisateurRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurGerantOrderbyName() {
        log.info("We are going to take back all the GERANT order by GERANT name");

        return utilisateurRepository.findAllByOrderByNomAsc().stream()
                .filter(user -> user.getUrole().getRoleName().equals("GERANT")).map(UtilisateurRequestDto::fromEntity)
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
