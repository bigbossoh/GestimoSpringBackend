package com.bzdata.gestimospringbackend.Services.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.CronMailDto;
import com.bzdata.gestimospringbackend.Models.CronMail;
import com.bzdata.gestimospringbackend.Services.CronMailService;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.CronMailRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CronMailServiceImpl implements CronMailService {
    final CronMailRepository cronMailRepository;
    final GestimoWebMapperImpl gestimoWebMapperImpl;

    @Override
    public CronMailDto creerUneDateDenvoi(LocalDate date) {
        CronMail cronMail = new CronMail();
        cronMail.setNextDateMail(date);
        cronMail.setDayOfMonth(date.getDayOfMonth());
        cronMail.setDonne(false);
        CronMail cronSave = cronMailRepository.save(cronMail);
        return gestimoWebMapperImpl.fromCronMail(cronSave);
    }

    @Override
    public CronMailDto modifierUneDateDenvoi(Long id) {
        CronMail cronMail = cronMailRepository.findById(id).orElse(null);
        cronMail.setDonne(true);
        System.out.println("Le cron est le suivant : " + cronMail.getNextDateMail());
        CronMail cronSave = cronMailRepository.save(cronMail);
        System.out.println("Le cron est le enregistrer est le suivant : " + cronMail.isDonne());
        return gestimoWebMapperImpl.fromCronMail(cronSave);
    }

    @Override
    public CronMailDto getLasCronMailDto() {
        CronMail cronMail = cronMailRepository.findTopByOrderByIdDesc();

        return gestimoWebMapperImpl.fromCronMail(cronMail);
    }

    @Override
    public List<CronMail> getAllCronMailNotSend() {
        List<CronMail> cronMailDtos = cronMailRepository.findAll().stream()
                .filter(cron -> cron.isDonne() == false)
        .filter(crn->crn.getNextDateMail().getMonth()==LocalDate.now().getMonth())
        .collect(Collectors.toList());
        return cronMailDtos;
    }

}
