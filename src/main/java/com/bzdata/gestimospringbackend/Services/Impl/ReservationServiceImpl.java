package com.bzdata.gestimospringbackend.Services.Impl;

import static com.bzdata.gestimospringbackend.enumeration.Role.*;

import com.bzdata.gestimospringbackend.DTOs.AppartementDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;
import com.bzdata.gestimospringbackend.DTOs.EncaissementReservationDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementReservationRequestDto;
import com.bzdata.gestimospringbackend.DTOs.ReservationAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.ReservationRequestDto;
import com.bzdata.gestimospringbackend.DTOs.ReservationSaveOrUpdateDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.Appartement;
import com.bzdata.gestimospringbackend.Models.EncaissementPrincipal;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Models.hotel.EncaissementReservation;
import com.bzdata.gestimospringbackend.Models.hotel.Reservation;
import com.bzdata.gestimospringbackend.Services.AppartementService;
import com.bzdata.gestimospringbackend.Services.ClotureCaisseService;
import com.bzdata.gestimospringbackend.Services.EncaissementReservationService.SaveEncaissementReservationAvecRetourDeListService;
import com.bzdata.gestimospringbackend.Services.ReservationService;
import com.bzdata.gestimospringbackend.Services.UtilisateurService;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.AppartementRepository;
import com.bzdata.gestimospringbackend.repository.EncaissementPrincipalRepository;
import com.bzdata.gestimospringbackend.repository.ReservationRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationServiceImpl implements ReservationService {

  final UtilisateurService utilisateurService;
  final AppartementService appartementService;
  final ReservationRepository reservationRepository;
  final GestimoWebMapperImpl gestimoWebMapperImpl;
  final UtilisateurRepository utilisateurRepository;
  final AppartementRepository appartementRepository;
  final EncaissementPrincipalRepository encaissementPrincipalRepository;
  final SaveEncaissementReservationAvecRetourDeListService saveEncaissementReservationAvecRetourDeListService;

  @Override
  public Long save(ReservationSaveOrUpdateDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'save'");
  }

  @Override
  public List<ReservationAfficheDto> findAlGood() {
    return reservationRepository
      .findAll()
      .stream()
      .map(gestimoWebMapperImpl::fromReservation)
      .filter(res -> res.getIdAgence() == 1)
      .collect(Collectors.toList());
  }

  @Override
  public ReservationAfficheDto findByIdGood(Long id) {
    Reservation reservation = reservationRepository.findById(id).orElse(null);
    if (reservation != null) {
      return gestimoWebMapperImpl.fromReservation(reservation);
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
  public ReservationSaveOrUpdateDto saveOrUpdate(
    ReservationSaveOrUpdateDto dto
  ) {
    AppartementDto appartementDto = appartementService.findById(
      dto.getIdAppartementdDto()
    );
    UtilisateurAfficheDto newUtilisateurDto = new UtilisateurAfficheDto();
    if (dto.getId() == 0) {
      Reservation nReservation = new Reservation();
      if (dto.getUtilisateurRequestDto().getId() == 0) {
        newUtilisateurDto =
          utilisateurService.saveUtilisateur(dto.getUtilisateurRequestDto());
        log.info(
          "Le nouveau user est le meme {},{}",
          newUtilisateurDto.getId(),
          newUtilisateurDto.getNom()
        );
        nReservation.setUtilisateurOperation(
          gestimoWebMapperImpl.toUtilisateur(newUtilisateurDto)
        );
        //UtilisateurRequestDto uDto= utilisateurService.findUtilisateurByUsername(dto.getUtilisateurOperation());

      } else {
        nReservation.setUtilisateurOperation(
          gestimoWebMapperImpl.toUtilisateur(
            utilisateurService.saveUtilisateur(dto.getUtilisateurRequestDto())
          )
        );
      }

      nReservation.setIdAgence(dto.getIdAgence());
      // nReservation.setIdCreateur(dto.getIdCreateur());
      // nReservation.setAdvancePayment(dto.getmo());
      // nReservation.setDateDebut(dto.getDateDebut());
      // nReservation.setDateFin(dto.getDateFin());
      // nReservation.setNmbrEnfant(dto.getNmbrEnfant());
      // nReservation.setNmbreFemme(dto.getNmbreFemme());
      // nReservation.setNmbreHomme(dto.getNmbreHomme());
      // nReservation.setRemainingPayment(dto.getRemainingPayment());
      // nReservation.setSoldReservation(dto.getSoldReservation());
      // nReservation.setAdvancePayment(dto.getAdvancePayment());
      nReservation.setBienImmobilierOperation(
        gestimoWebMapperImpl.fromAppartementDto(appartementDto)
      );
      Reservation saveReservation = reservationRepository.save(nReservation);
      return null; //GestimoWebMapperImpl.fromReservation(saveReservation);
    } else {
      Reservation reservationTrouver = reservationRepository.getById(
        dto.getId()
      );
      if (dto.getUtilisateurRequestDto().getId() == 0) {
        newUtilisateurDto =
          utilisateurService.saveUtilisateur(dto.getUtilisateurRequestDto());
        reservationTrouver.setUtilisateurOperation(
          gestimoWebMapperImpl.toUtilisateur(newUtilisateurDto)
        );
      } else {
        reservationTrouver.setUtilisateurOperation(
          gestimoWebMapperImpl.toUtilisateur(
            utilisateurService.saveUtilisateur(dto.getUtilisateurRequestDto())
          )
        );
      }
      reservationTrouver.setIdAgence(dto.getIdAgence());
      reservationTrouver.setIdCreateur(dto.getIdCreateur());
      // reservationTrouver.setAdvancePayment(dto.getAdvancePayment());
      // reservationTrouver.setDateDebut(dto.getDateDebut());
      // reservationTrouver.setDateFin(dto.getDateFin());
      // reservationTrouver.setNmbrEnfant(dto.getNmbrEnfant());
      // reservationTrouver.setNmbreFemme(dto.getNmbreFemme());
      // reservationTrouver.setNmbreHomme(dto.getNmbreHomme());
      // reservationTrouver.setRemainingPayment(dto.getRemainingPayment());
      // reservationTrouver.setSoldReservation(dto.getSoldReservation());
      // reservationTrouver.setAdvancePayment(dto.getAdvancePayment());
      reservationTrouver.setBienImmobilierOperation(
        gestimoWebMapperImpl.fromAppartementDto(appartementDto)
      );
      Reservation saveReservation = reservationRepository.save(
        reservationTrouver
      );
      return null; //GestimoWebMapperImpl.fromReservation(saveReservation);
    }
  }

  @Override
  public ReservationAfficheDto saveOrUpdateGood(ReservationRequestDto dto) {
    Objects.requireNonNull(dto, "Le paramètre dto ne doit pas être nul");
    AppartementDto appartementDto = appartementService.findById(
      dto.getIdAppartementdDto()
    );
    Appartement saveApp = appartementRepository
      .findById(dto.getIdAppartementdDto())
      .orElse(null);
    if (saveApp != null) {
      saveApp.setOccupied(true);
      appartementRepository.save(saveApp);
    }
    Utilisateur utilisateurRequestDto = utilisateurRepository
      .findById(dto.getIdUtilisateur())
      .orElse(null);
    Utilisateur utilisateur;

    if (utilisateurRequestDto.getId() == 0) {
      utilisateur =
        gestimoWebMapperImpl.fromUtilisateurRequestDto(
          utilisateurService.findUtilisateurByUsername(
            utilisateurRequestDto.getUsername()
          )
        );
    } else {
      utilisateur = utilisateurRepository.save(utilisateurRequestDto);
    }

    Reservation reservation;

    if (dto.getId() == 0) {
      reservation = new Reservation();
    } else {
      reservation = reservationRepository.getById(dto.getId());
    }
    LocalDate dateDebutLocalDate = LocalDate.parse(
      dto.getDateDebut(),
      DateTimeFormatter.ofPattern("yyyy-MM-dd")
    );
    LocalDate dateFinLocalDate = LocalDate.parse(
      dto.getDateFin(),
      DateTimeFormatter.ofPattern("yyyy-MM-dd")
    );
    reservation.setUtilisateurOperation(utilisateur);
    reservation.setIdAgence(dto.getIdAgence());
    reservation.setIdCreateur(dto.getIdCreateur());
    reservation.setMontantPaye(dto.getMontantPaye());
    reservation.setDateDebut(dateDebutLocalDate);
    reservation.setDateFin(dateFinLocalDate);
    reservation.setNmbrEnfant(dto.getNmbrEnfant());
    reservation.setNmbreAdulte(dto.getNmbreAdulte());
    // reservation.setNmbreHomme(dto.getNmbreHomme());
    reservation.setMontantReduction(dto.getMontantReduction());
    reservation.setPourcentageReduction(dto.getPourcentageReduction());
    // reservation.setst
    reservation.setSoldReservation(dto.getSoldReservation());
    reservation.setBienImmobilierOperation(
      gestimoWebMapperImpl.fromAppartementDto(appartementDto)
    );

    Reservation saveReservation = reservationRepository.save(reservation);
    return gestimoWebMapperImpl.fromReservation(saveReservation);
  }

  @Override
  public List<ReservationSaveOrUpdateDto> findAll() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findAll'");
  }

  @Override
  public ReservationSaveOrUpdateDto findById(Long id) {
    throw new UnsupportedOperationException("Unimplemented method 'findById'");
  }

  @Override
  public boolean saveOrUpdateReservation(ReservationRequestDto dto) {
    Objects.requireNonNull(dto, "Le paramètre dto ne doit pas être nul");

    AppartementDto appartementDto = appartementService.findById(
      dto.getIdAppartementdDto()
    );
    Appartement saveApp = appartementRepository
      .findById(dto.getIdAppartementdDto())
      .orElse(null);

    log.info("DTO de USER NAME II {}", dto.getUsername());

    Utilisateur utilisateurSave = utilisateurRepository.findUtilisateurByMobile(
      dto.getUsername()
    );

    Reservation reservation;

    if (dto.getId() == 0) {
      reservation = new Reservation();
    } else {
      reservation = reservationRepository.getById(dto.getId());
    }

    LocalDate dateDebutLocalDate = LocalDate.parse(
      dto.getDateDebut(),
      DateTimeFormatter.ofPattern("yyyy-MM-dd")
    );
    LocalDate dateFinLocalDate = LocalDate.parse(
      dto.getDateFin(),
      DateTimeFormatter.ofPattern("yyyy-MM-dd")
    );
    reservation.setUtilisateurOperation(utilisateurSave);
    reservation.setIdAgence(dto.getIdAgence());
    reservation.setIdCreateur(dto.getIdCreateur());
    reservation.setMontantPaye(dto.getMontantPaye());
    reservation.setDateDebut(dateDebutLocalDate);
    reservation.setDateFin(dateFinLocalDate);
    reservation.setNmbrEnfant(dto.getNmbrEnfant());
    reservation.setNmbreAdulte(dto.getNmbreAdulte());
    reservation.setMontantDeReservation(dto.getMontantDeReservation());
    reservation.setMontantReduction(dto.getMontantReduction());
    reservation.setPourcentageReduction(dto.getPourcentageReduction());
    reservation.setSoldReservation(dto.getSoldReservation());
    if (dto.getSoldReservation() == 0) {
      if (saveApp != null) {
        saveApp.setOccupied(false);
        appartementRepository.save(saveApp);
      }
      appartementRepository.save(saveApp);
      reservation.setStatutReservation("Ferme");
    } else {
      if (saveApp != null) {
        saveApp.setOccupied(true);
        appartementRepository.save(saveApp);
      }
      appartementRepository.save(saveApp);
      reservation.setStatutReservation("Ouvert");
    }
    reservation.setBienImmobilierOperation(
      gestimoWebMapperImpl.fromAppartementDto(appartementDto)
    );

    Reservation saveReservation = reservationRepository.save(reservation);
    log.info(
      "Affiche DTO {}",
      gestimoWebMapperImpl.fromReservation(saveReservation)
    );
    EncaissementReservationRequestDto encaissementReservation = new EncaissementReservationRequestDto();
    encaissementReservation.setIdReservation(saveReservation.getId());
    // encaissementReservation.setReservationAfficheDto(
    //   gestimoWebMapperImpl.fromReservation(saveReservation)
    // );
    encaissementReservation.setDateEncaissement(LocalDate.now());
    encaissementReservation.setEncienSoldReservation(
      dto.getMontantDeReservation() + dto.getSoldReservation()
    );
    //encaissementReservation.setEntiteOperation();
    encaissementReservation.setIdAgence(dto.getIdAgence());
    encaissementReservation.setIdCreateur(dto.getIdCreateur());
    // encaissementReservation.setModePaiement("ESPECE");
    encaissementReservation.setMontantEncaissement(
      dto.getMontantDeReservation()
    );
    encaissementReservation.setNvoSoldeReservation(dto.getSoldReservation());
   
    List<EncaissementReservationDto> encaissements = saveEncaissementReservationAvecRetourDeListService.saveEncaissementReservationAvecRetourDeList(
      encaissementReservation
    );
    return true;
  }

  @Override
  public List<EncaissementPrincipalDTO> saveEncaissementReservationAvecREsrourDeList(
    EncaissementPrincipalDTO dto
  ) {
    EncaissementPrincipal encaissementPrincipal = new EncaissementPrincipal();
    encaissementPrincipal.setModePaiement(dto.getModePaiement());
    encaissementPrincipal.setOperationType(dto.getOperationType());
    encaissementPrincipal.setIdAgence(dto.getIdAgence());
    encaissementPrincipal.setSoldeEncaissement(dto.getMontantEncaissement());
    encaissementPrincipal.setIdCreateur(dto.getIdCreateur());
    encaissementPrincipal.setDateEncaissement(LocalDate.now());
    encaissementPrincipal.setMontantEncaissement(dto.getMontantEncaissement());
    encaissementPrincipal.setIntituleDepense(dto.getIntituleDepense());
    encaissementPrincipal.setEntiteOperation(dto.getEntiteOperation());
    encaissementPrincipal.setStatureCloture("non cloturer");
    encaissementPrincipal.setEntiteOperation(null);
    EncaissementPrincipal saveEncaissement = encaissementPrincipalRepository.save(
      encaissementPrincipal
    );
    throw new UnsupportedOperationException(
      "Unimplemented method 'saveEncaissementReservationAvecREsrourDeList'"
    );
  }

  @Override
  public List<ReservationAfficheDto> listeDesReservationParAgence(
    Long idAgence
  ) {
    return reservationRepository
      .findAll()
      .stream()
      .filter(reser -> reser.getIdAgence() == idAgence)
      .map(x->gestimoWebMapperImpl.fromReservation(x))
      .collect(Collectors.toList());
  }

  @Override
  public List<ReservationAfficheDto> listeDesReservationOuvertParAgence(Long idAgence) {
    return reservationRepository
      .findAll()
      .stream()
      .filter(reser -> reser.getIdAgence() == idAgence && reser.getStatutReservation().contains("Ouv"))
      .map(x->gestimoWebMapperImpl.fromReservation(x))
      .collect(Collectors.toList());
  }
}
