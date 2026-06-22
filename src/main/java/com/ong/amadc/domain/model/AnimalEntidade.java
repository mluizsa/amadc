package com.ong.amadc.domain.model;

import com.ong.amadc.config.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import java.time.LocalTime;
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

    @Column(name = "anim_nome", nullable = false, columnDefinition = "VARCHAR(255)")
    private String nome;

    @Column(name = "anim_especie")
    private String especie;
    @Column(name = "anim_raca")
    private String raca;
    @Column(name = "anim_porte")
    private String porte;

    @Column(name = "anim_sexo")
    private String sexo; // MACHO, FEMEA

    @Column(name = "anim_idade_estimada")
    private String idadeEstimada; // FILHOTE, ADULTO, IDOSO

    @Column(name = "anim_pelagem_cor")
    private String pelagemCor;

    @Column(name = "anim_pelagem_tipo")
    private String pelagemTipo; // Curto, Longo, etc.

    @Column(name = "anim_marcas_cicatrizes")
    private String marcasCicatrizes;
    
    @Column(name = "anim_data_resgate")
    private LocalDate dataResgate;

    @Column(name = "anim_hora_resgate")
    private LocalTime horaResgate;

    @Column(name = "anim_local_resgate_bairro")
    private String localResgateBairro;

    @Column(name = "anim_local_resgate_rua")
    private String localResgateRua;

    @Column(name = "anim_local_resgate_referencia")
    private String localResgateReferencia;

    @Column(name = "anim_origem")
    private String origem; // RUA, MAUS_TRATOS, ENTREGA_VOLUNTARIA

    @Column(name = "anim_resgatador_nome")
    private String resgatadorNome;

    @Column(name = "anim_resgatador_contato")
    private String resgatadorContato;

    @Column(name = "anim_peso_entrada")
    private Double pesoEntrada;

    @Column(name = "anim_condicao_entrada")
    private String condicaoEntrada; // Saudável, Ferido, etc.

    @Column(name = "anim_microchip")
    private String microchip;

    @Column(name = "anim_castrado")
    private Boolean castrado = false; // Default para não castrado

    @Column(name = "anim_data_castracao")
    private LocalDate dataCastracao;

    @Column(name = "anim_data_castracao_desconhecida")
    private Boolean dataCastracaoDesconhecida = false; // Default para data conhecida

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