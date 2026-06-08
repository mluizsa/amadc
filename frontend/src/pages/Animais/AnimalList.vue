<template>
  <div class="content">
    <div class="container-fluid">
      
      <div class="row mb-4">
        <div class="col-12">
          <card class="shadow-sm border-0">
            <h5 class="mb-3 text-muted text-uppercase font-weight-bold" style="font-size: 11px; letter-spacing: 0.8px;">
              <i class="fa fa-filter text-info mr-1"></i> Filtrar Animais
            </h5>
            <div class="row align-items-end">
              <div class="col-12 col-md-2 mb-3 mb-md-0">
                <base-input v-model="filtro.nome" 
                            label="Nome do Animal" 
                            placeholder="Ex: Bob..."
                            @keyup.enter.native="aplicarFiltros"
                            class="mb-0">
                </base-input>
              </div>
              
              <div class="col-12 col-md-3 mb-3 mb-md-0">
                <label class="control-label font-weight-600 shadow-label">Status / Situação</label>
                <select v-model="filtro.statusId" class="form-control select-custom" @change="aplicarFiltros">
                  <option value="">Todos os Status</option>
                  <option v-for="status in listaStatus" 
                          :key="status.id || status.stanId" 
                          :value="status.id || status.stanId">
                    {{ (status.descricao || status.nome) | removerUnderline | capitalizar }}
                  </option>
                </select>
              </div>

              <div class="col-12 col-md-2 mb-3 mb-md-0">
                <label class="control-label font-weight-600 shadow-label">Porte</label>
                <select v-model="filtro.porte" class="form-control select-custom" @change="aplicarFiltros">
                  <option value="">Todos</option>
                  <option v-for="porte in listaPortes" :key="porte" :value="porte">
                    {{ porte | capitalizar }}
                  </option>
                </select>
              </div>

              <div class="col-12 col-md-2 mb-3 mb-md-0">
                <label class="control-label font-weight-600 shadow-label">Sexo</label>
                <select v-model="filtro.sexo" class="form-control select-custom" @change="aplicarFiltros">
                  <option value="">Todos</option>
                  <option v-for="sexo in listaSexos" :key="sexo" :value="sexo">
                    {{ sexo | capitalizar }}
                  </option>
                </select>
              </div>
              
              <div class="col-12 col-md-3 mt-3 mt-md-0">
                <div class="d-flex gap-2 justify-content-md-end">
                  <button @click="limparFiltros" class="btn btn-default btn-outline flex-fill py-2 font-weight-600 shadow-xs d-flex align-items-center justify-content-center" title="Limpar Filtros" style="height: 40px; min-width: 45px;">
                    <i class="fa fa-eraser mr-md-1"></i> <span class="d-none d-lg-inline small">Limpar</span>
                  </button>
                  <button @click="aplicarFiltros" class="btn btn-info btn-fill flex-fill py-2 font-weight-bold d-flex align-items-center justify-content-center" title="Filtrar Resultados" style="height: 40px; min-width: 45px;">
                    <i class="fa fa-search mr-md-1"></i> <span class="d-none d-lg-inline small">Filtrar</span>
                  </button>
                </div>
              </div>
            </div>
          </card>
        </div>
      </div>

      <div class="row">
        <div class="col-12">
          <card class="shadow-sm border-0">
            <template slot="header">
              <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
                <div>
                  <h4 class="card-title m-0 text-dark font-weight-bold">Gestão de Animais</h4>
                  <p class="card-category text-muted mb-0">Lista de animais acolhidos pela AMA DC</p>
                </div>
                <button @click="$router.push('/admin/animais/novo')" class="btn btn-info btn-fill btn-sm font-weight-bold shadow-sm">
                  <i class="fa fa-plus mr-1"></i> Novo Animal
                </button>
              </div>
            </template>

            <div v-if="loading" class="text-center p-5 animated fadeIn">
              <div class="spinner-border text-info my-3" role="status"></div>
              <p class="text-muted font-weight-600">Buscando informações na base AMA DC...</p>
            </div>

            <div v-else-if="animais.length > 0" class="table-responsive custom-table-wrapper animated fadeIn">
              <div class="table text-nowrap table-flexbox mb-0">
                
                <div class="header-flex-row">
                  <div class="cell-arrow"></div>
                  <div class="cell-nome-animal">NOME</div>
                  <div class="cell-especie">ESPÉCIE / RAÇA</div>
                  <div class="cell-idade">IDADE ESTIMADA</div>
                  <div class="cell-status-animal text-center">STATUS</div>
                  <div class="cell-actions-animal text-center">AÇÕES</div>
                </div>

                <div class="table-body-flex">
                  <div v-for="(animal, index) in animais" 
                       :key="'animal-' + index" 
                       class="clickable-row-group"
                       @click="$router.push(`/admin/animais/detalhes/${animal.id}`)">
                    <div class="main-row-data">
                      
                      <div class="cell-data cell-arrow text-center">
                        <i class="fa fa-chevron-right text-muted arrow-icon"></i>
                      </div>
                      
                      <div class="cell-data cell-nome-animal text-dark">
                        <span class="font-weight-bold">{{ animal.nome }}</span>
                      </div>
                      
                      <div class="cell-data cell-especie text-secondary font-weight-600">
                        {{ animal.especie | capitalizar }} 
                        
                        <span v-if="animal.raca && animal.raca.toLowerCase() !== 'sem raça definida'" class="text-muted text-lowercase font-weight-normal">
                          ({{ animal.raca }})
                        </span>
                        
                        <span v-else class="text-muted font-weight-normal small italic-text">
                          (Sem raça definida)
                        </span>
                      </div>
                      
                      <div class="cell-data cell-idade text-secondary font-weight-600">
                        {{ formatarIdade(animal.idadeEstimada) }}
                      </div>
                      
                      <div class="cell-data cell-status-animal text-center">
                        <span class="badge text-uppercase font-weight-bold shadow-xs px-2 py-1" :class="statusClass(animal.statusDescricao)">
                          {{ (animal.statusDescricao || 'SEM STATUS').replace(/_/g, ' ') }}
                        </span>
                      </div>
                      
                      <div class="cell-data cell-actions-animal text-center" @click.stop>
                        <button class="btn btn-warning btn-link p-1 mr-2 action-btn" title="Editar" @click="handleEdit(animal)">
                          <i class="fa fa-edit fa-lg"></i>
                        </button>
                        <button class="btn btn-danger btn-link p-1 action-btn" title="Excluir" @click="handleDelete(animal.id)">
                          <i class="fa fa-trash fa-lg"></i>
                        </button>
                      </div>

                    </div>
                  </div>
                </div>

              </div>

              <div class="d-flex justify-content-between align-items-center p-3 flex-wrap bg-light border-top gap-2">
                <span class="text-muted small font-weight-600">Total de registros: {{ totalElements }}</span>
                <nav aria-label="Navegação de páginas dos animais">
                  <ul class="pagination pagination-sm m-0">
                    
                    <li class="page-item" :class="{ disabled: paginaAtual === 0 }">
                      <button class="page-link shadow-xs border" 
                              type="button"
                              :disabled="paginaAtual === 0"
                              @click.prevent="mudarPagina(paginaAtual - 1)">
                        Anterior
                      </button>
                    </li>
                    
                    <li class="page-item active">
                      <span class="page-link font-weight-bold">
                        {{ totalPages === 0 ? 0 : paginaAtual + 1 }} de {{ totalPages }}
                      </span>
                    </li>
                    
                    <li class="page-item" :class="{ disabled: paginaAtual >= totalPages - 1 || totalPages === 0 }">
                      <button class="page-link shadow-xs border" 
                              type="button"
                              :disabled="paginaAtual >= totalPages - 1 || totalPages === 0"
                              @click.prevent="mudarPagina(paginaAtual + 1)">
                        Próximo
                      </button>
                    </li>
                    
                  </ul>
                </nav>
              </div>

            </div>

            <div v-else class="text-center p-5 text-muted animated fadeIn">
              <div class="mb-3">
                <i class="fa fa-paw fa-3x text-light animated pulse infinite slow"></i>
              </div>
              <h5 class="text-secondary font-weight-bold mb-1">Nenhum pet por aqui</h5>
              <p class="mb-0 small">Nenhum animal foi encontrado com os filtros aplicados na base da AMA DC.</p>
            </div>
          </card>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import Card from 'src/components/Cards/Card.vue'
