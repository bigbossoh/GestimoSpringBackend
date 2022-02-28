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
    ;

    private int code;
     ErrorCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
