package com.bzdata.gestimospringbackend.repository;

import java.util.List;
import java.util.Optional;

import com.bzdata.gestimospringbackend.Models.Appartement;
import com.bzdata.gestimospringbackend.Models.Etage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppartementRepository extends JpaRepository<Appartement, Long> {
//    @Query("SELECT coalesce(max(appart.numeroApp), 0) FROM Appartement appart")
//    int getMaxNumAppartement();

    Optional<Appartement> findByNomCompletBienImmobilier(String nom);

    List<Appartement> findByEtageAppartement(Etage entity);

}
