package com.bzdata.gestimospringbackend.Models;

import java.util.List;

import javax.persistence.Entity;
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
public class BailLocation extends Operation {
    String designationBail;
    String abrvCodeBail;
    boolean enCoursBail;
    double montantCautionBail;
    int nbreMoisCautionBail;
    @OneToMany(mappedBy = "bailLocataire")
    List<MontantLoyerBail> montantLoyerBails;
    @OneToMany(mappedBy = "bailLocataireCharge")
    List<Charges> charges;
    @OneToMany(mappedBy = "bailLocationAppelLoyer")
    List<AppelLoyer> appeldableBAppelLoyers;
}
