package com.bzdata.gestimospringbackend.repository;

import java.util.List;
import java.util.Optional;

import com.bzdata.gestimospringbackend.Models.Immeuble;
import com.bzdata.gestimospringbackend.Models.Site;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImmeubleRepository extends JpaRepository<Immeuble, Long> {
//    @Query("SELECT coalesce(max(imme.numeroImmeuble), 0) FROM Immeuble imme")
//    int getMaxNumImmeuble();

    Optional<Immeuble> findByDescriptionImmeuble(String nom);

    List<Immeuble> findBySite(Site site);

}
