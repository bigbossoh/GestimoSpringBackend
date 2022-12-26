package com.bzdata.gestimospringbackend.Services;

import java.time.LocalDate;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.CronMailDto;
import com.bzdata.gestimospringbackend.Models.CronMail;

public interface CronMailService {
    CronMailDto creerUneDateDenvoi(LocalDate date);

    CronMailDto modifierUneDateDenvoi(Long id);

    CronMailDto getLasCronMailDto();

    List<CronMail> getAllCronMailNotSend();
}
