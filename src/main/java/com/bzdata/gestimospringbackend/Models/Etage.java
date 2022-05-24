package com.bzdata.gestimospringbackend.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Etage extends AbstractEntity {
    private String nomEtage;
    private String AbrvEtage;
    private int numEtage;
    @OneToMany(mappedBy = "etageAppartement")
    private List<Appartement> appartements;
    @OneToMany(mappedBy = "etageStudio")
    private List<Studio> studios;
    @OneToMany(mappedBy = "etageMagasin")
    private List<Magasin> magasins;
    @ManyToOne
    private Immeuble immeuble;

}
