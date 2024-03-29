package com.bzdata.gestimospringbackend.Models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
public class Charges extends AbstractEntity {
    int typeCharge;
    Date debutCharge;
    Date finCharge;
    double montantCharge;
    double augmentationCharge;
    double tauxCharge;

    @ManyToOne
    BailLocation bailLocataireCharge;

}
