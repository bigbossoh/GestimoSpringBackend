package com.bzdata.gestimospringbackend.Services.Impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.bzdata.gestimospringbackend.DTOs.PaysDto;
import com.bzdata.gestimospringbackend.Models.Pays;
import com.bzdata.gestimospringbackend.repository.PaysRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
public class PaysServiceImplTest {

    private PaysServiceImpl underTest;

    @Mock
    private PaysRepository paysRepository;

    @BeforeEach
    void setUp() {
        underTest = new PaysServiceImpl(paysRepository);
    }

    @Test
    @Disabled
    void testDelete() {

    }

    @Test
    void canGetAllAll() {
        // WHEN
        underTest.findAll();
        // THEN
        verify(paysRepository).findAll(Sort.by(Direction.ASC, "nomPays"));

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
    void canSaveUserSave() {
        // GIVEN
        Pays pays = new Pays("abrvPays", "nomPays", null);
        // WHEN
        underTest.save(PaysDto.fromEntity(pays));

        // THEN
        ArgumentCaptor<Pays> pCaptor = ArgumentCaptor.forClass(Pays.class);
        verify(paysRepository).save(pCaptor.capture());
        assertThat(pays.getAbrvPays()).isEqualTo(pCaptor.getValue().getAbrvPays());
    }

}
