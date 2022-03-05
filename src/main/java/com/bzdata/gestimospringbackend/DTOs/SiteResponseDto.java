package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Site;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SiteResponseDto {
    Long id;
    String abrSite;
    String nomSite;
    QuartierDto quartierDto;
    public static SiteResponseDto fromEntity(Site site) {
        if (site == null) {
            return null;
        }
        return SiteResponseDto.builder()
                .id(site.getId())
                .abrSite(org.apache.commons.lang3.StringUtils.deleteWhitespace(site.getQuartier().getCommune().getVille().getPays().getAbrvPays())
               +"-"+org.apache.commons.lang3.StringUtils.deleteWhitespace(site.getQuartier().getCommune().getVille().getAbrvVille())
               +"-"+org.apache.commons.lang3.StringUtils.deleteWhitespace(site.getQuartier().getCommune().getAbrvCommune())
               +"-"+org.apache.commons.lang3.StringUtils.deleteWhitespace(site.getQuartier().getAbrvQuartier())
                        )
                .nomSite(       site.getQuartier().getCommune().getVille().getPays().getNomPays()
                                +"-"+site.getQuartier().getCommune().getVille().getNomVille()
                                +"-"+site.getQuartier().getCommune().getNomCommune()
                                +"-"+site.getQuartier().getNomQuartier()
                        )
                .quartierDto(QuartierDto.fromEntity(site.getQuartier()))
                .build();
    }

    public static Site toEntity(SiteResponseDto dto) {
        if (dto == null) {
            return null;
        }
        Site site = new Site();
        site.setId(dto.getId());
        site.setAbrSite(dto.getAbrSite());
        site.setNomSite(dto.getNomSite());
        site.setQuartier(QuartierDto.toEntity(dto.getQuartierDto()));
        return site;
    }
}
