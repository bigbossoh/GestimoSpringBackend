package com.bzdata.gestimospringbackend.Services.Impl;

import static org.mockito.Mockito.verify;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.Role;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Models.Villa;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.repository.VillaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
public class BailVillaServiceImplTest {

    private BailVillaServiceImpl underTest;
    @Mock
    private BailLocationRepository bailLocationRepository;
    @Mock
    private UtilisateurRepository utilisateurRepository;
    @Mock
    private VillaRepository villaRepository;

    @BeforeEach
    void setUp() {
        underTest = new BailVillaServiceImpl(bailLocationRepository, utilisateurRepository, villaRepository);
    }

    @Test
    @Disabled
    void testDelete() {

    }

    @Test
    // @Disabled
    void testFindAll() {
        // WHEN
        underTest.findAll();
        // THEN
        verify(bailLocationRepository).findAll(Sort.by(Direction.ASC, "designationBail"));

    }

    @Disabled
    @Test
    void testFindAllByIdBienImmobilier() {

    }

    @Disabled
    @Test
    void testFindAllByIdLocataire() {

    }

    @Test
    @Disabled
    void testFindById() {

    }

    @Test
    @Disabled
    void testFindByName() {

    }

    @Test
    @Disabled
    @DisplayName("Test si l'ajout des Baux passe bien")
    void weCanSaveBailVill() {
        // GIVEN
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        // AgenceImmobiliere agence=new AgenceImmobiliere("SEVE INVEST", "1011555",
        // "CC/CC 200021", 368555000L, "astaire@gamil", "SARL", "15000", "SEVE", null,
        // null);
        Role role = new Role("LOCATAIRE", "LOCATAIRE", null);
        Villa villa = new Villa(3, 5, 1, 1, "Villa Pigier", "VP", true, 1);
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setActivated(false);
        utilisateur.setUrole(role);
        // utilisateur.setAgence(agence);
        // utilisateur.set
        // ("ZRANGO ", "NAZAIRE ", "astairenazaire@gmail.com", "0708771317",
        // "dateDeNaissance", "Bouake", "CNI", "C0010", "dateDebutPiece",
        // "dateFinPiece", "IVOIRIEN", "M", false,
        // "astairenazaire", "password", 0, null, role, , 1);

        BailLocation bailLocation = new BailLocation();
        bailLocation.setAbrvCodeBail("BV");
        bailLocation.setArchiveBail(false);

        bailLocation.setBienImmobilierOperation(villa);
        try {
            bailLocation.setDateDebut(format.parse("10/05/2022"));
            bailLocation.setDateFin(format.parse("10/05/2024"));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        bailLocation.setDesignationBail("Bail de Villa Test");
        bailLocation.setEnCoursBail(true);
        bailLocation.setMontantCautionBail(250000L);
        bailLocation.setNbreMoisCautionBail(5);
        bailLocation.setUtilisateurOperation(utilisateur);

    }
}
