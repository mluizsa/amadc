package com.ong.amadc.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tipos_vinculo_arquivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoVinculoArquivoEntidade {

    @Id
    @Column(name = "tva_id", length = 50)
    private String id;

    @Column(name = "tva_descricao", nullable = false)
    private String descricao;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
}