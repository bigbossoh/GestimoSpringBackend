package com.bzdata.gestimospringbackend.repository;

import com.bzdata.gestimospringbackend.Models.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
    Optional<ImageModel> findByName(String name);
}
