package com.bzdata.gestimospringbackend.repository;

import com.bzdata.gestimospringbackend.Models.Pays;
import com.bzdata.gestimospringbackend.Models.Site;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site,Long> {

    Optional<Site> findByNomSite(String nom);
    
}
