package com.bzdata.gestimospringbackend.Models;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")
public class TokenVerification extends AbstractEntity{    
    private String token;
    private Instant expiryDate;
    @OneToOne(fetch = FetchType.LAZY)
    private Utilisateur utilisateur;
       
}
