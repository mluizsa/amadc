package com.ong.amadc.domain.model;

import com.ong.amadc.config.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "tipo_atendimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class TipoAtendimentoEntidade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tiat_id")
    private Long id;

    @Column(name = "tiat_descricao", nullable = false, unique = true)
    private String descricao;
}