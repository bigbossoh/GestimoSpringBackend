package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.BienImmobilierDto;

public interface BienImmobilierService {
    List<BienImmobilierDto> findAll();
}
