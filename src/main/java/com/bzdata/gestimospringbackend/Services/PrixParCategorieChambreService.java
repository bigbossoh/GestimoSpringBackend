package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.PrixParCategorieChambreDto;
import com.bzdata.gestimospringbackend.Models.hotel.PrixParCategorieChambre;

public interface PrixParCategorieChambreService extends AbstractService<PrixParCategorieChambreDto>{
    PrixParCategorieChambreDto saveOrUpDatePrixPArCategoryChambre(PrixParCategorieChambreDto dto);
    PrixParCategorieChambreDto categoriParPrix(Long idCategori);
}
