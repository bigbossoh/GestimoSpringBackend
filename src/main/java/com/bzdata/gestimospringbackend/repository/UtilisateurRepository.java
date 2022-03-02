package com.bzdata.gestimospringbackend.repository;

import com.bzdata.gestimospringbackend.DTOs.AgenceResponseDto;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {

    @Override
    Optional<Utilisateur> findById(Long aLong);
    Optional<Utilisateur> findUtilisateurByEmail(String email);
    List<Utilisateur> findAllByOrderByNomAsc();

}
