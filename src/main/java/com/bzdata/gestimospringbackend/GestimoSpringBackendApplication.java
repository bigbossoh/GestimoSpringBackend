package com.bzdata.gestimospringbackend;

import java.util.List;
import java.util.Optional;

import com.bzdata.gestimospringbackend.Models.Commune;
import com.bzdata.gestimospringbackend.Models.Pays;
import com.bzdata.gestimospringbackend.Models.Quartier;
import com.bzdata.gestimospringbackend.Models.Role;
import com.bzdata.gestimospringbackend.Models.Site;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Models.Ville;
import com.bzdata.gestimospringbackend.repository.CommuneRepository;
import com.bzdata.gestimospringbackend.repository.PaysRepository;
import com.bzdata.gestimospringbackend.repository.QuartierRepository;
import com.bzdata.gestimospringbackend.repository.RoleRepository;
import com.bzdata.gestimospringbackend.repository.SiteRepository;
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
    public CommandLineRunner chargerDonnees(SiteRepository siteRepository, QuartierRepository quartierRepository,
            RoleRepository roleRepository,
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder, PaysRepository paysRepository, VilleRepository villeRepository,
            CommuneRepository communeRepository) {
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
            // CREATION DES COMMUNES
            List<Ville> lesVilles = villeRepository.findAll();
            lesVilles.forEach(v -> {
                if (v.getAbrvVille().contains("ABJ")) {
                    Commune commune1 = new Commune();
                    commune1.setAbrvCommune("KOUM");
                    commune1.setNomCommune("Koumassi");
                    commune1.setVille(v);
                    communeRepository.save(commune1);

                    Commune commune = new Commune();
                    commune.setAbrvCommune("YOP");
                    commune.setNomCommune("Yopougon");
                    commune.setVille(v);
                    communeRepository.save(commune);
                }
            });
            // CREATIONS DES QUARTIERS
            for (int index = 0; index < 1; index++) {
                Quartier quartier = new Quartier();
                Optional<Commune> maCommune = communeRepository.findById(1L);
                if (maCommune.isPresent()) {
                    quartier.setAbrvQuartier("PROD");
                    quartier.setNomQuartier("Prodomo");
                    quartier.setCommune(maCommune.get());
                    quartierRepository.save(quartier);
                }
            }
            // GESTION DES SITES
            Optional<Quartier> monQuartier = quartierRepository.findById(1L);
            if (monQuartier.isPresent()) {
                Site site = new Site();
                site.setAbrSite("PROD1");
                site.setNomSite("Prodomo1");
                site.setQuartier(monQuartier.get());
                siteRepository.save(site);
            }
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
