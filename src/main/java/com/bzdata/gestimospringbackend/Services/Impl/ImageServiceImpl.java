package com.bzdata.gestimospringbackend.Services.Impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import com.bzdata.gestimospringbackend.Models.ImageModel;
import com.bzdata.gestimospringbackend.Services.ImagesService;
import com.bzdata.gestimospringbackend.Utils.ImageUtility;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.AgenceImmobiliereRepository;
import com.bzdata.gestimospringbackend.repository.ImageRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    final AgenceImmobiliereRepository agenceImmobiliereRepository;
    final ImageRepository imageRepository;

    @Override
    public byte[] saveLogo(AgenceRequestDto dto) throws IOException {
        System.out.println("************ le logo ****************************");
        log.info(" Le payload recu est le suivant : {}{}", dto.getId(), dto.getLogoAgence());
        System.out.println("****************************--------******");
        final byte[] lesFile = dto.getLogoAgence().getBytes();
        AgenceImmobiliere findAgence = agenceImmobiliereRepository.findById(dto.getId())
                .orElseThrow(() -> new InvalidEntityException("Aucun Agence has been found with Code " + dto.getId(),
                        ErrorCodes.AGENCE_NOT_FOUND));
        // findAgence.set(ImageUtility.compressImage(lesFile));
        agenceImmobiliereRepository.save(findAgence);
        return null;// ImageUtility.decompressImage(findAgence.getLogoAgence());

    }

    @Override
    public byte[] getLogo(Long idAgence) {
        AgenceImmobiliere findAgence = agenceImmobiliereRepository.findById(idAgence).orElse(null);
        ImageModel imgFound = imageRepository.findByLogoAgence(findAgence).orElse(null);

        return ImageUtility.decompressImage(imgFound.getPicByte());

    }

    @Override
    public ImageModel saveAgenceLogo(Long idAgence, MultipartFile file) throws IOException {
        System.out.println("original Image Byte Size - " + file.getBytes().length);
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereRepository.findById(idAgence).orElse(null);
        ImageModel img = new ImageModel();
        img.setLogoAgence(agenceImmobiliere);
        img.setName(file.getOriginalFilename());
        img.setType(file.getContentType());
        img.setPicByte(ImageUtility.compressImage(file.getBytes()));

        return imageRepository.save(img);
    }

    @Override
    public Path fetchProfilePhotoByUser(Long idAgence) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveProfilePhoto(MultipartFile file, Long idAgence) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveFile(MultipartFile file, Long idAgence, Path rootLocation) throws IOException {
        // TODO Auto-generated method stub

    }

}
