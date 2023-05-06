package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.AppartementDto;
import com.bzdata.gestimospringbackend.DTOs.ReservationAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.ReservationSaveOrUpdateDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Models.hotel.Reservation;
import com.bzdata.gestimospringbackend.Services.AppartementService;
import com.bzdata.gestimospringbackend.Services.ReservationService;
import com.bzdata.gestimospringbackend.Services.UtilisateurService;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationServiceImpl implements ReservationService {
    final UtilisateurService utilisateurService;
    final AppartementService appartementService;
    final ReservationRepository reservationRepository;
    final GestimoWebMapperImpl gestimoWebMapperImpl;
    @Override
    public Long save(ReservationSaveOrUpdateDto dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public List<ReservationAfficheDto> findAlGood() {
        return reservationRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Reservation::getCreationDate).reversed())
                .map(GestimoWebMapperImpl::fromReservation)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationAfficheDto findByIdGood(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if (reservation != null) {
            return GestimoWebMapperImpl.fromReservation(reservation);
        } else {
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if (reservation != null) {
            reservationRepository.delete(reservation);
        } else {
            throw new UnsupportedOperationException("No value to 'delete'");
        }
    }

    @Override
    public ReservationSaveOrUpdateDto saveOrUpdate(ReservationSaveOrUpdateDto dto) {

        AppartementDto appartementDto = appartementService.findById(dto.getIdAppartementdDto());

        UtilisateurAfficheDto newUtilisateurDto = new UtilisateurAfficheDto();

        if (dto.getId()==0 ) {
            Reservation nReservation = new Reservation();
            if (dto.getUtilisateurRequestDto().getId() == 0) {
                log.info("********* Utilisateur id est ******* : {}", dto.getUtilisateurRequestDto().getId());
                newUtilisateurDto = utilisateurService.saveUtilisateur(dto.getUtilisateurRequestDto());
                log.info("Le nouveau user est le meme {},{}", newUtilisateurDto.getId(),newUtilisateurDto.getNom());
                nReservation.setUtilisateurOperation(gestimoWebMapperImpl.toUtilisateur(newUtilisateurDto));
               //UtilisateurRequestDto uDto= utilisateurService.findUtilisateurByUsername(dto.getUtilisateurOperation());
                log.info("Le nouveau reservaion est  est le meme {},{}", nReservation.getUtilisateurOperation().getNom(),newUtilisateurDto.getNom());
            } else {
                nReservation.setUtilisateurOperation(gestimoWebMapperImpl
                        .toUtilisateur(utilisateurService.saveUtilisateur(dto.getUtilisateurRequestDto())));
            }
            nReservation.setIdAgence(dto.getIdAgence());
            nReservation.setIdCreateur(dto.getIdCreateur());
            nReservation.setAdvancePayment(dto.getAdvancePayment());
            nReservation.setDateDebut(dto.getDateDebut());
            nReservation.setDateFin(dto.getDateFin());
            nReservation.setNmbrEnfant(dto.getNmbrEnfant());
            nReservation.setNmbreFemme(dto.getNmbreFemme());
            nReservation.setNmbreHomme(dto.getNmbreHomme());
            nReservation.setRemainingPayment(dto.getRemainingPayment());
            nReservation.setSoldReservation(dto.getSoldReservation());
            nReservation.setAdvancePayment(dto.getAdvancePayment());
            nReservation.setBienImmobilierOperation(gestimoWebMapperImpl.fromAppartementDto(appartementDto));
            Reservation saveReservation = reservationRepository.save(nReservation);
            return null;//GestimoWebMapperImpl.fromReservation(saveReservation);
        } else {
            Reservation reservationTrouver = reservationRepository.getById(dto.getId());
            if (dto.getUtilisateurRequestDto().getId() == 0 ) {
                newUtilisateurDto = utilisateurService.saveUtilisateur(dto.getUtilisateurRequestDto());
                reservationTrouver.setUtilisateurOperation(gestimoWebMapperImpl.toUtilisateur(newUtilisateurDto));
            } else {
                reservationTrouver.setUtilisateurOperation(gestimoWebMapperImpl
                        .toUtilisateur(utilisateurService.saveUtilisateur(dto.getUtilisateurRequestDto())));
            }
            reservationTrouver.setIdAgence(dto.getIdAgence());
            reservationTrouver.setIdCreateur(dto.getIdCreateur());
            reservationTrouver.setAdvancePayment(dto.getAdvancePayment());
            reservationTrouver.setDateDebut(dto.getDateDebut());
            reservationTrouver.setDateFin(dto.getDateFin());
            reservationTrouver.setNmbrEnfant(dto.getNmbrEnfant());
            reservationTrouver.setNmbreFemme(dto.getNmbreFemme());
            reservationTrouver.setNmbreHomme(dto.getNmbreHomme());
            reservationTrouver.setRemainingPayment(dto.getRemainingPayment());
            reservationTrouver.setSoldReservation(dto.getSoldReservation());
            reservationTrouver.setAdvancePayment(dto.getAdvancePayment());
            reservationTrouver.setBienImmobilierOperation(gestimoWebMapperImpl.fromAppartementDto(appartementDto));
            Reservation saveReservation = reservationRepository.save(reservationTrouver);
            return null;//GestimoWebMapperImpl.fromReservation(saveReservation);
        }

    }

    @Override
    public ReservationAfficheDto saveOrUpdateGood(ReservationSaveOrUpdateDto dto) {
        Objects.requireNonNull(dto, "Le paramètre dto ne doit pas être nul");

        AppartementDto appartementDto = appartementService.findById(dto.getIdAppartementdDto());

        UtilisateurRequestDto utilisateurRequestDto = dto.getUtilisateurRequestDto();
        Objects.requireNonNull(utilisateurRequestDto, "Le paramètre utilisateurRequestDto ne doit pas être nul");

        Utilisateur utilisateur;

        if (utilisateurRequestDto.getId() == 0) {
            utilisateur = gestimoWebMapperImpl.fromUtilisateurRequestDto(utilisateurService.findUtilisateurByUsername(utilisateurRequestDto.getUsername()));
        } else {
            utilisateur = gestimoWebMapperImpl.toUtilisateur(utilisateurService.saveUtilisateur(utilisateurRequestDto));
        }

        Reservation reservation;

        if (dto.getId() == 0) {
            reservation = new Reservation();
        } else {
            reservation = reservationRepository.getById(dto.getId());
        }

        reservation.setUtilisateurOperation(utilisateur);
        reservation.setIdAgence(dto.getIdAgence());
        reservation.setIdCreateur(dto.getIdCreateur());
        reservation.setAdvancePayment(dto.getAdvancePayment());
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setNmbrEnfant(dto.getNmbrEnfant());
        reservation.setNmbreFemme(dto.getNmbreFemme());
        reservation.setNmbreHomme(dto.getNmbreHomme());
        reservation.setRemainingPayment(dto.getRemainingPayment());
        reservation.setSoldReservation(dto.getSoldReservation());
        reservation.setBienImmobilierOperation(gestimoWebMapperImpl.fromAppartementDto(appartementDto));

        Reservation saveReservation = reservationRepository.save(reservation);
        return GestimoWebMapperImpl.fromReservation(saveReservation);
    }

    @Override
    public List<ReservationSaveOrUpdateDto> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public ReservationSaveOrUpdateDto findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

}
