package com.bzdata.gestimospringbackend.Models;

import java.time.LocalDate;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CronMail extends AbstractEntity{
    LocalDate nextDateMail;
    int dayOfMonth;
    boolean isDonne;
}
