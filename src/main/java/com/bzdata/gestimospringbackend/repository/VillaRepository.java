package com.bzdata.gestimospringbackend.repository;

import com.bzdata.gestimospringbackend.Models.Villa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VillaRepository extends JpaRepository<Villa, Long> {
//    @Query("SELECT coalesce(max(vill.numBien), 0) FROM Villa vill")
//    int getMaxNumVilla();
}
