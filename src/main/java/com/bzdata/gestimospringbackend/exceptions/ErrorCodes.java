package com.bzdata.gestimospringbackend.exceptions;

public enum ErrorCodes {
    UTILISATEUR_NOT_FOUND(1000),
    UTILISATEUR_NOT_VALID(1001),
    UTILISATEUR_ALREADY_IN_USE(1002),
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

    ;

    private int code;

    ErrorCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
