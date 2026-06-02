package com.ong.amadc.domain.model;

import com.ong.amadc.config.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "status_animal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class StatusAnimalEntidade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stan_id")
    private Long id;

    @Column(name = "stan_descricao", nullable = false, unique = true)
    private String descricao;
}