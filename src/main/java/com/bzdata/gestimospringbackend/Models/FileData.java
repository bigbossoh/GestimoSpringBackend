package com.bzdata.gestimospringbackend.Models;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FILE_DATA")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class FileData extends AbstractEntity{

    private String nameFileData;
    private String typeFileData;
    private String filePathFileData;
}
