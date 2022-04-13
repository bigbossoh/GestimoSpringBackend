package com.bzdata.gestimospringbackend.repository;

import java.util.List;
import java.util.Optional;

import com.bzdata.gestimospringbackend.Models.Utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    @Override
    Optional<Utilisateur> findById(Long aLong);

    Optional<Utilisateur> findUtilisateurByEmail(String email);

    Utilisateur findUtilisateurByUsername(String username);

    Utilisateur findUtilisateurByMobile(String mobile);

    List<Utilisateur> findAllByOrderByNomAsc();

}
