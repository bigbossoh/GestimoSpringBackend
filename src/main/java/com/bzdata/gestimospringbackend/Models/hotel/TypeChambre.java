package com.bzdata.gestimospringbackend.Models.hotel;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.bzdata.gestimospringbackend.Models.AbstractEntity;

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
@FieldDefaults(level = AccessLevel.PRIVATE)

public class TypeChambre extends AbstractEntity {
    String name;
    double price;
}
