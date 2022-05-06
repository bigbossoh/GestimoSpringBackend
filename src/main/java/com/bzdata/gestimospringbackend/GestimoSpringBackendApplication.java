package com.bzdata.gestimospringbackend;

import static com.bzdata.gestimospringbackend.constant.FileConstant.USER_FOLDER;
import static com.bzdata.gestimospringbackend.enumeration.Role.*;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import com.bzdata.gestimospringbackend.Models.Commune;
import com.bzdata.gestimospringbackend.Models.Magasin;
import com.bzdata.gestimospringbackend.Models.Pays;
import com.bzdata.gestimospringbackend.Models.Quartier;
import com.bzdata.gestimospringbackend.Models.Role;
import com.bzdata.gestimospringbackend.Models.Site;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Models.Ville;

import com.bzdata.gestimospringbackend.repository.AgenceImmobiliereRepository;

import com.bzdata.gestimospringbackend.repository.CommuneRepository;
import com.bzdata.gestimospringbackend.repository.MagasinRepository;
import com.bzdata.gestimospringbackend.repository.PaysRepository;
import com.bzdata.gestimospringbackend.repository.QuartierRepository;
import com.bzdata.gestimospringbackend.repository.RoleRepository;
import com.bzdata.gestimospringbackend.repository.SiteRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.repository.VilleRepository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@RequiredArgsConstructor
@OpenAPIDefinition(info = @Info(title = "Gestimo API", version = "2.0", description = "Description de Gestimo"))
@SecurityScheme(name = "gestimoapi", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER, bearerFormat = "JWT")
public class GestimoSpringBackendApplication {

    //private final GestimoWebInitDataAgenceImmoServiceImpl gestimoWebInitDataAgenceImmoService;

    public static void main(String[] args) {
        SpringApplication.run(GestimoSpringBackendApplication.class, args);
        new File(USER_FOLDER).mkdirs();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token",
                "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    public CommandLineRunner chargerDonnees(SiteRepository siteRepository, QuartierRepository quartierRepository,
            RoleRepository roleRepository,
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder, PaysRepository paysRepository, VilleRepository villeRepository,
            CommuneRepository communeRepository,
            AgenceImmobiliereRepository agenceImmobiliereRepository,
            MagasinRepository magasinRepository) {
        String mdp = passwordEncoder.encode("superviseur");
        Utilisateur utilisateur = new Utilisateur();
        Magasin magasin = new Magasin();
        Pays pays = new Pays();
        AgenceImmobiliere aImmobiliere = new AgenceImmobiliere();

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

            Optional<Pays> p = paysRepository.findById(1L);
            if (p.isPresent()) {
                Optional<Ville> v = villeRepository.findById(1L);
                if (!v.isPresent()) {
                    Ville ville = new Ville();
                    ville.setAbrvVille("ABJ");
                    ville.setPays(p.get());
                    ville.setNomVille("Abidjan");
                    villeRepository.save(ville);

                    Ville ville1 = new Ville();
                    ville1.setAbrvVille("BKE");
                    ville1.setPays(p.get());
                    ville1.setNomVille("Bouaké");
                    villeRepository.save(ville1);
                }

            }

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
                site.setAbrSite(StringUtils.deleteWhitespace(monQuartier.get()
                        .getCommune().getVille().getPays().getAbrvPays())
                        + "-" +
                        StringUtils.deleteWhitespace(monQuartier.get().getCommune().getVille().getAbrvVille())
                        + "-" + StringUtils.deleteWhitespace(monQuartier.get()
                                .getCommune().getAbrvCommune())
                        + "-" + StringUtils.deleteWhitespace(monQuartier.get().getAbrvQuartier()));
                site.setNomSite(
                        monQuartier.get()
                                .getCommune().getVille().getPays().getNomPays()
                                + "-" + monQuartier.get().getCommune().getVille().getNomVille()
                                + "-" + monQuartier.get().getCommune().getNomCommune()
                                + "-" + monQuartier.get().getNomQuartier());
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
                Utilisateur userPrincipalSuperviseur = utilisateurRepository
                        .findUtilisateurByUsername("+2250103833350");

                if (userPrincipalSuperviseur == null) {
                    utilisateur.setUrole(roles.get());

                    utilisateur.setNom("SUPERVISEUR");
                    utilisateur.setPrenom("SUPERVISEUR PRENOM");
                    utilisateur.setEmail("superviseur@superviseur.com");
                    utilisateur.setMobile("+2250103833350");
                    utilisateur.setDateDeNaissance(LocalDate.parse("1980-01-08"));
                    utilisateur.setLieuNaissance("Abidjan");
                    utilisateur.setTypePieceIdentite("CNI");
                    utilisateur.setNumeroPieceIdentite("1236544");
                    utilisateur.setDateFinPiece(LocalDate.parse("2022-01-08"));
                    utilisateur.setDateDebutPiece(LocalDate.parse("2016-01-08"));
                    utilisateur.setNationalité("Ivoirienne");
                    utilisateur.setGenre("M");
                    utilisateur.setUsername(utilisateur.getMobile());
                    utilisateur.setPassword(mdp);
                    utilisateur.setIdAgence(1L);

                    utilisateur.setJoinDate(new Date());
                    utilisateur.setActivated(true);
                    utilisateur.setRoleUsed(ROLE_SUPER_SUPERVISEUR.name());
                    utilisateur.setAuthorities(ROLE_SUPER_SUPERVISEUR.getAuthorities());
                    utilisateur.setActive(true);
                    utilisateur.setNonLocked(true);
                    utilisateurRepository.save(utilisateur);
                }


            } // CRATION DU SUPERUTILISATEUR

               // gestimoWebInitDataAgenceImmoService.initAgenceImmobilier();

        };
    }
}
