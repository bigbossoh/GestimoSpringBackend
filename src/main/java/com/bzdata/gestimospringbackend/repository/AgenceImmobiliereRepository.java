package com.bzdata.gestimospringbackend.repository;

import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgenceImmobiliereRepository extends JpaRepository<AgenceImmobiliere,Long> {

    Optional<AgenceImmobiliere> findAgenceImmobiliereByEmailAgence(String email);
    List<AgenceImmobiliere> findAllByOrderByNomAgenceAsc();
}
