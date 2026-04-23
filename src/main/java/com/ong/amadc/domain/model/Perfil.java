package com.ong.amadc.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "perfis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Perfil {

    @Id
    @Column(name = "perf_id")
    private Long id;

    @Column(name = "perf_nome")
    private String nome;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "perfil_permissoes",
            joinColumns = @JoinColumn(name = "perf_id"),
            inverseJoinColumns = @JoinColumn(name = "perm_id")
    )
    private Set<Permissao> permissoes;
}