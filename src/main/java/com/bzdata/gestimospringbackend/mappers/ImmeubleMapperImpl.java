package com.bzdata.gestimospringbackend.mappers;

import com.bzdata.gestimospringbackend.DTOs.ImmeubleEtageDto;
import com.bzdata.gestimospringbackend.Models.Bienimmobilier;
import com.bzdata.gestimospringbackend.Models.Immeuble;
import com.bzdata.gestimospringbackend.Models.Site;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.repository.SiteRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImmeubleMapperImpl {

    final SiteRepository siteRepository;
    final UtilisateurRepository utilisateurRepository;

    public ImmeubleEtageDto fromImmeubleEtage(Immeuble immeuble) {
        ImmeubleEtageDto immeubleEtageDto = new ImmeubleEtageDto();
        BeanUtils.copyProperties(immeuble, immeubleEtageDto);
        immeubleEtageDto.setIdSite(immeuble.getSite().getId());
        immeubleEtageDto.setIdUtilisateur(immeuble.getUtilisateur().getId());
        return immeubleEtageDto;
    }

    public Immeuble fromImmeubleEtageDto(ImmeubleEtageDto immeubleEtageDto){
        Immeuble immeuble=new Immeuble();
        BeanUtils.copyProperties(immeubleEtageDto, immeuble);
        if(immeubleEtageDto.getIdSite()!=null){
            Site site = siteRepository.findById(immeubleEtageDto.getIdSite()).orElse(null);
            if(site !=null)
                immeuble.setSite(site);

        }
        if(immeubleEtageDto.getIdUtilisateur()!=null){
            Utilisateur utilisateur= utilisateurRepository.findById(immeubleEtageDto.getIdUtilisateur()).orElse(null);
            if(utilisateur!=null)
                immeuble.setUtilisateur(utilisateur);
        }
        return immeuble;
    }
}
