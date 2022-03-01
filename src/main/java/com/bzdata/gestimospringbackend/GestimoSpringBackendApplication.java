package com.bzdata.gestimospringbackend;

import java.util.List;
import java.util.Optional;

import com.bzdata.gestimospringbackend.Models.Pays;
import com.bzdata.gestimospringbackend.Models.Role;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Models.Ville;
import com.bzdata.gestimospringbackend.repository.PaysRepository;
import com.bzdata.gestimospringbackend.repository.RoleRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.repository.VilleRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@OpenAPIDefinition(info = @Info(title = "Gestimo API", version = "2.0", description = "Description de Gestimo"))
@SecurityScheme(name = "gestimoapi", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER, bearerFormat = "JWT")
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
            PasswordEncoder passwordEncoder, PaysRepository paysRepository, VilleRepository villeRepository) {
        String mdp = passwordEncoder.encode("superviseur");
        Utilisateur utilisateur = new Utilisateur();
        Pays pays = new Pays();

        return (args) -> {
            // Creation des Constants
            // CHARGEMENT DU PAYS COTE D4IVOIRE
            Optional<Pays> oPays = paysRepository.findByAbrvPays("CI");
            if (!oPays.isPresent()) {
                pays.setAbrvPays("CI");
                pays.setNomPays("Côte d'Ivoire");
                paysRepository.save(pays);
            }
            // CREATION VILLES
            List<Pays> lesPays = paysRepository.findAll();
            lesPays.forEach(p -> {
                Ville ville = new Ville();
                ville.setAbrvVille("ABJ");
                ville.setPays(p);
                ville.setNomVille("Abidjan");
                villeRepository.save(ville);

                Ville ville1 = new Ville();
                ville1.setAbrvVille("BKE");
                ville1.setPays(p);
                ville1.setNomVille("Bouaké");
                villeRepository.save(ville1);
            });
            // ROLES
            Optional<Role> roles = null;
            roles = roleRepository.findRoleByRoleName("SUPERVISEUR");
            if (!roles.isPresent()) {
                roleRepository.save(new Role("SUPERVISEUR", "Role de superviseur", null));
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
            roles = roleRepository.findRoleByRoleName("SUPERVISEUR");
            if (roles.isPresent()) {
                Optional<Utilisateur> userPrincipal = utilisateurRepository
                        .findUtilisateurByEmail("superviseur@superviseur.com");
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
