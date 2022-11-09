package com.bzdata.gestimospringbackend.Services.Impl;

import java.io.IOException;
import java.util.Optional;

import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import com.bzdata.gestimospringbackend.Services.ImagesService;
import com.bzdata.gestimospringbackend.Utils.ImageUtility;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.AgenceImmobiliereRepository;

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
public class ImageServiceImpl implements ImagesService {
    private final AgenceImmobiliereRepository agenceImmobiliereRepository;

    @Override
    public Boolean saveLogo(AgenceRequestDto dto) throws IOException {
        log.info(" Le payload recu est le suivant : {} ", dto.getId());
        AgenceImmobiliere findAgence = agenceImmobiliereRepository.findById(dto.getId())
                .orElseThrow(() -> new InvalidEntityException("Aucun Agence has been found with Code " + dto.getId(),
                        ErrorCodes.AGENCE_NOT_FOUND));
        findAgence.setLogoAgence(ImageUtility.compressImage(dto.getLogoAgence()));
        agenceImmobiliereRepository.save(findAgence);
        return true;
    }

    @Override
    public byte[] getLogo(Long idAgence) {
        Optional<AgenceImmobiliere> findAgence = agenceImmobiliereRepository.findById(idAgence);
        return ImageUtility.decompressImage(findAgence.get().getLogoAgence());

    }

}
