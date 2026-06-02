package com.ong.amadc.domain.model;

import com.ong.amadc.config.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "animais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class AnimalEntidade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anim_id")
    private Long id;

    @Column(name = "anim_nome", nullable = false)
    private String nome;

    @Column(name = "anim_especie")
    private String especie;
    @Column(name = "anim_raca")
    private String raca;
    @Column(name = "anim_porte")
    private String porte;
    
    @Column(name = "anim_data_resgate")
    private LocalDate dataResgate;
    @Column(name = "anim_peso_entrada")
    private Double pesoEntrada;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stan_id", nullable = false)
    private StatusAnimalEntidade status;

    @Column(name = "anim_possivel_adocao")
    private Boolean possivelAdocao = true;

    @Column(name = "anim_historia", columnDefinition = "TEXT")
    private String historia;

    @NotAudited
    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    private List<AtendimentoEntidade> prontuario = new ArrayList<>();
}