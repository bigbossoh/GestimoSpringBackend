package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.OperationDto;
import com.bzdata.gestimospringbackend.Services.OperationService;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.OperationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperationServiceImpl implements OperationService {
    final BailLocationRepository bailLocationRepository;

    @Override
    public List<OperationDto> getAllOperation() {

        return bailLocationRepository.findAll().stream()
                .map(OperationDto::fromEntity)
                .collect(Collectors.toList());

    }

}
