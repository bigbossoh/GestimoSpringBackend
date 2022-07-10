package com.bzdata.gestimospringbackend.Models;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
@DiscriminatorValue("Magasin")
public class Magasin extends Bienimmobilier {
    boolean isUnderBuildingMagasin;
    int nmbrPieceMagasin;
    String nomMagasin;
    String abrvNomMagasin;
    @ManyToOne(fetch = FetchType.LAZY)
    Etage etageMagasin;
    @OneToMany(mappedBy = "magasinBail")
    List<Operation> operationsMagasin;
    
}
