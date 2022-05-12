package com.bzdata.gestimospringbackend.repository;

import com.bzdata.gestimospringbackend.Models.Magasin;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MagasinRepository extends JpaRepository<Magasin, Long> {
    @Query("SELECT coalesce(max(magas.numBien), 0) FROM Magasin magas")
    int getMaxNumMagasin();
    Magasin findMagasinByAbrvBienimmobilier(String AbrvBienimmobilier);
    Magasin findMagasinByNomBien(String NomBien);
}
