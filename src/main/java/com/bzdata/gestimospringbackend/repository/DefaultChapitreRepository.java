package com.bzdata.gestimospringbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bzdata.gestimospringbackend.Models.Etablissement;

public interface DefaultChapitreRepository extends JpaRepository<Etablissement,Long>{
    
}
