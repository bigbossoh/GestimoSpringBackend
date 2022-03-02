package com.bzdata.gestimospringbackend.Services.Impl;

import static com.bzdata.gestimospringbackend.Utils.Constants.ACTIVATION_EMAIL;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import com.bzdata.gestimospringbackend.Models.NotificationEmail;
import com.bzdata.gestimospringbackend.Models.Role;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Models.VerificationToken;
import com.bzdata.gestimospringbackend.Services.AgenceImmobilierService;
import com.bzdata.gestimospringbackend.exceptions.*;
import com.bzdata.gestimospringbackend.repository.AgenceImmobiliereRepository;
import com.bzdata.gestimospringbackend.repository.RoleRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.repository.VerificationTokenRepository;
import com.bzdata.gestimospringbackend.validator.AgenceDtoValidator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;


@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AgenceImmobiliereServiceImpl implements AgenceImmobilierService {

    private final AgenceImmobiliereRepository agenceImmobiliereRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private  final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    @Override
    public AgenceResponseDto save(AgenceRequestDto dto) {
        log.info("We are going to create  a new agence {}",dto);
        List<String> errors= AgenceDtoValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error("l'agence immobilière n'est pas valide {}",errors);
            throw new InvalidEntityException("Certain attributs de l'object agence immobiliere sont null.",
                    ErrorCodes.AGENCE_NOT_VALID,errors);
        }
        Optional<Utilisateur> utilisateurByEmail = utilisateurRepository.findUtilisateurByEmail(dto.getEmailAgence());
        if(!utilisateurByEmail.isPresent()) {
            AgenceImmobiliere saveAgence=agenceImmobiliereRepository.save(AgenceRequestDto.toEntity(dto));
            log.info("We are going to create  a new utilisateur gerant by the logged user {}",dto.getUtilisateurCreateur());
            Utilisateur newUtilisateur = new Utilisateur();
            newUtilisateur.setNom(dto.getNomAgence());
            newUtilisateur.setEmail(dto.getEmailAgence());
            newUtilisateur.setMobile(dto.getTelAgence());
            newUtilisateur.setPassword(passwordEncoder.encode("gerant"));
            newUtilisateur.setAgence(saveAgence);
            Optional<Role> newRole = roleRepository.findRoleByRoleName("GERANT");
            if (newRole.isPresent()) {
                newUtilisateur.setUrole(newRole.get());
            }
            newUtilisateur.setUserCreate(UtilisateurRequestDto.toEntity(dto.getUtilisateurCreateur()));
            Utilisateur saveUser = utilisateurRepository.save(newUtilisateur);
            String token=generateVerificationToken(saveUser);
            String message=mailContentBuilder.build("Merci de vous être enregistré a Gestimoweb, Cliquer sur le lien " +
                    "ci-dessous pour activer votre account: "+ ACTIVATION_EMAIL+"/"+token+"\n");
            mailService.sendMail(new NotificationEmail("Veuillez activer votre compte en cliquant sur ce lien: ",saveUser.getEmail(),message));
            return AgenceResponseDto.fromEntity(saveAgence);
        }

        else {
                log.error("This user is already exist");
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
    public AgenceResponseDto findAgenceById(Long id) {
        log.info("We are going to get back the Agence Immobilière en fonction de l'ID {} du bien", id);
        if(id==null){
            log.error("you are provided a null ID for the Agence");
            return null;
        }
        return agenceImmobiliereRepository.findById(id)
                .map(AgenceResponseDto::fromEntity)
                .orElseThrow(()->new InvalidEntityException("Aucune agence has been found with ID "+id,
                        ErrorCodes.AGENCE_NOT_FOUND));
    }

    @Override
    public List<AgenceResponseDto> listOfAgenceImmobilier() {

        log.info("We are going to take back all agences");

        return agenceImmobiliereRepository.findAll().stream()
                .map(AgenceResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<AgenceResponseDto> listOfAgenceOrderByNomAgenceAsc() {
        log.info("We are going to take back all the agences order by agence name");

        return agenceImmobiliereRepository.findAllByOrderByNomAgenceAsc().stream()
                .map(AgenceResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAgence(Long id) {
        log.info("We are going to delete a Agence with the ID {}", id);
        if (id==null){
            log.error("you are provided a null ID for the agence");
        }
        boolean exist=agenceImmobiliereRepository.existsById(id);
        if (!exist)
        {
            throw new EntityNotFoundException("Aucune Agence avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD",  ErrorCodes.AGENCE_NOT_FOUND);

        }
        List<Utilisateur> utilisateurs=utilisateurRepository.findAll();
        Stream<AgenceImmobiliere> agenceImmobiliereStream = utilisateurs.stream()
                .filter(user -> user.getUserCreate().getId() == id).map(Utilisateur::getAgence);
        if (agenceImmobiliereStream.findAny().isPresent()) {
            throw new InvalidOperationException("Impossible de supprimer une agence qui a des utilisateurs déjà crées",
                    ErrorCodes.AGENCE_ALREADY_IN_USE);
        }
        agenceImmobiliereRepository.deleteById(id);

    }

    @Override
    public AgenceResponseDto findAgenceByEmail(String email) {
        log.info("We are going to get back the Agence by email {}",email);
        if (!StringUtils.hasLength(email)){
            log.error("you are not provided a email  get back the Agence.");
            return  null;
        }
        return agenceImmobiliereRepository.findAgenceImmobiliereByEmailAgence(email)
                .map(AgenceResponseDto::fromEntity)
                .orElseThrow(()->new InvalidEntityException("Aucun bien immobilier has been found with Code "+email,
                        ErrorCodes.AGENCE_NOT_FOUND));
    }
}
