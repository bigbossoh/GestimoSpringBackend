package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.BienImmobilierAffiheDto;

public interface BienImmobilierService {
    List<BienImmobilierAffiheDto> findAll();
}
