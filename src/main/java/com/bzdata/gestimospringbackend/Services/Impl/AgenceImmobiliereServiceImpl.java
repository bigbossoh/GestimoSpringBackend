package com.bzdata.gestimospringbackend.Services.Impl;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.ACTIVATION_EMAIL;
import static com.bzdata.gestimospringbackend.enumeration.Role.ROLE_GERANT;

import com.bzdata.gestimospringbackend.DTOs.AgenceImmobilierDTO;
import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.DTOs.ImageLogoDto;
import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import com.bzdata.gestimospringbackend.Models.Etablissement;
import com.bzdata.gestimospringbackend.Models.EtablissementUtilisateur;
import com.bzdata.gestimospringbackend.Models.ImageModel;
import com.bzdata.gestimospringbackend.Models.NotificationEmail;
import com.bzdata.gestimospringbackend.Models.Role;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Models.VerificationToken;
import com.bzdata.gestimospringbackend.Services.AgenceImmobilierService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.GestimoWebExceptionGlobal;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.exceptions.InvalidOperationException;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.AgenceImmobiliereRepository;
import com.bzdata.gestimospringbackend.repository.EtablissementRepository;
import com.bzdata.gestimospringbackend.repository.EtablissementUtilisteurRepository;
import com.bzdata.gestimospringbackend.repository.ImageRepository;
import com.bzdata.gestimospringbackend.repository.RoleRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.repository.VerificationTokenRepository;
import com.bzdata.gestimospringbackend.validator.AgenceDtoValidator;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AgenceImmobiliereServiceImpl implements AgenceImmobilierService {

  private final AgenceImmobiliereRepository agenceImmobiliereRepository;
  private final UtilisateurRepository utilisateurRepository;
  private final VerificationTokenRepository verificationTokenRepository;
  private final RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;
  private final MailContentBuilder mailContentBuilder;
  private final MailService mailService;
  private GestimoWebMapperImpl gestimoWebMapperImpl;
  private final ImageRepository imageRepository;

  private final EtablissementRepository etablissementRepository;
  private final EtablissementUtilisteurRepository etablissementUtilisteurRepository;

  @Override
  public boolean save(AgenceRequestDto dto) {
    AgenceImmobiliere agenceImmobiliere = new AgenceImmobiliere();
 
    List<String> errors = AgenceDtoValidator.validate(dto);
      log.error("l'agence immobilière ETABLISSEMENT {}", dto.getIdEtable());
    if (!errors.isEmpty()) {
      log.error("l'agence immobilière n'est pas valide {}", errors);
      throw new InvalidEntityException(
        "Certain attributs de l'object agence immobiliere sont null.",
        ErrorCodes.AGENCE_NOT_VALID,
        errors
      );
    }

    // Check if the user already exist in the database
    Utilisateur utilisateurByMobile = utilisateurRepository.findUtilisateurByUsername(
      dto.getMobileAgence()
    );
    if (utilisateurByMobile == null) {
      // get back the connected user
      getUserCreate(dto);
      // agenceImmobiliere.setCreateur(userCreate);
      agenceImmobiliere.setSigleAgence(dto.getSigleAgence());
      agenceImmobiliere.setCapital(dto.getCapital());
      agenceImmobiliere.setCompteContribuable(dto.getCompteContribuable());
      agenceImmobiliere.setEmailAgence(dto.getEmailAgence());
      agenceImmobiliere.setFaxAgence(dto.getFaxAgence());
      agenceImmobiliere.setMobileAgence(dto.getMobileAgence());
      agenceImmobiliere.setNomAgence(dto.getNomAgence());
      agenceImmobiliere.setRegimeFiscaleAgence(dto.getRegimeFiscaleAgence());
      agenceImmobiliere.setTelAgence(dto.getTelAgence());

      AgenceImmobiliere saveAgence = agenceImmobiliereRepository.save(
        agenceImmobiliere
      );
      saveAgence.setIdAgence(saveAgence.getId());
      // AgenceRequestDto agenceRequestDto = AgenceRequestDto.fromEntity(saveAgence);
      AgenceImmobiliere saveAgenceUpdate = agenceImmobiliereRepository.save(
        saveAgence
      );
      Utilisateur newUtilisateur = new Utilisateur();
      newUtilisateur.setIdAgence(saveAgenceUpdate.getId());
      newUtilisateur.setNom(dto.getNomPrenomGerant());
      // newUtilisateur.setPrenom(dto.getNomAgence());
      newUtilisateur.setEmail(dto.getEmailAgence());
      newUtilisateur.setMobile(dto.getMobileAgence());
      newUtilisateur.setPassword(passwordEncoder.encode(dto.getMotdepasse()));
      // newUtilisateur.setAgenceImmobilier(saveAgenceUpdate);
      Optional<Role> newRole = roleRepository.findRoleByRoleName("GERANT");
      if (newRole.isPresent()) {
        newUtilisateur.setUrole(newRole.get());
      }
      newUtilisateur.setUtilisateurIdApp(generateUserId());
      newUtilisateur.setJoinDate(new Date());
      newUtilisateur.setRoleUsed(ROLE_GERANT.name());
      newUtilisateur.setAuthorities(ROLE_GERANT.getAuthorities());
      newUtilisateur.setActive(dto.isActive());
      newUtilisateur.setActivated(true);
      newUtilisateur.setUsername(dto.getMobileAgence());
      newUtilisateur.setNonLocked(true);
      // newUtilisateur.setUserCreate(userCreate);
     Utilisateur saveUser = utilisateurRepository.save(newUtilisateur);
  
      // String token = generateVerificationToken(saveUser);
      // String message = mailContentBuilder.build(
      //   "Merci de vous être enregistré a Gestimoweb, Cliquer sur le lien " +
      //   "ci-dessous pour activer votre account: " +
      //   ACTIVATION_EMAIL +
      //   "/" +
      //   token +
      //   "\n"
      // );
      // mailService.sendMail(
      //   new NotificationEmail(
      //     "Veuillez activer votre compte en cliquant sur ce lien: ",
      //     saveUser.getEmail(),
      //     message
      //   )
      // );

      return true;
    } else {
      log.error("This user is already exist");
      throw new EntityNotFoundException(
        "The username or mobile is already exist in db " +
        dto.getMobileAgence(),
        ErrorCodes.UTILISATEUR_ALREADY_IN_USE
      );
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
    Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(
      token
    );
    verificationTokenOptional.orElseThrow(() ->
      new GestimoWebExceptionGlobal("Invalid Token")
    );
    feachUserAndEnable(verificationTokenOptional.get());
  }

  @Override
  public void feachUserAndEnable(VerificationToken verificationToken) {
    String username = verificationToken.getUtilisateur().getUsername();
    Utilisateur utilisateur = utilisateurRepository.findUtilisateurByUsername(
      username
    );
    if (utilisateur != null) {
      utilisateur.setActivated(true);
      utilisateur.setActive(true);
      utilisateurRepository.save(utilisateur);
    } else {
      throw new GestimoWebExceptionGlobal(
        "Utilisateur avec l'username " + username + " n'exise pas."
      );
    }
  }

  @Override
  public AgenceResponseDto findAgenceById(Long id) {
    if (id == null) {
      log.error("you are provided a null ID for the Agence");
      return null;
    }
    return agenceImmobiliereRepository
      .findById(id)
      .map(AgenceResponseDto::fromEntity)
      .orElseThrow(() ->
        new InvalidEntityException(
          "Aucune agence has been found with ID " + id,
          ErrorCodes.AGENCE_NOT_FOUND
        )
      );
  }

  @Override
  public List<AgenceImmobilierDTO> listOfAgenceImmobilier() {
    return agenceImmobiliereRepository
      .findAll()
      .stream()
      .map(gestimoWebMapperImpl::fromAgenceImmobilier)
      .distinct()
      .collect(Collectors.toList());
  }

  @Override
  public List<AgenceImmobilierDTO> listOfAgenceOrderByNomAgenceAsc(
    Long idAgence
  ) {
    return agenceImmobiliereRepository
      .findAll()
      .stream()
      .filter(ag -> ag.getId() == idAgence)
      .sorted(Comparator.comparing(AgenceImmobiliere::getNomAgence))
      .map(gestimoWebMapperImpl::fromAgenceImmobilier)
      .distinct()
      .collect(Collectors.toList());
  }

  @Override
  public void deleteAgence(Long id) {
    if (id == null) {
      log.error("you are provided a null ID for the agence");
    }
    boolean exist = agenceImmobiliereRepository.existsById(id);
    if (!exist) {
      throw new EntityNotFoundException(
        "Aucune Agence avec l'ID = " + id + " " + "n' ete trouve dans la BDD",
        ErrorCodes.AGENCE_NOT_FOUND
      );
    }
    List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
    Stream<Long> agenceImmobiliereStream = utilisateurs
      .stream()
      .filter(user -> user.getIdCreateur().equals(id))
      .map(Utilisateur::getIdAgence);
    if (agenceImmobiliereStream.findAny().isPresent()) {
      throw new InvalidOperationException(
        "Impossible de supprimer une agence qui a des utilisateurs déjà crées",
        ErrorCodes.AGENCE_ALREADY_IN_USE
      );
    }
    agenceImmobiliereRepository.deleteById(id);
  }

  @Override
  public AgenceImmobilierDTO findAgenceByEmail(String email) {
    if (!StringUtils.hasLength(email)) {
      log.error("you are not provided a email  get back the Agence.");
      return null;
    }
    return agenceImmobiliereRepository
      .findAgenceImmobiliereByEmailAgence(email)
      .map(gestimoWebMapperImpl::fromAgenceImmobilier)
      .orElseThrow(() ->
        new InvalidEntityException(
          "Aucun bien immobilier has been found with Code " + email,
          ErrorCodes.AGENCE_NOT_FOUND
        )
      );
  }

  @Override
  public AgenceImmobilierDTO saveUneAgence(AgenceRequestDto dto) {
     log.error("###################### AGENCE SAVE ########");
    if (dto.getId() == 0 || dto.getId() == null) {
        log.error("###################### AGENCE SAVE NOT FOUND #### ########");
      AgenceImmobiliere agenceImmobiliere = new AgenceImmobiliere();

      List<String> errors = AgenceDtoValidator.validate(dto);
      if (!errors.isEmpty()) {
        log.error("l'agence immobilière n'est pas valide {}", errors);
        throw new InvalidEntityException(
          "Certain attributs de l'object agence immobiliere sont null.",
          ErrorCodes.AGENCE_NOT_VALID,
          errors
        );
      }
      // Check if the user already exist in the database
      Utilisateur utilisateurByMobile = utilisateurRepository.findUtilisateurByUsername(
        dto.getMobileAgence()
      );
      if (utilisateurByMobile == null) {
        // agenceImmobiliere.setCreateur(userCreate);
        agenceImmobiliere.setSigleAgence(dto.getSigleAgence());
        agenceImmobiliere.setCapital(dto.getCapital());
        agenceImmobiliere.setCompteContribuable(dto.getCompteContribuable());
        agenceImmobiliere.setEmailAgence(dto.getEmailAgence());
        agenceImmobiliere.setFaxAgence(dto.getFaxAgence());
        agenceImmobiliere.setMobileAgence(dto.getMobileAgence());
        agenceImmobiliere.setNomAgence(dto.getNomAgence());
        agenceImmobiliere.setRegimeFiscaleAgence(dto.getRegimeFiscaleAgence());
        agenceImmobiliere.setTelAgence(dto.getTelAgence());

        AgenceImmobiliere saveAgence = agenceImmobiliereRepository.save(
          agenceImmobiliere
        );
        saveAgence.setIdAgence(saveAgence.getId());
        // AgenceRequestDto agenceRequestDto = AgenceRequestDto.fromEntity(saveAgence);
        AgenceImmobiliere saveAgenceUpdate = agenceImmobiliereRepository.save(
          saveAgence
        );

        Utilisateur newUtilisateur = new Utilisateur();
        newUtilisateur.setIdAgence(saveAgenceUpdate.getId());
        newUtilisateur.setNom(dto.getNomPrenomGerant());
        // newUtilisateur.setPrenom(dto.getNomAgence());
        newUtilisateur.setEmail(dto.getEmailAgence());
        newUtilisateur.setMobile(dto.getMobileAgence());
        newUtilisateur.setPassword(passwordEncoder.encode(dto.getMotdepasse()));
        // newUtilisateur.setAgenceImmobilier(saveAgenceUpdate);
        Optional<Role> newRole = roleRepository.findRoleByRoleName("GERANT");
        if (newRole.isPresent()) {
          newUtilisateur.setUrole(newRole.get());
        }
        newUtilisateur.setUtilisateurIdApp(generateUserId());
        newUtilisateur.setJoinDate(new Date());
        newUtilisateur.setRoleUsed(ROLE_GERANT.name());
        newUtilisateur.setAuthorities(ROLE_GERANT.getAuthorities());
        newUtilisateur.setActive(dto.isActive());
        newUtilisateur.setActivated(true);
        newUtilisateur.setUsername(dto.getMobileAgence());
        newUtilisateur.setNonLocked(true);
        // newUtilisateur.setUserCreate(userCreate);
        Utilisateur saveUser = utilisateurRepository.save(newUtilisateur);
            Etablissement etablissement;
    
      if (dto.getIdEtable() == null) {
        etablissement = etablissementRepository.getById(1L);
      } else if (dto.getIdEtable() == 0  ) {
         etablissement = etablissementRepository.getById(1L);
      }else{
        etablissement =
          etablissementRepository.getById(dto.getIdEtable());
      }
      log.info("THE ETABLISSEMENT IS {}", etablissement.getLibChapitre());
      EtablissementUtilisateur etablissementUtilisateurCeate = new EtablissementUtilisateur();
      etablissementUtilisateurCeate.setEtableDefault(true);
      etablissementUtilisateurCeate.setEtabl(etablissement);
      etablissementUtilisateurCeate.setUtilisateurEtabl(saveUser);
      etablissementUtilisateurCeate.setIdAgence(dto.getIdAgence());
      etablissementUtilisateurCeate.setIdCreateur(dto.getIdCreateur());
       etablissementUtilisteurRepository.save(etablissementUtilisateurCeate);
       
      //   String token = generateVerificationToken(saveUser);
      //   String message = mailContentBuilder.build(
      //     "Merci de vous être enregistré a Gestimoweb, Cliquer sur le lien " +
      //     "ci-dessous pour activer votre account: " +
      //     ACTIVATION_EMAIL +
      //     "/" +
      //     token +
      //     "\n"
      //   );
        // mailService.sendMail(
        //   new NotificationEmail(
        //     "Veuillez activer votre compte en cliquant sur ce lien: ",
        //     saveUser.getEmail(),
        //     message
        //   )
        // );

        AgenceImmobilierDTO agenceImmobilierDTO = gestimoWebMapperImpl.fromAgenceImmobilier(
          saveAgenceUpdate
        );
        return agenceImmobilierDTO;
      } else {
        log.error("This user is already exist");
        throw new EntityNotFoundException(
          "The username or mobile is already exist in db " +
          dto.getMobileAgence(),
          ErrorCodes.UTILISATEUR_ALREADY_IN_USE
        );
      }
    } else {
      AgenceImmobiliere agenceImmobiliere1 = agenceImmobiliereRepository
        .findById(dto.getId())
        .orElseThrow(() ->
          new InvalidEntityException(
            "Aucun Etage has been found with id " + dto.getId(),
            ErrorCodes.APPARTEMENT_NOT_FOUND
          )
        );
      agenceImmobiliere1.setId(dto.getId());
      agenceImmobiliere1.setSigleAgence(dto.getSigleAgence());
      agenceImmobiliere1.setCapital(dto.getCapital());
      agenceImmobiliere1.setCompteContribuable(dto.getCompteContribuable());
      agenceImmobiliere1.setEmailAgence(dto.getEmailAgence());
      agenceImmobiliere1.setFaxAgence(dto.getFaxAgence());
      agenceImmobiliere1.setMobileAgence(dto.getMobileAgence());
      agenceImmobiliere1.setNomAgence(dto.getNomAgence());
      agenceImmobiliere1.setRegimeFiscaleAgence(dto.getRegimeFiscaleAgence());
      agenceImmobiliere1.setTelAgence(dto.getTelAgence());
      AgenceImmobiliere save = agenceImmobiliereRepository.save(
        agenceImmobiliere1
      );
      return gestimoWebMapperImpl.fromAgenceImmobilier(save);
    }
  }

  private Utilisateur getUserCreate(AgenceRequestDto dto) {
    return utilisateurRepository
      .findById(dto.getIdUtilisateurCreateur())
      .orElseThrow(() ->
        new InvalidEntityException(
          "Aucun Utilisateur has been found with Code " +
          dto.getIdUtilisateurCreateur(),
          ErrorCodes.UTILISATEUR_NOT_FOUND
        )
      );
  }

  @Override
  public String uploadLogoAgence(ImageLogoDto dto) throws IOException {
    AgenceImmobiliere agenceImmobilier = agenceImmobiliereRepository
      .findById(dto.getAgenceImmobiliere())
      .orElseThrow(() ->
        new InvalidEntityException(
          "Aucune agence has been found with ID " + dto.getAgenceImmobiliere(),
          ErrorCodes.AGENCE_NOT_FOUND
        )
      );
    try {
      ImageModel imageDataFound = imageRepository
        .findById(dto.getIdImage())
        .orElseThrow(() ->
          new InvalidEntityException(
            "Aucune agence has been found with ID " +
            dto.getAgenceImmobiliere(),
            ErrorCodes.AGENCE_NOT_FOUND
          )
        );
      if (imageDataFound != null) {
        imageDataFound.setLogoAgence(agenceImmobilier);
        imageDataFound.setName(agenceImmobilier.getSigleAgence());
        imageDataFound.setPicByte(dto.getFile().getBytes());
        imageRepository.save(imageDataFound);
      } else {
        ImageModel imageData = new ImageModel();
        imageData.setLogoAgence(agenceImmobilier);
        imageData.setName(agenceImmobilier.getSigleAgence());
        imageData.setPicByte(dto.getFile().getBytes());
        imageRepository.save(imageData);
      }

      return "Images dowload";
    } catch (Exception e) {
      return "Echec du téléchargement";
    }
  }
}
