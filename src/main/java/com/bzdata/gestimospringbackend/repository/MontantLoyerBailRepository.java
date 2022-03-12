package com.bzdata.gestimospringbackend.repository;

import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MontantLoyerBailRepository extends JpaRepository<MontantLoyerBail, Long> {


    List<MontantLoyerBail> findByBailLocation(BailLocation bailLocation);
}
