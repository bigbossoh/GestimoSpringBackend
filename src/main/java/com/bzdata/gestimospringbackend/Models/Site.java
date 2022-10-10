package com.bzdata.gestimospringbackend.Models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Site extends AbstractEntity {
    String abrSite;
    String nomSite;
    @ManyToOne
    Quartier quartier;
//    @OneToMany(mappedBy = "site")
//    List<Bienimmobilier> bienImmobiliers;
}
