package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.ServiceAditionnelSaveOrUpdateDto;
import com.bzdata.gestimospringbackend.Models.hotel.ServiceAdditionnelle;
import com.bzdata.gestimospringbackend.Services.ServiceAdditionnelService;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.ServiceAdditionnelRepository;
import com.bzdata.gestimospringbackend.validator.ObjectsValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceAdditionnelServiceImpl implements ServiceAdditionnelService{
    final ServiceAdditionnelRepository serviceAdditionnelRepository;

    private final ObjectsValidator<ServiceAditionnelSaveOrUpdateDto> validator;

    @Override
    public Long save(ServiceAditionnelSaveOrUpdateDto dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public List<ServiceAditionnelSaveOrUpdateDto> findAll() {
        return serviceAdditionnelRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(ServiceAdditionnelle::getName))
                .map(GestimoWebMapperImpl::fromServiceAditionnel)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceAditionnelSaveOrUpdateDto findById(Long id) {
        ServiceAdditionnelle serviceAdditionnelle = serviceAdditionnelRepository.findById(id).orElse(null);
        if (serviceAdditionnelle != null) {
            return GestimoWebMapperImpl.fromServiceAditionnel(serviceAdditionnelle);
        } else {
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        ServiceAdditionnelle serviceAdditionnelle = serviceAdditionnelRepository.findById(id).orElse(null);
        if (serviceAdditionnelle != null) {
            serviceAdditionnelRepository.delete(serviceAdditionnelle);
        } else {
            throw new UnsupportedOperationException("Unimplemented method 'delete'");
        }
    }

    @Override
    public ServiceAditionnelSaveOrUpdateDto saveOrUpdate(ServiceAditionnelSaveOrUpdateDto dto) {
        ServiceAdditionnelle serviceAdditionnelle = serviceAdditionnelRepository.findById(dto.getId()).orElse(null);

        validator.validate(dto);
        if (serviceAdditionnelle != null) {
            //serviceAdditionnelle.setType(dto.g());
            serviceAdditionnelle.setName(dto.getName());
            serviceAdditionnelle.setAmount(dto.getAmount());
            serviceAdditionnelle.setIdAgence(dto.getIdAgence());
            serviceAdditionnelle.setIdCreateur(dto.getIdCreateur());
            ServiceAdditionnelle saveServiceAdditionnelle = serviceAdditionnelRepository.save(serviceAdditionnelle);
            return GestimoWebMapperImpl.fromServiceAditionnel(saveServiceAdditionnelle);
        } else {
            ServiceAdditionnelle newsServiceAdditionnelle = new ServiceAdditionnelle();
           // newCategorieChambre.setDescription(dto.getDescription());
           newsServiceAdditionnelle.setName(dto.getName());
           newsServiceAdditionnelle.setAmount(dto.getAmount());
           newsServiceAdditionnelle.setIdCreateur(dto.getIdCreateur());
           newsServiceAdditionnelle.setIdAgence(dto.getIdAgence());
           ServiceAdditionnelle saveServiceAdditionnelle = serviceAdditionnelRepository.save(newsServiceAdditionnelle);
            return GestimoWebMapperImpl.fromServiceAditionnel(saveServiceAdditionnelle);
        }
    }

}
