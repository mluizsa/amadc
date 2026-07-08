package com.ong.amadc.domain.model;

import com.ong.amadc.config.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name = "arquivos_sistema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AuditOverride(isAudited = false)
public class ArquivoSistemaEntidade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "arq_id")
    private Long id;

    @Column(name = "arq_url", nullable = false, length = 1000)
    private String url;

    @Column(name = "arq_nome_original")
    private String nomeOriginal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tva_id", nullable = false)
    private TipoVinculoArquivoEntidade tipoVinculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anim_id")
    private AnimalEntidade animal;

    @Column(name = "aten_id")
    private Long atendimentoId;

    @Column(name = "volu_id")
    private Long voluntarioId;

    @Column(name = "arq_is_capa")
    private Boolean isCapa;

}