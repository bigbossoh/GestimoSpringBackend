package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.MontantLoyerBailDto;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;

import java.util.List;

public interface MontantLoyerBailService {

    MontantLoyerBailDto saveNewMontantLoyerBail(MontantLoyerBailDto dto);

    MontantLoyerBailDto updateNewMontantLoyerBail(MontantLoyerBailDto dto);

    boolean delete(Long id);

    List<MontantLoyerBailDto> findAll();

    MontantLoyerBailDto findById(Long id);

    List<MontantLoyerBailDto> findAllMontantLoyerBailByBailId(Long idBailLocation);
}
