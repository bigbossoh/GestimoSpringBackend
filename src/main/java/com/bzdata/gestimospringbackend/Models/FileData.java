package com.bzdata.gestimospringbackend.Models;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "FILE_DATA")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class FileData extends AbstractEntity{
    private String nameFileData;
    private String typeFileData;
    private String filePathFileData;
}
