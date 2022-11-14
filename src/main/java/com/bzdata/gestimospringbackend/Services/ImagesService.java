package com.bzdata.gestimospringbackend.Services;

import java.io.IOException;

import com.bzdata.gestimospringbackend.DTOs.AgenceRequestDto;

public interface ImagesService {

    byte[] saveLogo(AgenceRequestDto logo) throws IOException;

    public byte[] getLogo(Long idAgence);

    // public byte[] decompressBytes(byte[] data);

    // public byte[] compressBytes(byte[] data);
}
