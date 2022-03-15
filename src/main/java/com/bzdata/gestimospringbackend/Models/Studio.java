package com.bzdata.gestimospringbackend.Models;

import java.util.List;

import javax.persistence.Entity;
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
public class Studio extends AbstractEntity {
    String descStudio;
    int numeroStudio;
    String abrvNomStudio;
    String nomStudio;
    @ManyToOne
    Etage etageStudio;
    @OneToMany(mappedBy = "studioBail")
    List<Operation> operationsStudio;
}
