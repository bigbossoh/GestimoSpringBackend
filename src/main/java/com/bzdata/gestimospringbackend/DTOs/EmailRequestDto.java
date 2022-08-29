package com.bzdata.gestimospringbackend.DTOs;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequestDto {
     String from;
     String to;
     String body;
     String subject;
     String[] toList;
     String fileToAttache;
}