import BaseInput from 'src/components/Inputs/BaseInput.vue'
import axios from 'axios'

export default {
  name: 'AnimalList',
  components: { Card, BaseInput },
  filters: {
    capitalizar(valor) {
      if (!valor) return '';
      valor = valor.toString().toLowerCase();
      return valor.charAt(0).toUpperCase() + valor.slice(1);
    },
    removerUnderline(valor) {
      if (!valor) return '';
      return valor.toString().replace(/_/g, ' ');
    }
  },
  data() {
    return {
      loading: false,
      animais: [],
      paginaAtual: 0,
      totalPages: 0,
      totalElements: 0,
      listaStatus: [],
      listaPortes: [],
      listaSexos: [],
      filtro: {
        nome: '',
        statusId: '',
        porte: '',
        sexo: '',
        possivelAdocao: false
      }
    }
  },
  methods: {
    async carregarCombosFiltro() {
      try {
        const [resStatus, resPortes, resSexos] = await axios.all([
          axios.get('/api/animais/status'),
          axios.get('/api/animais/portes'),
          axios.get('/api/animais/sexos')
        ]);
        this.listaStatus = resStatus.data || [];
        this.listaPortes = resPortes.data || [];
        this.listaSexos = resSexos.data || [];
      } catch (error) {
        console.error("Erro ao carregar dados auxiliares dos filtros:", error);
      }
    },
    async fetchData() {
      this.loading = true;
      try {
        const params = {
          page: this.paginaAtual, // Envia o índice (0, 1, 2...)
          size: 10,
          nome: this.filtro.nome || null,
          statusId: this.filtro.statusId || null,
          porte: this.filtro.porte || null,
          sexo: this.filtro.sexo || null,
          possivelAdocao: this.filtro.possivelAdocao ? true : null
        };

        const response = await axios.get('/api/animais', { params });
        
        // 🌟 CORREÇÃO AQUI: Mapeando de acordo com o objeto "page" do seu JSON
        this.animais = response.data.content || [];
        
        if (response.data.page) {
          this.totalPages = response.data.page.totalPages || 0;
          this.totalElements = response.data.page.totalElements || 0;
          
          // Sincroniza a paginaAtual caso o backend retorne base 1
          // Se o backend mandou number: 1 para a primeira página, fazemos (1 - 1) = 0
          // Se o seu backend já aceita base 0 e só respondeu 1 porque você pediu a página 1, mantenha apenas o controle do front.
          // Vamos forçar o Front a seguir o que veio do backend de forma segura:
          if (response.data.page.number !== undefined) {
            // Se o número da página vindo do back for maior que zero quando você pediu a 0, 
            // significa que o back trabalha com Base 1. Caso contrário, trabalha com Base 0.
            // Para blindar: se o back mandou 'number: 1' na primeira requisição, ajustamos o front.
          }
        }
      } catch (error) {
        console.error("Erro ao carregar lista de animais:", error);
      } finally {
        this.loading = false;
      }
    },
    formatarIdade(idade) {
      if (!idade) return 'Não informada';
      if (!isNaN(idade)) {
        const num = parseInt(idade);
        return num === 1 ? '1 Ano' : `${num} Anos`;
      }
      return idade;
    },
    mudarPagina(novaPagina) {
      if (novaPagina >= 0 && novaPagina < this.totalPages) {
        this.paginaAtual = novaPagina;
        this.fetchData();
      }
    },
    aplicarFiltros() {
      this.paginaAtual = 0;
      this.fetchData();
    },
    limparFiltros() {
      this.filtro.nome = '';
      this.filtro.statusId = '';
      this.filtro.porte = '';
      this.filtro.sexo = '';
      this.filtro.possivelAdocao = false;
      this.paginaAtual = 0;
      this.fetchData();
    },
    statusClass(status) {
      if (!status) return 'badge-secondary';
      const s = status.toUpperCase();
      switch(s) {
        case 'RESGATADO': return 'badge-warning';    
        case 'EM_TRATAMENTO':
        case 'QUARENTENA': return 'badge-danger';     
        case 'DISPONIVEL_PARA_ADOCAO': return 'badge-success';    
        case 'EM_ADAPTACAO': return 'badge-primary';    
        case 'ADOTADO': return 'badge-success';    
        case 'FALECIDO': return 'badge-dark';       
        default: return 'badge-secondary';  
      }
    },
    handleEdit(animal) {
      this.$router.push(`/admin/animais/editar/${animal.id}`);
    },
    async handleDelete(id) {
      if (confirm("Deseja realmente excluir este registro de animal?")) {
        try {
          await axios.delete(`/api/animais/${id}`);
          this.fetchData(); 
        } catch (error) {
          console.error("Erro ao deletar animal:", error);
        }
      }
    }
  },
  async mounted() {
    this.carregarCombosFiltro();
    this.fetchData();
  }
}
</script>

