package com.bzdata.gestimospringbackend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.bzdata.gestimospringbackend.Models.Pays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PaysRepositoryTest {

    @Autowired
    private PaysRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        // GIVEN
        Pays pays = new Pays();
        pays.setAbrvPays("abrvPays");
        pays.setNomPays("nomPays");
        underTest.save(pays);
    }

    @Test
    @DisplayName("Test si on touvre pas l'abreviation")
    void itShoulCheckIfPaysFindByAbrvPays() {

        // WHEN
        Optional<Pays> paysAttendue = underTest.findByAbrvPays("abrvPays");
        // THEN
        assertThat(paysAttendue).isPresent();
    }

    @Test
    void testFindByNomPays() {
        Optional<Pays> paysAttendue = underTest.findByNomPays("nomPays");
        // THEN
        assertThat(paysAttendue).isPresent();
    }
}
