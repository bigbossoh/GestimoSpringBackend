package com.bzdata.gestimospringbackend.repository;
import com.bzdata.gestimospringbackend.Models.Villa;
import org.springframework.data.jpa.repository.JpaRepository;



public interface VillaRepository extends JpaRepository<Villa,Long> {
//    @Query(value = "SELECT min(nbrePiece) FROM Villa")
//    public Long min();
//
//    @Query(value = "SELECT max(nbrePiece) FROM Villa")
//    public Long max();
//    Optional<Villa> findByNomVilla(String nom);
//    List<Villa> findBySiteRequestDto(Site site);
}
