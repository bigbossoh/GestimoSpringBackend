package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Quartier;
import com.bzdata.gestimospringbackend.Models.Site;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SiteDto {
    Long id;
    String abrSite;
    String nomSite;
    Quartier quartier;
    public static SiteDto fromEntity(Site site) {
        if (site == null) {
            return null;
        }
        return SiteDto.builder()
                .id(site.getId())
                .abrSite(site.getAbrSite())
                .nomSite(site.getNomSite())
                .quartier(site.getQuartier())
                .build();
    }

    public static Site toEntity(SiteDto dto) {
        if (dto == null) {
            return null;
        }
        Site site = new Site();
        site.setId(dto.getId());
        site.setAbrSite(dto.getAbrSite());
        site.setNomSite(dto.getNomSite());
        site.setQuartier(dto.getQuartier());
        return site;
    }
}