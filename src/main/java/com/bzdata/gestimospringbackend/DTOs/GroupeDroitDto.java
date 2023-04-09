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
public class GroupeDroitDto {
    Long id;
    Long idAgence;
    Long idCreateur;
    @NotNull(message = "Le groupeDroit ne doit pas etre null")
    @NotEmpty(message = "Le groupeDroit ne doit pas etre vide")
    @NotBlank(message = "Le groupeDroit ne doit pas etre vide")
    String groupeDroit;
}
