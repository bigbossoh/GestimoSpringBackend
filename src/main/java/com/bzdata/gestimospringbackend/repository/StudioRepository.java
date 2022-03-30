package com.bzdata.gestimospringbackend.repository;

import java.util.List;
import java.util.Optional;

import com.bzdata.gestimospringbackend.Models.Etage;
import com.bzdata.gestimospringbackend.Models.Studio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudioRepository extends JpaRepository<Studio, Long> {
    @Query("SELECT coalesce(max(stud.numeroStudio), 0) FROM Studio stud")
    int getMaxNumStudio();

    Optional<Studio> findByNomStudio(String nom);

    List<Studio> findByEtageStudio(Etage etage);

}
