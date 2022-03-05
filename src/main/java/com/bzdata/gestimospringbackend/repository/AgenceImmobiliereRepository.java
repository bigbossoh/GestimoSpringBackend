package com.bzdata.gestimospringbackend.repository;

import java.util.List;
import java.util.Optional;

import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenceImmobiliereRepository extends JpaRepository<AgenceImmobiliere, Long> {

    Optional<AgenceImmobiliere> findAgenceImmobiliereByEmailAgence(String email);

    List<AgenceImmobiliere> findAllByOrderByNomAgenceAsc();
}
