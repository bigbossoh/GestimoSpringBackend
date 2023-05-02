package com.bzdata.gestimospringbackend.DTOs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryChambreSaveOrUpdateDto {
    Long id;
    Long idAgence;
     Long idCreateur;
    @NotNull(message = "La Description ne doit pas etre null")
    @NotEmpty(message = "La Description ne doit pas etre vide")
    @NotBlank(message = "La Description ne doit pas etre vide")
    String description;
    @NotNull(message = "Le Nom ne doit pas etre null")
    @NotEmpty(message = "Le Nom ne doit pas etre vide")
    @NotBlank(message = "Le Nom ne doit pas etre vide")
    String name;
    double price;
}
