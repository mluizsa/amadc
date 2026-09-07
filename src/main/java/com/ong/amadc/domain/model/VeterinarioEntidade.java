package com.ong.amadc.domain.model;

import com.ong.amadc.config.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "veterinarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class VeterinarioEntidade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vete_id")
    private Long id;

    @Column(name = "vete_nome", nullable = false)
    private String nome;

    @Column(name = "vete_crmv")
    private String crmv;
    @Column(name = "vete_telefone")
    private String telefone;
    @Column(name = "vete_especialidade")
    private String especialidade;

    @Column(name = "vete_observacoes_parceria", columnDefinition = "TEXT")
    private String observacoesParceria;
}