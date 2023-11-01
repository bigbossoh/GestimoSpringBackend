package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.PrixParCategorieChambreDto;

public interface PrixParCategorieChambreService extends AbstractService<PrixParCategorieChambreDto>{
    PrixParCategorieChambreDto saveOrUpDatePrixPArCategoryChambre(PrixParCategorieChambreDto dto);
    PrixParCategorieChambreDto categoriParPrix(Long idCategori);
}
