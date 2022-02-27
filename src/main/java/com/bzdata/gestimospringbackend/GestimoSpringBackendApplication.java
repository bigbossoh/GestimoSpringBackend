package com.bzdata.gestimospringbackend;

import java.util.Optional;

import com.bzdata.gestimospringbackend.Models.Role;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.repository.RoleRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class GestimoSpringBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestimoSpringBackendApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner chargerDonnees(RoleRepository roleRepository, UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder) {
        String mdp = passwordEncoder.encode("superviseur");
        Utilisateur utilisateur = new Utilisateur();
        return (args) -> {

            // Creation des Constants
            // ROLES
            Optional<Role> roles = null;
            roles = roleRepository.findRoleByRoleName("SUPERVISER");
            if (!roles.isPresent()) {
                roleRepository.save(new Role("SUPERVISER", "Role de superviseur", null));
            }
            roles = null;
            roles = roleRepository.findRoleByRoleName("GERANT");
            if (!roles.isPresent()) {
                roleRepository.save(new Role("GERANT", "Role de GERANT", null));
            }
            roles = null;
            roles = roleRepository.findRoleByRoleName("PROPRIETAIRE");
            if (!roles.isPresent()) {
                roleRepository.save(new Role("PROPRIETAIRE", "Role de PROPRIETAIRE", null));
            }
            roles = null;
            roles = roleRepository.findRoleByRoleName("LOCATAIRE");
            if (!roles.isPresent()) {
                roleRepository.save(new Role("LOCATAIRE", "Role de LOCATAIRE", null));
            }
            roles = null;
            roles = roleRepository.findRoleByRoleName("SUPERVISER");
            if (roles.isPresent()) {
                Optional<Utilisateur>userPrincipal=utilisateurRepository.findUtilisateurByEmail("superviseur@superviseur.com");
             if (!userPrincipal.isPresent()) {
                    utilisateur.setUrole(roles.get());
                    utilisateur.setActivated(true);
                    utilisateur.setEmail("superviseur@superviseur.com");
                    utilisateur.setGenre("M");
                    utilisateur.setMobile("0177797744");
                    utilisateur.setNationalité("Ivoirien");
                    utilisateur.setPassword(mdp);
                    utilisateur.setNom("superviseur");
                    utilisateur.setPrenom("superviseur");
                    utilisateur.setUsername("superviseur");
                    utilisateurRepository.save(utilisateur);
             }
   
            } // CRATION DU SUPERUTILISATEUR

        };
    }

}
