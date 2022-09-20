package com.bzdata.gestimospringbackend.mappers;

import com.bzdata.gestimospringbackend.DTOs.BailVillaDto;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class BailMapperImpl {
    public BailVillaDto fromVilla(BailLocation bailLocation){
        BailVillaDto bailLocaDto= new BailVillaDto();
        BeanUtils.copyProperties(bailLocation, bailLocaDto);
        bailLocaDto.setIdBienImmobilier(bailLocation.getBienImmobilierOperation().getId());
        bailLocaDto.setFullNomLocatire(bailLocation.getUtilisateurOperation().getNom()+" "+bailLocation.getUtilisateurOperation().getPrenom());
        bailLocaDto.setIdLocataire(bailLocation.getUtilisateurOperation().getId());
        return bailLocaDto;
    }
}
