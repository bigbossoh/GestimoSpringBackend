package com.bzdata.gestimospringbackend.repository;

import com.bzdata.gestimospringbackend.Models.Commune;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommuneRepository extends JpaRepository<Commune,Long>{
    
}
