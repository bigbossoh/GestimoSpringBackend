package com.bzdata.gestimospringbackend.Models.hotel;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

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
public class Prestation  extends AbstractEntity{
    String name;
    double amount;
    String type;
    @OneToMany
    @JoinColumn(name = "idServiceAdditionnel")
    List<PrestationAdditionnelReservation>serviceAddits;
}
