package com.bzdata.gestimospringbackend.mappers;

import javax.transaction.Transactional;


import org.springframework.stereotype.Service;

import com.bzdata.gestimospringbackend.DTOs.GroupeDroitDto;
import com.bzdata.gestimospringbackend.Models.GroupeDroit;
import org.springframework.beans.BeanUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupeDroitMapperImpl {

    public GroupeDroit fromGroupeDroitDto(GroupeDroitDto dto){
        GroupeDroit groupeDroit = new GroupeDroit();
        BeanUtils.copyProperties(dto, groupeDroit);
        return groupeDroit; 
    }
    public GroupeDroitDto fromGroupeDroit(GroupeDroit groupeDroit){
        GroupeDroitDto groupeDroitDto = new GroupeDroitDto();
        BeanUtils.copyProperties(groupeDroit, groupeDroitDto);
        return groupeDroitDto; 
    }
    
}
