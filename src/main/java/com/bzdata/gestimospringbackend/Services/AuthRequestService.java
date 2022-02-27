package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.Auth.AuthRequestDto;
import com.bzdata.gestimospringbackend.DTOs.Auth.AuthResponseDto;

public interface AuthRequestService {

    AuthResponseDto authenticate( AuthRequestDto dto);
}
