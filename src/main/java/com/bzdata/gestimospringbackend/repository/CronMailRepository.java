package com.bzdata.gestimospringbackend.repository;

import com.bzdata.gestimospringbackend.Models.CronMail;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CronMailRepository extends JpaRepository<CronMail,Long>{

    CronMail findTopByOrderByIdDesc();

}
