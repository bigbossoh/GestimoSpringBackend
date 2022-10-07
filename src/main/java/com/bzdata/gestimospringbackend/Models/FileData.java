package com.bzdata.gestimospringbackend.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

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
