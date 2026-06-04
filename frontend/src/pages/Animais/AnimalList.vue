<template>
  <div class="content">
    <div class="container-fluid">
      
      <div class="row mb-4">
        <div class="col-12">
          <card>
            <h5 class="mb-3"><i class="fa fa-filter text-info"></i> Filtrar Animais</h5>
            <div class="row">
              <div class="col-12 col-md-4">
                <base-input v-model="filtro.nome" label="Nome do Animal" placeholder="Ex: Bob..."></base-input>
              </div>
              <div class="col-12 col-md-4">
                <label class="control-label">Status</label>
                <select v-model="filtro.status" class="form-control">
                  <option value="">Todos</option>
                  <option value="RESGATADO">Resgatado</option>
                  <option value="ACOLHIDO">Acolhido</option>
                  <option value="ADOTADO">Adotado</option>
                </select>
              </div>
              <div class="col-12 col-md-4 d-flex align-items-end">
                <button @click="aplicarFiltros" class="btn btn-info btn-fill mr-2">
                  <i class="fa fa-search"></i> Buscar
                </button>
                <button @click="limparFiltros" class="btn btn-neutral btn-fill">
                  Limpar
                </button>
              </div>
            </div>
          </card>
        </div>
      </div>

      <div class="row">
        <div class="col-12">
          <card class="strpied-tabled-with-hover">
            <template slot="header">
              <div class="d-flex justify-content-between align-items-center flex-wrap">
                <div>
                  <h4 class="card-title m-0">Gestão de Animais</h4>
                  <p class="card-category">Lista de animais acolhidos pela AMA DC</p>
                </div>
                <button @click="$router.push('/admin/animais/novo')" class="btn btn-info btn-fill btn-sm">
                  <i class="fa fa-plus"></i> Novo Animal
                </button>
              </div>
            </template>

            <div v-if="loading" class="text-center p-5">
              <div class="spinner-border text-info" role="status"></div>
              <p class="mt-2 text-muted">Buscando informações dos animais...</p>
            </div>

            <div v-else-if="animais.length > 0" class="table-responsive custom-table-wrapper">
              <div class="table text-nowrap table-flexbox">
                
                <div class="header-flex-row">
                  <div class="cell-arrow"></div>
                  <div class="cell-nome-animal">NOME</div>
                  <div class="cell-especie">ESPÉCIE / RAÇA</div>
                  <div class="cell-idade">IDADE APROX.</div>
                  <div class="cell-status-animal text-center">STATUS</div>
                  <div class="cell-actions-animal text-center">AÇÕES</div>
                </div>

                <div class="table-body-flex">
                  <template v-for="(animal, index) in animais">
                    <div :key="'animal-' + index" class="clickable-row-group">
                      <div class="main-row-data">
                        <div class="cell-data cell-arrow">
                          <i class="fa fa-chevron-right text-muted arrow-icon"></i>
                        </div>
                        <div class="cell-data cell-nome-animal"><b>{{ animal.nome }}</b></div>
                        <div class="cell-data cell-especie">{{ animal.especie }} {{ animal.raca ? `(${animal.raca})` : '' }}</div>
                        <div class="cell-data cell-idade">{{ animal.idadeAproximada || 'Não informada' }}</div>
                        <div class="cell-data cell-status-animal text-center">
                          <span class="badge" :class="statusClass(animal.status)">
                            {{ animal.status }}
                          </span>
                        </div>
                        <div class="cell-data cell-actions-animal text-center" @click.stop>
                          <button class="btn btn-warning btn-link btn-xs" title="Editar" @click="handleEdit(animal)">
                            <i class="fa fa-edit fa-lg"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </template>
                </div>

              </div>

              <div class="d-flex justify-content-between align-items-center p-3 flex-wrap">
                <span class="text-muted small">Total de registros: {{ totalElements }}</span>
                <nav>
                  <ul class="pagination pagination-sm m-0">
                    <li class="page-item" :class="{ disabled: paginaAtual === 0 }">
                      <a class="page-link" @click.prevent="mudarPagina(paginaAtual - 1)" href="#">Anterior</a>
                    </li>
                    <li class="page-item active">
                      <a class="page-link" href="#">{{ paginaAtual + 1 }} de {{ totalPages }}</a>
                    </li>
                    <li class="page-item" :class="{ disabled: paginaAtual >= totalPages - 1 }">
                      <a class="page-link" @click.prevent="mudarPagina(paginaAtual + 1)" href="#">Próximo</a>
                    </li>
                  </ul>
                </nav>
              </div>

            </div>

            <div v-else class="text-center p-4 text-muted">
              Nenhum animal encontrado com os filtros aplicados.
            </div>
          </card>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import Card from 'src/components/Cards/Card.vue'
import axios from 'axios'

export default {
  components: { Card },
  data() {
    return {
      loading: false,
      animais: [],
      paginaAtual: 0,
      totalPages: 0,
      totalElements: 0,
      filtro: {
        nome: '',
        status: ''
      }
    }
  },
  methods: {
    async fetchData() {
      this.loading = true;
      try {
        // Passando paginação e filtros via query params (mapeia direto no Pageable e AnimalFiltroRequest do Spring)
        const params = {
          page: this.paginaAtual,
          size: 10,
          nome: this.filtro.nome,
          status: this.filtro.status
        };
        const response = await axios.get('/api/animais', { params });
        
        // Desestruturando o retorno do Page do Spring Boot
        this.animais = response.data.content || [];
        this.totalPages = response.data.totalPages || 0;
        this.totalElements = response.data.totalElements || 0;
      } catch (error) {
        console.error("Erro ao carregar lista de animais:", error);
      } finally {
        this.loading = false;
      }
    },
    mudarPagina(novaPagina) {
      if (novaPagina >= 0 && novaPagina < this.totalPages) {
        this.paginaAtual = novaPagina;
        this.fetchData();
      }
    },
    aplicarFiltros() {
      this.paginaAtual = 0; // Volta para a primeira página ao filtrar
      this.fetchData();
    },
    limparFiltros() {
      this.filtro.nome = '';
      this.filtro.status = '';
      this.paginaAtual = 0;
      this.fetchData();
    },
    statusClass(status) {
      if (status === 'ADOTADO') return 'badge-success';
      if (status === 'ACOLHIDO') return 'badge-info';
      return 'badge-warning'; // RESGATADO
    },
    handleEdit(animal) {
      this.$router.push(`/admin/animais/editar/${animal.id}`);
    }
  },
  mounted() {
    this.fetchData();
  }
}
</script>

<style scoped>
/* Reaproveitando os estilos da nossa tabela customizada */
.table-flexbox { display: flex !important; flex-direction: column !important; width: 100% !important; }
.header-flex-row { display: flex !important; width: 100% !important; border-bottom: 2px solid #e3e8ee; background-color: #fafbfc; padding: 12px 0; font-weight: 600; color: #666; }
.clickable-row-group { display: flex !important; flex-wrap: wrap !important; width: 100% !important; border-bottom: 1px solid #eeeeee !important; }
.main-row-data { display: flex !important; width: 100% !important; align-items: center; }
.cell-data { padding: 14px 15px !important; }

/* Distribuição das colunas de Animais */
.cell-arrow           { width: 5%; min-width: 45px; }
.cell-nome-animal     { width: 30%; }
.cell-especie         { width: 25%; }
.cell-idade           { width: 15%; }
.cell-status-animal   { width: 13%; }
.cell-actions-animal  { width: 12%; }

.badge { padding: 5px 10px; font-size: 11px; border-radius: 4px; }
.page-link { cursor: pointer; }
</style>