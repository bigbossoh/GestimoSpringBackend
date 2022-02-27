package com.bzdata.gestimospringbackend;

import com.bzdata.gestimospringbackend.Models.Role;
import com.bzdata.gestimospringbackend.repository.RoleRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GestimoSpringBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestimoSpringBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner chargerDonnees(RoleRepository roleRepository) {

        return (args) -> {
            // Creation des Constants
            // ROLES
            roleRepository.save(new Role("SUPERVISER", "Role de superviseur", null));
            roleRepository.save(new Role("GERANT", "Role de GERANT", null));
            roleRepository.save(new Role("PROPRIETAIRE", "Role de PROPRIETAIRE", null));
            roleRepository.save(new Role("LOCATAIRE", "Role de LOCATAIRE", null));
            //CRATION DU SUPERUTILISATEUR
            
        };
    }

}
