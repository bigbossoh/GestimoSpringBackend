package com.bzdata.gestimospringbackend.Services.Impl;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.ACTIVATION_EMAIL;
import static com.bzdata.gestimospringbackend.enumeration.Role.ROLE_GERANT;

import java.util.Date;
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
import com.bzdata.gestimospringbackend.Services.UtilisateurService;
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
    private  final UtilisateurService utilisateurService;
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

        Utilisateur utilisateurByEmail = utilisateurRepository.findUtilisateurByUsername(dto.getMobileAgence());
        if(utilisateurByEmail ==null) {
            UtilisateurRequestDto utilisateurSuperviseur=utilisateurService.findById(dto.getUtilisateurCreateur().getId());
            dto.setUtilisateurCreateur(utilisateurSuperviseur);
            AgenceImmobiliere saveAgence=agenceImmobiliereRepository.save(AgenceRequestDto.toEntity(dto));
            saveAgence.setIdAgence(saveAgence.getId());
            AgenceRequestDto agenceRequestDto = AgenceRequestDto.fromEntity(saveAgence);
            AgenceImmobiliere saveAgenceUpdate=agenceImmobiliereRepository.save(AgenceRequestDto.toEntity(agenceRequestDto));
            log.info("We are going to create  a new utilisateur gerant by the logged user {}",dto.getUtilisateurCreateur());
            Utilisateur newUtilisateur = new Utilisateur();
            newUtilisateur.setIdAgence(saveAgenceUpdate.getIdAgence());
            newUtilisateur.setNom(dto.getNomAgence());
            newUtilisateur.setPrenom(dto.getNomAgence());
            newUtilisateur.setEmail(dto.getEmailAgence());
            newUtilisateur.setMobile(dto.getMobileAgence());
            newUtilisateur.setPassword(passwordEncoder.encode("gerant"));
            newUtilisateur.setAgence(saveAgenceUpdate);
            Optional<Role> newRole = roleRepository.findRoleByRoleName("GERANT");
            if (newRole.isPresent()) {
                newUtilisateur.setUrole(newRole.get());
            }
            newUtilisateur.setJoinDate(new Date());
            newUtilisateur.setRoleUsed(ROLE_GERANT.name());
            newUtilisateur.setAuthorities(ROLE_GERANT.getAuthorities());
            newUtilisateur.setActive(false);
            newUtilisateur.setActivated(false);
            newUtilisateur.setUsername(dto.getMobileAgence());
            newUtilisateur.setNonLocked(true);
            newUtilisateur.setUserCreate(UtilisateurRequestDto.toEntity(dto.getUtilisateurCreateur()));
            Utilisateur saveUser = utilisateurRepository.save(newUtilisateur);
            String token=generateVerificationToken(saveUser);
            String message=mailContentBuilder.build("Merci de vous être enregistré a Gestimoweb, Cliquer sur le lien " +
                    "ci-dessous pour activer votre account: "+ ACTIVATION_EMAIL+"/"+token+"\n");
            mailService.sendMail(new NotificationEmail("Veuillez activer votre compte en cliquant sur ce lien: ",saveUser.getEmail(),message));
            log.info("We are same a gerant user and Agence also !!!");
            return AgenceResponseDto.fromEntity(saveAgence);
        }

        else {
                log.error("This user is already exist");
            throw new EntityNotFoundException("The email is already exist in db "+dto.getEmailAgence(),
                    ErrorCodes.UTILISATEUR_ALREADY_IN_USE);
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
        String username=verificationToken.getUtilisateur().getUsername();
        Utilisateur utilisateur=utilisateurRepository.findUtilisateurByUsername(username);
        if(utilisateur !=null) {
            utilisateur.setActivated(true);
            utilisateur.setActive(true);
            utilisateurRepository.save(utilisateur);
        }else{
           throw new GestimoWebExceptionGlobal("Utilisateur avec l'username "+username+" n'exise pas.");
        }


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
                .filter(user -> user.getUserCreate().getId().equals(id) ).map(Utilisateur::getAgence);
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
