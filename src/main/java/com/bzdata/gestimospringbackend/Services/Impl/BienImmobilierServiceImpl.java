package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.BienImmobilierDto;
import com.bzdata.gestimospringbackend.Services.BienImmobilierService;
import com.bzdata.gestimospringbackend.repository.BienImmobilierRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service

@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BienImmobilierServiceImpl implements BienImmobilierService {
    final BienImmobilierRepository bienImmobilierRepository;

    @Override
    public List<BienImmobilierDto> findAll() {
        return bienImmobilierRepository.findAll().stream()
                .map(BienImmobilierDto::fromEntity)
                .collect(Collectors.toList());
    }

}