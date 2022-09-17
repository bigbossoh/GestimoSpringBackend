package com.bzdata.gestimospringbackend.Models;

import javax.persistence.Entity;

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

    // @OneToMany(mappedBy = "agenceImmobilier")
    // List<Utilisateur> utilisateursAgence;
    // @ManyToOne
    // Utilisateur createur;

}
