package com.bzdata.gestimospringbackend.repository;

import java.util.List;

import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MontantLoyerBailRepository extends JpaRepository<MontantLoyerBail, Long> {

    List<MontantLoyerBail> findByBailLocation(BailLocation bailLocation);
}
