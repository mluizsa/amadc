package com.ong.amadc.domain.model;

import com.ong.amadc.config.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_atendimentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class AtendimentoEntidade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aten_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "anim_id", nullable = false)
    private AnimalEntidade animal;

    @ManyToOne
    @JoinColumn(name = "vete_id")
    private VeterinarioEntidade veterinario;

    @ManyToOne
    @JoinColumn(name = "volu_id")
    private VoluntarioEntidade voluntario;

    @Column(name = "aten_data_atendimento")
    private LocalDateTime dataAtendimento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tiat_id")
    private TipoAtendimentoEntidade tipoAtendimento;
    
    @Column(name = "aten_descricao", columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "aten_peso_na_data")
    private Double pesoNaData;
    
    @Column(name = "aten_valor_cobrado")
    private BigDecimal valorCobrado;
}