package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.DTOs.*;
import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import com.bzdata.gestimospringbackend.Models.Role;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.AgenceImmobilierService;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.AgenceImmobiliereRepository;
import com.bzdata.gestimospringbackend.repository.RoleRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.validator.AgenceDtoValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AgenceImmobiliereServiceImpl implements AgenceImmobilierService {

    private final AgenceImmobiliereRepository agenceImmobiliereRepository;
    private final UtilisateurRepository utilisateurRepository;
    private  final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
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
            newUtilisateur.setPassword(bCryptPasswordEncoder.encode("gerant"));
            newUtilisateur.setAgence(saveAgence);
            Optional<Role> newRole = roleRepository.findRoleByRoleName("GERANT");
            if (newRole.isPresent()) {
                newUtilisateur.setUrole(newRole.get());
            }
            newUtilisateur.setUserCreate(UtilisateurRequestDto.toEntity(dto.getUtilisateurCreateur()));
            utilisateurRepository.save(newUtilisateur);
            return AgenceResponseDto.fromEntity(saveAgence);
        }
        else {
                log.error("This user is already exist");
            return null;
            }


    }

    @Override
    public AgenceResponseDto findById(Long id) {
        return null;
    }

    @Override
    public List<AgenceResponseDto> listOfAgenceImmobilier() {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public AgenceResponseDto findAgenceByEmail(String email) {
        return null;
    }
}
