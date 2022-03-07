package com.bzdata.gestimospringbackend.exceptions;

public enum ErrorCodes {
    UTILISATEUR_NOT_FOUND(1000),
    UTILISATEUR_NOT_VALID(1001),
    UTILISATEUR_ALREADY_IN_USE(1002),
    UTILISATEUR_NOT_GOOD_ROLE(1003),
    AGENCE_NOT_FOUND(2000),
    AGENCE_NOT_VALID(2001),
    AGENCE_ALREADY_IN_USE(2002),
    BAD_CREDENTIALS(13003),
    ROLE_NOT_FOUND(3000),
    ROLE_NOT_VALID(3001),
    ROLE_ALREADY_IN_USE(3002),
    PAYS_NOT_VALID(4000),
    PAYS_NOT_FOUND(4001),
    PAYS_ALREADY_IN_USE(4002),
    SITE_NOT_FOUND(5000),
    SITE_NOT_VALID(5001),
    SITE_ALREADY_IN_USE(5002),
    VILLE_NOT_VALID(5000),
    VILLE_NOT_FOUND(5001),
    COMMUNE_NOT_VALID(7000),
    COMMUNE_NOT_FOUND(7001),
    QUARTIER_NOT_VALID(8000),
    QUARTIER_NOT_FOUND(8001),
    VILLA_NOT_FOUND(9000),
    VILLA_NOT_VALID(9001),
    VILLA_ALREADY_IN_USE(9002),
    IMMEUBLE_NOT_VALID(10000),
    IMMEUBLE_NOT_FOUND(10001),
    IMMEUBLE_ALREADY_IN_USE(10002),
    ETAGE_NOT_VALID(11000),
    ETAGE_NOT_FOUND(11001),
    ETAGE_ALREADY_IN_USE(11002),
    STUDIO_NOT_VALID(12000),
    STUDIO_NOT_FOUND(12001),
    STUDIO_ALREADY_IN_USE(12002),
    APPARTEMENT_NOT_VALID(13000),
    APPARTEMENT_NOT_FOUND(13001),
    APPARTEMENT_ALREADY_IN_USE(13002),
    ;

    private int code;

    ErrorCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
