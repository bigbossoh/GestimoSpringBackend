package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.AnneeAppelLoyersDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyerDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyerRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;
import com.bzdata.gestimospringbackend.DTOs.BienPeriodeDto;
import com.bzdata.gestimospringbackend.DTOs.PeriodeDto;

public interface AppelLoyerService {

    List<String> save(AppelLoyerRequestDto dto);

    boolean deleteAppelDto(Long id);

    List<AppelLoyersFactureDto> findAll();

    AppelLoyersFactureDto getFirstLoyerImpayerByBien(Long bienImmobilier);

    List<AppelLoyersFactureDto> findAllAppelLoyerByPeriode(String periodeAppelLoyer);

    public double soldeArrierer(Long idBailLocation);

    AppelLoyersFactureDto findById(Long id);

    List<AppelLoyerDto> findAllAppelLoyerByBailId(Long idBailLocation);

    List<Integer> listOfDistinctAnnee();

    List<PeriodeDto> listOfPerodesByAnnee(Integer annee);

    List<AnneeAppelLoyersDto> listOfAppelLoyerByAnnee(Integer annee);

    List<AppelLoyersFactureDto> findAllAppelLoyerImpayerByBailId(Long idBailLocation);

    double impayeParPeriode(String periode);

    double payeParPeriode(String periode);

    double impayeParAnnee(String annee);

    double payeParAnnee(String annee);

    Long nombreBauxImpaye(String periode);

    Long nombreBauxPaye(String periode);

    double montantBeauxImpayer(String periode);
}
