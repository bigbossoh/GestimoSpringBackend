package com.bzdata.gestimospringbackend.repository;

import java.util.List;

import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.BailLocation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppelLoyerRepository extends JpaRepository<AppelLoyer, Long> {

    List<AppelLoyer> findAllByBailLocationAppelLoyer(BailLocation bailLocation);

}
