package com.bzdata.gestimospringbackend.Models.hotel;

import com.bzdata.gestimospringbackend.Models.EncaissementPrincipal;
import com.bzdata.gestimospringbackend.Models.Operation;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@DiscriminatorValue("Reservation")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reservation extends Operation {

  double montantDeReservation;
  double pourcentageReduction;
  double montantReduction;
  double soldReservation;
  double montantPaye;
  int nmbreAdulte;
  int nmbrEnfant;
  String statutReservation;

  @OneToMany
  @JoinColumn(name = "idResvationServiceAdditionnel")
  List<PrestationAdditionnelReservation> serviceAdditionnelreservations;

  @OneToMany
  @JoinColumn(name = "idResvationEncaissement")
  List<EncaissementPrincipal> encaisssementsreservation;
}
