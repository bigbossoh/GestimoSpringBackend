package com.bzdata.gestimospringbackend.Services;

import java.io.IOException;
import java.nio.file.Path;

import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;
import com.bzdata.gestimospringbackend.Models.ImageModel;

import org.springframework.web.multipart.MultipartFile;

public interface ImagesService {

    byte[] saveLogo(AgenceRequestDto logo) throws IOException;

    public byte[] getLogo(Long idAgence);

    ImageModel saveAgenceLogo(Long idAgence, MultipartFile file)throws IOException;

    Path fetchProfilePhotoByUser(Long idAgence) throws IOException;

    void saveProfilePhoto(MultipartFile file, Long idAgence) throws IOException;

    void saveFile(MultipartFile file, Long idAgence, Path rootLocation) throws IOException;

    // public byte[] decompressBytes(byte[] data);

    // public byte[] compressBytes(byte[] data);
}
