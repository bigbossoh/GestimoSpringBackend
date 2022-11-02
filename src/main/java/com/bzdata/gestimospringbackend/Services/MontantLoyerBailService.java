package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.MontantLoyerBailDto;

public interface MontantLoyerBailService {

    boolean saveNewMontantLoyerBail(Long currentIdMontantLoyerBail, double nouveauMontantLoyer,
                                    double ancienMontantLoyer,Long idBailLocation, Long idAgence);

    MontantLoyerBailDto updateNewMontantLoyerBail(MontantLoyerBailDto dto);

    boolean delete(Long id);

    List<MontantLoyerBailDto> findAll(Long idAgence);

    MontantLoyerBailDto findById(Long id);

    List<MontantLoyerBailDto> findAllMontantLoyerBailByBailId(Long idBailLocation);
}
