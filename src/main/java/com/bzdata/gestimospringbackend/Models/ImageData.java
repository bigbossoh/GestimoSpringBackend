package com.bzdata.gestimospringbackend.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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