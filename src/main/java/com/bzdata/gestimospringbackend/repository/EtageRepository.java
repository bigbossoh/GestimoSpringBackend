package com.bzdata.gestimospringbackend.repository;

import java.util.List;
import java.util.Optional;

import com.bzdata.gestimospringbackend.Models.Etage;
import com.bzdata.gestimospringbackend.Models.Immeuble;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EtageRepository extends JpaRepository<Etage, Long> {
    @Query("SELECT coalesce(max(eta.numEtage), 0) FROM Etage eta")
    int getMaxNumEtage();

    Optional<Etage> findByNomEtage(String nom);

    List<Etage> findByImmeuble(Immeuble entity);

}
