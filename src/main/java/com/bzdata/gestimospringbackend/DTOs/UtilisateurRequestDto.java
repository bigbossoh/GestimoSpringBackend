package com.bzdata.gestimospringbackend.DTOs;

import com.bzdata.gestimospringbackend.Models.Utilisateur;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UtilisateurRequestDto {
    private Long id;
    private Long idAgence;
    private String nom;
    private String prenom;
    private String email;
    private String mobile;
    private LocalDate dateDeNaissance;
    private String lieuNaissance;
    private String typePieceIdentite;
    private String numeroPieceIdentite;
    private LocalDate dateDebutPiece;
    private LocalDate dateFinPiece;
    private String nationalité;
    private String genre;
    private boolean isActivated;
    private String username;
    private String password;
    AgenceRequestDto agenceDto;
    RoleRequestDto roleRequestDto;
    UtilisateurRequestDto userCreateDto;

    public static UtilisateurRequestDto fromEntity(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        return UtilisateurRequestDto.builder()
                .id(utilisateur.getId())
                .idAgence(utilisateur.getIdAgence())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .email(utilisateur.getEmail())
                .mobile(utilisateur.getMobile())
                .dateDeNaissance(utilisateur.getDateDeNaissance())
                .lieuNaissance(utilisateur.getLieuNaissance())
                .typePieceIdentite(utilisateur.getTypePieceIdentite())
                .numeroPieceIdentite(utilisateur.getNumeroPieceIdentite())
                .dateDebutPiece(utilisateur.getDateDebutPiece())
                .dateFinPiece(utilisateur.getDateFinPiece())
                .nationalité(utilisateur.getNationalité())
                .genre(utilisateur.getGenre())
                .isActivated(utilisateur.isActivated())
                .username(utilisateur.getUsername())
                .password(utilisateur.getPassword())
                .agenceDto(AgenceRequestDto.fromEntity(utilisateur.getAgence()))
                .roleRequestDto(RoleRequestDto.fromEntity(utilisateur.getUrole()))
                .userCreateDto(UtilisateurRequestDto.fromEntity(utilisateur.getUserCreate()))
                .build();
    }

    public static Utilisateur toEntity(UtilisateurRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Utilisateur newUtilisateur = new Utilisateur();

        newUtilisateur.setId(dto.getId());
        newUtilisateur.setNom(dto.getNom());
        newUtilisateur.setPrenom(dto.getPrenom());
        newUtilisateur.setEmail(dto.getEmail());
        newUtilisateur.setMobile(dto.getMobile());
        newUtilisateur.setDateDeNaissance(dto.getDateDeNaissance());
        newUtilisateur.setLieuNaissance(dto.getLieuNaissance());
        newUtilisateur.setTypePieceIdentite(dto.getTypePieceIdentite());
        newUtilisateur.setNumeroPieceIdentite(dto.getNumeroPieceIdentite());
        newUtilisateur.setDateFinPiece(dto.getDateFinPiece());
        newUtilisateur.setDateDebutPiece(dto.getDateDebutPiece());
        newUtilisateur.setNationalité(dto.getNationalité());
        newUtilisateur.setGenre(dto.getGenre());
        newUtilisateur.setActivated(dto.isActivated());
        newUtilisateur.setUsername(dto.getUsername());
        newUtilisateur.setPassword(dto.getPassword());
        newUtilisateur.setIdAgence(dto.getIdAgence());
        newUtilisateur.setAgence(AgenceRequestDto.toEntity(dto.getAgenceDto()));
        newUtilisateur.setUrole(RoleRequestDto.toEntity(dto.getRoleRequestDto()));
        newUtilisateur.setUserCreate(UtilisateurRequestDto.toEntity(dto.getUserCreateDto()));

        return newUtilisateur;
    }

}
