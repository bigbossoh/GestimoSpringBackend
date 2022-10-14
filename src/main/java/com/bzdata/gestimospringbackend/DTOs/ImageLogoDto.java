package com.bzdata.gestimospringbackend.DTOs;

import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageLogoDto {
    Long idImage;
    private String nameImage;
    private String typeImage;
    private String profileAgenceImageUrl;

    private byte[] imageData;
    MultipartFile file;
    private Long agenceImmobiliere;
}
