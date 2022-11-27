package com.bzdata.gestimospringbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bzdata.gestimospringbackend.Models.SuivieDepense;

public interface SuivieDepenseRepository extends JpaRepository<SuivieDepense, Long> {

    Optional<SuivieDepense> findByCodeTransaction(String codeTransation);
   
}
