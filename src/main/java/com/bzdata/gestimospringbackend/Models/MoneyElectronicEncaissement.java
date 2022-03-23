package com.bzdata.gestimospringbackend.Models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("mobile")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MoneyElectronicEncaissement extends Encaissement {
    int numeroTelephone;
}
