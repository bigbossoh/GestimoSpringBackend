package com.bzdata.gestimospringbackend.repository;

import com.bzdata.gestimospringbackend.Models.Site;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site,Long> {
    
}
