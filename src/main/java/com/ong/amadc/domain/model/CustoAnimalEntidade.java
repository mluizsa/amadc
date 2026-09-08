package com.ong.amadc.domain.model;

import com.ong.amadc.config.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "custos_animais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class CustoAnimalEntidade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "anim_id", nullable = false)
    private AnimalEntidade animal;

    @ManyToOne
    @JoinColumn(name = "aten_id")
    private AtendimentoEntidade atendimento;

    @Column(name = "cust_descricao", nullable = false)
    private String descricao;

    @Column(name = "cust_valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "cust_data", nullable = false)
    private LocalDateTime dataCusto;
}