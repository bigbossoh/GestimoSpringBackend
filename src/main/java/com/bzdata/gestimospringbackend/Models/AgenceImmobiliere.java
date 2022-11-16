package com.bzdata.gestimospringbackend.Models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AgenceImmobiliere extends AbstractEntity {
    String nomAgence;
    String telAgence;
    String compteContribuable;
    double capital;
    String emailAgence;
    String mobileAgence;
    String regimeFiscaleAgence;
    String faxAgence;
    String sigleAgence;
    String adresseAgence;

    @OneToMany(mappedBy = "logoAgence")
    List<ImageModel> imageModels;

}
