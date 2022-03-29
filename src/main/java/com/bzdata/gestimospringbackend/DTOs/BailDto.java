package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.BailLocation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BailDto {
    Long idBail;
    Long idUtilisateur;

    public static BailDto fromEntity(BailLocation bailLocation) {
        if (bailLocation == null) {
            return null;
        }
        return BailDto.builder()
                .idBail(bailLocation.getId())
                .idUtilisateur(bailLocation.getUtilisateurOperation().getId()).build();
    }

}
