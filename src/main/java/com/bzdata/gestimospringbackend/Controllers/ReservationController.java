package com.bzdata.gestimospringbackend.Controllers;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.ReservationAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.ReservationSaveOrUpdateDto;
import com.bzdata.gestimospringbackend.Services.ReservationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(APP_ROOT + "/reservation")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "gestimoapi")
public class ReservationController {
    final ReservationService reservationService;
    @PostMapping("/saveorupdate")
    @Operation(summary = "Creation et mise à jour d'une Reservation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ReservationSaveOrUpdateDto> saveorupdate(
            @RequestBody ReservationSaveOrUpdateDto dto) {

        return ResponseEntity.ok(reservationService.saveOrUpdate(dto));
    }

    @PostMapping("/saveorupdategood")
    @Operation(summary = "Creation et mise à jour d'une Reservation bon", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ReservationAfficheDto> saveorupdategood(
            @RequestBody ReservationSaveOrUpdateDto dto) {

        return ResponseEntity.ok(reservationService.saveOrUpdateGood(dto));
    }

      // SUPPRESSION D'UNE COMMUNE
      @Operation(summary = "Suppression d'une Reservation avec l'ID en paramètre", security = @SecurityRequirement(name = "bearerAuth"))
      @DeleteMapping("/delete/{id}")
      public ResponseEntity<Void> deleteCommune(@PathVariable("id") Long id) {

          reservationService.delete(id);
          return ResponseEntity.ok().build();
      }
      @Operation(summary = "Trouver une Reservation par son ID", security = @SecurityRequirement(name = "bearerAuth"))
      @GetMapping("/findById/{id}")
      public ResponseEntity<ReservationSaveOrUpdateDto> findCategorieChambreByID(@PathVariable("id") Long id) {

          return ResponseEntity.ok(reservationService.findById(id));
      }
      // TOUTES LES RESERVATION
    @Operation(summary = "Liste de toutes les Reservation", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<List<ReservationSaveOrUpdateDto>> findAllCategorieChambre() {
        return ResponseEntity.ok(reservationService.findAll());
    }
}
