package com.ong.amadc.domain.model;

import com.ong.amadc.config.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Table(name = "voluntarios")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoluntarioEntidade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "volu_id")
    private Long id;

    @Column(name = "volu_nome", nullable = false)
    private String nome;

    @Column(name = "volu_cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "volu_telefone")
    private String telefone;

    @Column(name = "volu_email")
    private String email;

    @Column(name = "volu_data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "volu_ocupacao")
    private String ocupacao;

    @Column(name = "volu_observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "volu_ativo")
    private Boolean ativo = true;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true) // Adicionado CascadeType.ALL e orphanRemoval
    @JoinColumn(name = "usu_id")
    @org.hibernate.envers.Audited(targetAuditMode = org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED)
    private UsuarioEntidade usuario;
}