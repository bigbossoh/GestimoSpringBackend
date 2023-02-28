package com.bzdata.gestimospringbackend;

import static com.bzdata.gestimospringbackend.constant.FileConstant.FOLDER_PATH;
import static com.bzdata.gestimospringbackend.enumeration.Role.ROLE_SUPER_SUPERVISEUR;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.bzdata.gestimospringbackend.Models.Commune;
import com.bzdata.gestimospringbackend.Models.Pays;
import com.bzdata.gestimospringbackend.Models.Quartier;
import com.bzdata.gestimospringbackend.Models.Role;
import com.bzdata.gestimospringbackend.Models.Site;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Models.Ville;
import com.bzdata.gestimospringbackend.Utils.SmsOrangeConfig;
import com.bzdata.gestimospringbackend.repository.AgenceImmobiliereRepository;
import com.bzdata.gestimospringbackend.repository.CommuneRepository;
import com.bzdata.gestimospringbackend.repository.MagasinRepository;
import com.bzdata.gestimospringbackend.repository.PaysRepository;
import com.bzdata.gestimospringbackend.repository.QuartierRepository;
import com.bzdata.gestimospringbackend.repository.RoleRepository;
import com.bzdata.gestimospringbackend.repository.SiteRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.repository.VilleRepository;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
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
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
@OpenAPIDefinition(info = @Info(title = "Gestimo API", version = "2.0", description = "Description de Gestimo"))
@SecurityScheme(name = "gestimoapi", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER, bearerFormat = "JWT")
public class GestimoSpringBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestimoSpringBackendApplication.class, args);
        new File(FOLDER_PATH).mkdirs();

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
        corsConfiguration.setAllowedOriginPatterns(
                Arrays.asList("*", "http://angular-front-end-gestimoweb.s3-website-us-east-1.amazonaws.com:4200",
                        "http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(
                Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type", "Content-Disposition",
                        "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
                        "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token",
                "Content-Disposition", "Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    public CommandLineRunner chargerDonnees(SiteRepository siteRepository, QuartierRepository quartierRepository,
            RoleRepository roleRepository, SmsOrangeConfig envoiSmsOrange,
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder, PaysRepository paysRepository, VilleRepository villeRepository,
            CommuneRepository communeRepository,
            AgenceImmobiliereRepository agenceImmobiliereRepository,
            MagasinRepository magasinRepository) {
        String mdp = passwordEncoder.encode("superviseur");
        Utilisateur utilisateur = new Utilisateur();
        Pays pays = new Pays();
        return (args) -> {
            // Creation des Constants
            // CHARGEMENT DU PAYS COTE D'IVOIRE
            Optional<Pays> oPays = paysRepository.findByAbrvPays("CI");
            if (!oPays.isPresent()) {
                pays.setAbrvPays("CI");
                pays.setIdAgence(1L);
                pays.setNomPays("Côte d'Ivoire");
                paysRepository.save(pays);
            }
            // CREATION VILLES
            Optional<Pays> p = paysRepository.findById(1L);
            if (p.isPresent()) {
                Optional<Ville> v = villeRepository.findById(1L);
                if (!v.isPresent()) {
                    Stream.of("ABIDJAN", "ABOISSO").forEach(vil -> {
                        Ville ville = new Ville();
                        if (vil == "ABIDJAN") {
                            ville.setAbrvVille("ABJ");
                        } else {
                            ville.setAbrvVille("ABOI");
                        }
                        ville.setPays(p.get());
                        ville.setNomVille(vil);
                        ville.setIdAgence(1L);
                        villeRepository.save(ville);

                    });
                }

            }

            // CREATION DES COMMUNES
            List<Ville> lesVilles = villeRepository.findAll();
            lesVilles.forEach(v -> {
                Optional<Commune> maCommune1 = communeRepository.findById(1L);
                if (!maCommune1.isPresent()) {
                    if (v.getAbrvVille().contains("ABJ")) {
                        Stream.of("Abobo", "Adjamé", "Anyama",
                                "Attécoubé", "Bingerville", "Cocody", "Koumassi",
                                "Marcory", "Plateau", "Port bouët", "Treichville",
                                "Songon", "Yopougon").forEach(com -> {

                                    Commune commune1 = new Commune();
                                    commune1.setAbrvCommune(com.substring(0, 4));
                                    commune1.setNomCommune(com);
                                    commune1.setIdAgence(1L);
                                    commune1.setVille(v);
                                    communeRepository.save(commune1);
                                });
                    }
                }

            });

            // CREATIONS DES QUARTIERS

            Optional<Quartier> monQuartierVerification = quartierRepository.findById(1L);
            if (!monQuartierVerification.isPresent()) {
                List<Commune> lesCommunes = communeRepository.findAll();
                lesCommunes.forEach(comm -> {

                    if (comm.getNomCommune().contains("Cocody")) {
                        Stream.of("Abatta", "Aghien", "Dokui").forEach(com -> {
                            Quartier quartier = new Quartier();
                            quartier.setAbrvQuartier(com.substring(0, 4));
                            quartier.setNomQuartier(com);
                            quartier.setIdAgence(1L);
                            quartier.setCommune(comm);
                            quartierRepository.save(quartier);
                        });
                    }
                    if (comm.getNomCommune().contains("Anyama")) {
                        Stream.of("Ebimpé").forEach(com -> {
                            Quartier quartier = new Quartier();
                            quartier.setIdAgence(1L);
                            quartier.setAbrvQuartier(com.substring(0, 3));
                            quartier.setNomQuartier(com);
                            quartier.setCommune(comm);
                            quartierRepository.save(quartier);
                        });
                    }
                    if (comm.getNomCommune().contains("Yopougon")) {
                        Stream.of("Assanvon", "Niangon", "Port Bouet II").forEach(com -> {
                            Quartier quartier = new Quartier();
                            quartier.setAbrvQuartier(com.substring(0, 4));
                            quartier.setIdAgence(1L);
                            quartier.setNomQuartier(com);
                            quartier.setCommune(comm);
                            quartierRepository.save(quartier);
                        });
                    }
                });

            }

            // GESTION DES SITES
            Optional<Site> monSite = siteRepository.findById(1L);
            if (!monSite.isPresent()) {
                List<Quartier> lesQuartiers = quartierRepository.findAll();
                lesQuartiers.forEach(quart -> {

                    if (quart.getNomQuartier().contains("Assanvon")) {
                        Site site = new Site();
                        site.setIdAgence(1L);
                        site.setAbrSite(StringUtils.deleteWhitespace(quart
                                .getCommune().getVille().getPays().getAbrvPays())
                                + "-" +
                                StringUtils.deleteWhitespace(quart.getCommune().getVille().getAbrvVille())
                                + "-" + StringUtils.deleteWhitespace(quart
                                        .getCommune().getAbrvCommune())
                                + "-" + StringUtils.deleteWhitespace(quart.getAbrvQuartier()));
                        site.setNomSite(
                                quart.getCommune().getVille().getPays().getNomPays()
                                        + "-" + quart.getCommune().getVille().getNomVille()
                                        + "-" + quart.getCommune().getNomCommune()
                                        + "-" + quart.getNomQuartier());
                        site.setQuartier(quart);
                        siteRepository.save(site);
                    }
                    if (quart.getNomQuartier().contains("Aghien")) {
                        Site site = new Site();
                        site.setIdAgence(1L);
                        site.setAbrSite(StringUtils.deleteWhitespace(quart
                                .getCommune().getVille().getPays().getAbrvPays())
                                + "-" +
                                StringUtils.deleteWhitespace(quart.getCommune().getVille().getAbrvVille())
                                + "-" + StringUtils.deleteWhitespace(quart
                                        .getCommune().getAbrvCommune())
                                + "-" + StringUtils.deleteWhitespace(quart.getAbrvQuartier()));
                        site.setNomSite(
                                quart.getCommune().getVille().getPays().getNomPays()
                                        + "-" + quart.getCommune().getVille().getNomVille()
                                        + "-" + quart.getCommune().getNomCommune()
                                        + "-" + quart.getNomQuartier());
                        site.setQuartier(quart);
                        siteRepository.save(site);
                    }
                });
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
                        .findUtilisateurByUsername("0103833350");
                if (userPrincipalSuperviseur == null) {
                    utilisateur.setUrole(roles.get());
                    utilisateur.setUtilisateurIdApp(generateUserId());
                    utilisateur.setNom("SUPERVISEUR");
                    utilisateur.setPrenom("SUPERVISEUR PRENOM");
                    utilisateur.setEmail("superviseur@superviseur.com");
                    utilisateur.setMobile("0103833350");
                    utilisateur.setDateDeNaissance(LocalDate.parse("1980-01-08"));
                    utilisateur.setLieuNaissance("Abidjan");
                    utilisateur.setTypePieceIdentite("CNI");
                    utilisateur.setNumeroPieceIdentite("1236544");
                    utilisateur.setDateFinPiece(LocalDate.parse("2022-01-08"));
                    utilisateur.setDateDebutPiece(LocalDate.parse("2016-01-08"));
                    utilisateur.setNationalité("Ivoirienne");
                    utilisateur.setGenre("M");
                    utilisateur.setActivated(true);
                    utilisateur.setUsername("0103833350");
                    utilisateur.setPassword(mdp);
                    utilisateur.setIdAgence(1001L);
                    utilisateur.setJoinDate(new Date());
                    utilisateur.setRoleUsed(ROLE_SUPER_SUPERVISEUR.name());
                    utilisateur.setAuthorities(ROLE_SUPER_SUPERVISEUR.getAuthorities());
                    utilisateur.setActive(true);
                    utilisateur.setNonLocked(true);
                    utilisateurRepository.save(utilisateur);
                }

            } // CREATION DU SUPERUTILISATEUR

        };
    }

    @Bean
    public HttpTraceRepository htttpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

    private String generateUserId() {
        return "User-" + RandomStringUtils.randomAlphanumeric(5);
    }
}
