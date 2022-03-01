package com.bzdata.gestimospringbackend.repository;

import com.bzdata.gestimospringbackend.Models.Quartier;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuartierRepository extends JpaRepository<Long,Quartier> {
    
}
