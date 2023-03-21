package com.bzdata.gestimospringbackend.Models.hotel;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.bzdata.gestimospringbackend.Models.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ServicesHotel extends AbstractEntity {
   @ManyToOne
   ClientHotel clientHotel;
    @ManyToOne
    PrestationHotel prestationHotel;
  

}
