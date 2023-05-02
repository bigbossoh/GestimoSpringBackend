package com.bzdata.gestimospringbackend.DTOs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceAditionnelSaveOrUpdateDto {
    Long id;
    Long idAgence;
    Long idCreateur;
    @NotNull(message = "La Description ne doit pas etre null")
    @NotEmpty(message = "La Description ne doit pas etre vide")
    @NotBlank(message = "La Description ne doit pas etre vide")
    String name;
    double amount;

}