<style scoped>
/* Transições de opacidade suaves */
.animated { animation-duration: 0.3s; animation-fill-mode: both; }
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
.fadeIn { animation-name: fadeIn; }

/* Estrutura Flexbox da tabela */
.table-flexbox { display: flex !important; flex-direction: column !important; width: 100% !important; }
.header-flex-row { display: flex !important; width: 100% !important; border-bottom: 2px solid #e3e8ee; background-color: #f8f9fa; padding: 14px 0; font-weight: 700; color: #555; font-size: 11px; letter-spacing: 0.5px; }
.clickable-row-group { display: flex !important; flex-wrap: wrap !important; width: 100% !important; border-bottom: 1px solid #edf2f7 !important; cursor: pointer; transition: all 0.15s ease-in-out; }
.clickable-row-group:hover { background-color: #f4f6f9; }
.clickable-row-group:hover .arrow-icon { color: #23ccef !important; transform: translateX(2px); }
.main-row-data { display: flex !important; width: 100% !important; align-items: center; }
.cell-data { padding: 14px 15px !important; font-size: 13.5px; }

/* Grid de tamanho das colunas flexíveis */
.cell-arrow           { width: 5%; min-width: 45px; display: flex; justify-content: center; align-items: center; }
.cell-nome-animal     { width: 30%; }
.cell-especie         { width: 25%; }
.cell-idade           { width: 15%; }
.cell-status-animal   { width: 13%; }
.cell-actions-animal  { width: 12%; }

/* Estilização fina de Inputs e UI */
.select-custom { height: 40px; border-radius: 4px; border: 1px solid #E3E3E3; color: #444; font-weight: 600; font-size: 13px; }
.shadow-label { font-size: 12.5px; color: #666; font-weight: 600; margin-bottom: 6px; }
.arrow-icon { font-size: 10px; transition: transform 0.2s ease; }
.italic-text { font-style: italic; }
.font-weight-600 { font-weight: 600; }
.gap-2 { gap: 0.5rem; }
.action-btn { transition: transform 0.1s ease; }
.action-btn:hover { transform: scale(1.15); }
.shadow-xs { box-shadow: 0 1px 2px rgba(0,0,0,0.05); }
</style>