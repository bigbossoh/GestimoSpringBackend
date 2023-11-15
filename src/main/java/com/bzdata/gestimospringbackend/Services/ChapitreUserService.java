package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.ChapitreUserDto;

public interface ChapitreUserService extends AbstractService<ChapitreUserDto> {
   ChapitreUserDto  saveorUpdateChapitreUser(ChapitreUserDto dto);
}
