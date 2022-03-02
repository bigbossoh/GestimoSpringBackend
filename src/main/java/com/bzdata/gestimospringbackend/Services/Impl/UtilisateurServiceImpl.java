package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import static com.bzdata.gestimospringbackend.Utils.Constants.ACTIVATION_EMAIL;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    public final PasswordEncoder passwordEncoderUser;
    private final VerificationTokenRepository verificationTokenRepository;
    private  final RoleRepository roleRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    @Override
    public UtilisateurRequestDto saveLocataire(UtilisateurRequestDto dto) {
        log.info("We are going to create a new user with the role Locataire {}",dto);
        List<String> errors= UtilisateurDtoValiditor.validate(dto);
        if(!errors.isEmpty()){
            log.error("l'utilisateur n'est pas valide {}",errors);
            throw new InvalidEntityException("Certain attributs de l'object utiliateur avec pour role locataire sont null.",
                    ErrorCodes.UTILISATEUR_NOT_VALID,errors);
        }
        Optional<Utilisateur> utilisateurByEmail = utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
        if(!utilisateurByEmail.isPresent()) {
            // ROLES
            Optional<Role> roles = Optional.empty();
            roles = roleRepository.findRoleByRoleName("LOCATAIRE");
            dto.setRoleRequestDto(RoleRequestDto.fromEntity(roles.get()));
            dto.setPassword(passwordEncoderUser.encode(dto.getPassword()));
            //UTILISATEUR
            Optional<Utilisateur> user=Optional.empty();
            user=utilisateurRepository.findById(dto.getUserCreateDto().getId());
            dto.setUserCreateDto(UtilisateurRequestDto.fromEntity(user.get()));
            Utilisateur saveLocataire = utilisateurRepository.save(UtilisateurRequestDto.toEntity(dto));
            String token=generateVerificationToken(saveLocataire);
            String message=mailContentBuilder.build("Activez votre compte locataire enc cliquant sur le lien "
                     + ACTIVATION_EMAIL+"/"+token+"\n");
            mailService.sendMail(new NotificationEmail("Veuillez activer votre compte en cliquant sur ce lien: ",saveLocataire.getEmail(),message));
            return UtilisateurRequestDto.fromEntity(saveLocataire);
        }else{
            log.error("We cannot save this locataire because this user is already exist");
            return null;
        }
    }
    private String generateVerificationToken(Utilisateur utilisateur){
        String token= UUID.randomUUID().toString();
        VerificationToken verificationToken= new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUtilisateur(utilisateur);
        verificationTokenRepository.save(verificationToken);
        return token;
    }
    @Override
    public void verifyAccount(String token){
        Optional<VerificationToken> verificationTokenOptional=verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(()->new GestimoWebExceptionGlobal("Invalid Token"));
        feachUserAndEnable(verificationTokenOptional.get());
    }
    @Override
    public void feachUserAndEnable(VerificationToken verificationToken){
        String email=verificationToken.getUtilisateur().getEmail();
        Utilisateur utilisateur=utilisateurRepository.findUtilisateurByEmail(email).orElseThrow(()->
                new GestimoWebExceptionGlobal("Utilisateur avec l'username "+email+" n'exise pas."));
        utilisateur.setActivated(true);
        utilisateurRepository.save(utilisateur);

    }

    @Override
    public UtilisateurRequestDto saveProprietaire(UtilisateurRequestDto dto) {
        return null;
    }

    @Override
    public UtilisateurRequestDto saveGerant(UtilisateurRequestDto dto) {
        return null;
    }

    @Override
    public UtilisateurRequestDto findById(Long id) {
        return null;
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

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurOrderbyName() {
        return null;
    }

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurLocataireOrderbyName() {
        return null;
    }

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurProprietaireOrderbyName() {
        return null;
    }

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurGerantOrderbyName() {
        return null;
    }

    @Override
    public List<UtilisateurRequestDto> listOfAllUtilisateurSuperviseurOrderbyName() {
        return null;
    }

    @Override
    public void deleteLocatire(Long id) {

    }

    @Override
    public void deleteProprietaire(Long id) {

    }
}
