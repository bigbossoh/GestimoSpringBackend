package com.bzdata.gestimospringbackend.repository;

import com.bzdata.gestimospringbackend.Models.ImageData;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageData,Long>{

}
