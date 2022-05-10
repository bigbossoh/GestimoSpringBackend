package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.MontantLoyerBailDto;

public interface MontantLoyerBailService {

    boolean saveNewMontantLoyerBail(MontantLoyerBailDto dto);

    MontantLoyerBailDto updateNewMontantLoyerBail(MontantLoyerBailDto dto);

    boolean delete(Long id);

    List<MontantLoyerBailDto> findAll();

    MontantLoyerBailDto findById(Long id);

    List<MontantLoyerBailDto> findAllMontantLoyerBailByBailId(Long idBailLocation);
}
