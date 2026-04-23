package com.ong.amadc.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perm_id")
    private Long id;

    @Column(name = "perm_chave", nullable = false, unique = true)
    private String chave;

    @Column(name = "perm_descricao")
    private String descricao;
}