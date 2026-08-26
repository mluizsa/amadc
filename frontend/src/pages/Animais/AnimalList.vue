<template>
  <div class="content">
    <div class="container-fluid">
      
      <!-- Bloco de Filtros -->
      <div class="row mb-4">
          <div class="col-12">
            <div class="card shadow-sm border-0 bg-white" style="border-radius: 8px;">
              
              <div class="p-3 d-flex justify-content-between align-items-center custom-collapse-header" 
                  @click="exibirFiltros = !exibirFiltros"
                  style="cursor: pointer; user-select: none;">
                <h6 class="m-0 text-muted text-uppercase font-weight-bold" style="font-size: 11px; letter-spacing: 0.8px;">
                  <i class="fa fa-filter text-info mr-1"></i> Filtrar Animais
                </h6>
                <div class="text-muted">
                  <span class="small mr-2">{{ exibirFiltros ? 'Recolher' : 'Expandir' }}</span>
                  <i class="fa" :class="exibirFiltros ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
                </div>
              </div>
              
              <transition name="fade-collapse">
                <div v-show="exibirFiltros" class="card-body pt-0 px-4 pb-4">
                  <hr class="mt-0 mb-3" style="border-top: 1px solid #edf2f7;">
                  
                  <div class="row align-items-end">
                    <div class="col-12 col-md-3 mb-3 mb-md-0">
                      <label class="control-label font-weight-bold mb-1" style="font-size: 12px; color: #666;">Nome do Animal</label>
                      <input type="text" v-model="filtro.nome" placeholder="Ex: Bob..." class="form-control custom-input" @keyup.enter="aplicarFiltros">
                    </div>
                    
                    <div class="col-12 col-md-3 mb-3 mb-md-0">
                      <label class="control-label font-weight-bold mb-1" style="font-size: 12px; color: #666;">Status / Situação</label>
                      <select v-model="filtro.statusId" class="form-control custom-select-filter" @change="aplicarFiltros">
                        <option value="">Todos os Status</option>
                        <option v-for="status in listaStatus" :key="status.id || status.stanId" :value="status.id || status.stanId">
                          {{ (status.descricao || status.nome) | removerUnderline | capitalizar }}
                        </option>
                      </select>
                    </div>

                    <div class="col-12 col-md-2 mb-3 mb-md-0">
                      <label class="control-label font-weight-bold mb-1" style="font-size: 12px; color: #666;">Porte</label>
                      <select v-model="filtro.porte" class="form-control custom-select-filter" @change="aplicarFiltros">
                        <option value="">Todos</option>
                        <option v-for="porte in listaPortes" :key="porte" :value="porte">
                          {{ porte | capitalizar }}
                        </option>
                      </select>
                    </div>

                    <div class="col-12 col-md-2 mb-3 mb-md-0">
                      <label class="control-label font-weight-bold mb-1" style="font-size: 12px; color: #666;">Sexo</label>
                      <select v-model="filtro.sexo" class="form-control custom-select-filter" @change="aplicarFiltros">
                        <option value="">Todos</option>
                        <option v-for="sexo in listaSexos" :key="sexo" :value="sexo">
                          {{ sexo | capitalizar }}
                        </option>
                      </select>
                    </div>

                    <div class="col-12 col-md-2 mb-3 mb-md-0 d-flex align-items-center" style="height: 40px;">
                      <div class="custom-control custom-checkbox pt-2">
                        <input type="checkbox" id="checkCastrados" v-model="filtro.apenasCastrados" class="custom-control-input" @change="aplicarFiltros">
                        <label class="custom-control-label font-weight-bold text-secondary" for="checkCastrados" style="font-size: 12px; cursor: pointer;">
                          Apenas Castrados
                        </label>
                      </div>
                    </div>
                  </div>

                  <div class="row mt-3">
                    <div class="col-12 d-flex justify-content-end align-items-center" style="gap: 10px;">
                      <button @click="limparFiltros" class="btn btn-light px-4 font-weight-bold text-muted border" style="height: 38px; font-size: 13px; border-radius: 4px;">
                        <i class="fa fa-eraser mr-1"></i> Limpar
                      </button>
                      <button @click="aplicarFiltros" class="btn btn-info px-4 font-weight-bold text-white" style="height: 38px; font-size: 13px; border-radius: 4px; background-color: #23ccef; border: none;">
                        <i class="fa fa-search mr-1"></i> Filtrar
                      </button>
                    </div>
                  </div>

                </div>
              </transition>

            </div>
          </div>
        </div>

      <!-- Listagem Principal -->
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
                
                <!-- Cabeçalho da Tabela Flexbox com Ordenação -->
                <div class="header-flex-row">
                  <div class="cell-arrow"></div>
                  
                  <div class="cell-nome-animal sortable-header" @click="ordenar('nome')">
                    NOME <i class="fa ml-1" :class="obterIconeOrdenacao('nome')"></i>
                  </div>
                  
                  <div class="cell-especie sortable-header" @click="ordenar('especie')">
                    ESPÉCIE / RAÇA <i class="fa ml-1" :class="obterIconeOrdenacao('especie')"></i>
                  </div>
                  
                  <div class="cell-idade sortable-header" @click="ordenar('idadeEstimada')">
                    IDADE ESTIMADA <i class="fa ml-1" :class="obterIconeOrdenacao('idadeEstimada')"></i>
                  </div>
                  
                  <div class="cell-castrado text-center">CASTRADO</div>
                  
                  <div class="cell-status-animal text-center sortable-header" @click="ordenar('status')">
                    STATUS <i class="fa ml-1" :class="obterIconeOrdenacao('status')"></i>
                  </div>
                  
                  <div class="cell-actions-animal text-center">AÇÕES</div>
                </div>

                <!-- Corpo da Tabela -->
                <div class="table-body-flex">
                  <div v-for="(animal, index) in animais" 
                       :key="'animal-' + index" 
                       class="clickable-row-group"
                       @click="$router.push(`/admin/animais/detalhes/${animal.id}`)">
                    <div class="main-row-data">
                      
                      <div class="cell-data cell-arrow text-center">
                        <i class="fa fa-chevron-right text-muted arrow-icon"></i>
                      </div>
                      
                      <div class="cell-data cell-nome-animal text-dark d-flex align-items-center">
                        <span class="font-weight-bold">{{ animal.nome }}</span>
                        <!-- Indicativo pequeno de foto -->
                        <span v-if="animal.fotosGaleria && animal.fotosGaleria.length > 0" title="Possui fotos registradas">
                          <i class="fa fa-camera text-info small"></i>
                        </span>
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
                      
                      <!-- Coluna Castrado -->
                      <div class="cell-data cell-castrado text-center">
                        <span v-if="animal.castrado && animal.dataCastracaoDesconhecida" class="badge badge-info shadow-xs px-2 py-1" style="font-size: 11px;">
                          <i class="fa fa-check"></i> Sim (S/ Data)
                        </span>
                        <span v-else-if="animal.castrado" class="badge badge-info shadow-xs px-2 py-1" style="font-size: 11px;" :title="'Data: ' + formatarDataSimples(animal.dataCastracao)">
                          <i class="fa fa-check"></i> Sim ({{ formatarDataSimples(animal.dataCastracao) }})
                        </span>
                        <span v-else class="badge badge-secondary shadow-xs px-2 py-1" style="font-size: 11px; opacity: 0.75;">
                          <i class="fa fa-times"></i> Não
                        </span>
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

              <!-- Paginação -->
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
      exibirFiltros: false,
      animais: [],
      paginaAtual: 0,
      totalPages: 0,
      totalElements: 0,
      listaStatus: [],
      listaPortes: [],
      listaSexos: [],
      // Parâmetros de Ordenação
      ordenarPor: 'nome',
      direcao: 'asc',
      filtro: {
        nome: '',
        statusId: '',
        porte: '',
        sexo: '',
        apenasCastrados: false,
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
        // Formata o sort combinando campo e direção (ex: "nome,asc" ou "nome,desc")
        const ordenacaoFormatada = this.ordenarPor ? `${this.ordenarPor},${this.direcao}` : null;

        const params = {
          page: this.paginaAtual,
          size: 10,
          sort: ordenacaoFormatada, // <-- Enviando no padrão do Spring Data (campo,direcao)
          nome: this.filtro.nome || null,
          statusId: this.filtro.statusId || null,
          porte: this.filtro.porte || null,
          sexo: this.filtro.sexo || null,
          possivelAdocao: this.filtro.possivelAdocao ? true : null
        };

        if (this.filtro.apenasCastrados) {
          params.castrado = true;
        }

        const response = await axios.get('/api/animais', { params });
        this.animais = response.data.content || [];
        
        if (response.data.page) {
          this.totalPages = response.data.page.totalPages || 0;
          this.totalElements = response.data.page.totalElements || 0;
        }
      } catch (error) {
        console.error("Erro ao carregar lista de animais:", error);
      } finally {
        this.loading = false;
      }
    },
    ordenar(campo) {
      if (this.ordenarPor === campo) {
        this.direcao = this.direcao === 'asc' ? 'desc' : 'asc';
      } else {
        this.ordenarPor = campo;
        this.direcao = 'asc';
      }
      this.paginaAtual = 0;
      this.fetchData();
    },
    obterIconeOrdenacao(campo) {
      if (this.ordenarPor !== campo) {
        return 'fa-sort text-muted opacity-50';
      }
      return this.direcao === 'asc' ? 'fa-sort-up text-info' : 'fa-sort-down text-info';
    },
    formatarIdade(idade) {
      if (!idade) return 'Não informada';
      if (!isNaN(idade)) {
        const num = parseInt(idade);
        return num === 1 ? '1 Ano' : `${num} Anos`;
      }
      return idade;
    },
    formatarDataSimples(dataString) {
      if (!dataString) return '';
      try {
        const apenasData = dataString.split('T')[0];
        const [ano, mes, dia] = apenasData.split('-');
        return `${dia}/${mes}/${ano}`;
      } catch (e) {
        return dataString;
      }
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
      this.filtro.apenasCastrados = false;
      this.filtro.possivelAdocao = false;
      this.ordenarPor = 'nome';
      this.direcao = 'asc';
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
.animated { animation-duration: 0.3s; animation-fill-mode: both; }
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
.fadeIn { animation-name: fadeIn; }

.table-flexbox { display: flex !important; flex-direction: column !important; width: 100% !important; }
.header-flex-row { display: flex !important; width: 100% !important; border-bottom: 2px solid #e3e8ee; background-color: #f8f9fa; padding: 14px 0; font-weight: 700; color: #555; font-size: 11px; letter-spacing: 0.5px; align-items: center; }
.clickable-row-group { display: flex !important; flex-wrap: wrap !important; width: 100% !important; border-bottom: 1px solid #edf2f7 !important; cursor: pointer; transition: all 0.15s ease-in-out; }
.clickable-row-group:hover { background-color: #f4f6f9; }
.clickable-row-group:hover .arrow-icon { color: #23ccef !important; transform: translateX(2px); }
.main-row-data { display: flex !important; width: 100% !important; align-items: center; }
.cell-data { padding: 14px 15px !important; font-size: 13.5px; }

/* Estilos de Ordenação do Cabeçalho */
.sortable-header {
  cursor: pointer;
  user-select: none;
  transition: color 0.2s ease;
}
.sortable-header:hover {
  color: #23ccef !important;
}

/* Proporções equilibradas do Flexbox */
.cell-arrow           { width: 4%; min-width: 40px; display: flex; justify-content: center; align-items: center; }
.cell-nome-animal     { width: 24%; padding: 0 15px; }
.cell-especie         { width: 22%; padding: 0 15px; }
.cell-idade           { width: 14%; padding: 0 15px; }
.cell-castrado        { width: 14%; }
.cell-status-animal   { width: 11%; padding: 0 15px; }
.cell-actions-animal  { width: 11%; }

.select-custom { height: 40px; border-radius: 4px; border: 1px solid #E3E3E3; color: #444; font-weight: 600; font-size: 13px; }
.shadow-label { font-size: 12.5px; color: #666; font-weight: 600; margin-bottom: 6px; }
.arrow-icon { font-size: 10px; transition: transform 0.2s ease; }
.italic-text { font-style: italic; }
.font-weight-600 { font-weight: 600; }
.gap-2 { gap: 0.5rem; }
.action-btn { transition: transform 0.1s ease; }
.action-btn:hover { transform: scale(1.15); }
.shadow-xs { box-shadow: 0 1px 2px rgba(0,0,0,0.05); }

/* Estilização para o Alinhamento do Checkbox */
.custom-checkbox-wrapper { display: flex; align-items: center; height: 40px; }
.clickable-label { cursor: pointer; user-select: none; padding-left: 5px; margin-bottom: 0; }

/* Alturas padronizadas e bordas suaves */
.custom-input, .custom-select-filter {
  height: 40px !important;
  border: 1px solid #E3E3E3 !important;
  border-radius: 4px !important;
  font-size: 13.5px !important;
  color: #444 !important;
  font-weight: 500 !important;
  box-shadow: none !important;
  transition: border-color 0.2s ease-in-out;
}

.custom-input:focus, .custom-select-filter:focus {
  border-color: #23ccef !important;
}

/* Customização para o Checkbox nativo do Bootstrap se destacar elegantemente */
.custom-checkbox .custom-control-input:checked ~ .custom-control-label::before {
  background-color: #23ccef !important;
  border-color: #23ccef !important;
}

.custom-control-label::before {
  border-radius: 4px !important;
  border: 1px solid #ced4da !important;
  width: 16px;
  height: 16px;
  top: 2px;
}

.custom-control-label::after {
  width: 16px;
  height: 16px;
  top: 2px;
}

/* Efeito de hover no cabeçalho do filtro */
.custom-collapse-header {
  transition: background-color 0.2s ease;
  border-radius: 8px;
}
.custom-collapse-header:hover {
  background-color: #f8f9fa;
}

/* Animação do Vue para abrir/fechar suavemente */
.fade-collapse-enter-active, .fade-collapse-leave-active {
  transition: all 0.3s ease-in-out;
  max-height: 300px;
  overflow: hidden;
  opacity: 1;
}
.fade-collapse-enter, .fade-collapse-leave-to {
  max-height: 0px;
  opacity: 0;
  padding-top: 0 !important;
  padding-bottom: 0 !important;
}
</style>