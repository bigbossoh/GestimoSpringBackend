package com.bzdata.gestimospringbackend.repository;

import com.bzdata.gestimospringbackend.Models.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByName(String name);
}
