package com.bzdata.gestimospringbackend.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ImageData")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ImageData extends AbstractEntity{

    private String nameImage;
    private String typeImage;
    private String profileAgenceImageUrl;
    @Lob
    @Column(name = "imagedata",length = 1500)
    private byte[] imageData;
    @OneToOne(mappedBy = "imageData")
    private AgenceImmobiliere agenceImmobiliere;
}