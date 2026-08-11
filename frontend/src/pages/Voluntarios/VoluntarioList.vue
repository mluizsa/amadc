<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <card class="strpied-tabled-with-hover">
            <template slot="header">
              <div class="d-flex justify-content-between align-items-center flex-wrap">
                <div>
                  <h4 class="card-title m-0">Gestão de {{ $route.name }}</h4>
                  <p class="card-category">Clique em uma linha para ver os detalhes completos</p>
                </div>
                <button @click="$router.push('/admin/voluntarios/novo')" class="btn btn-info btn-fill btn-sm mt-2 mt-sm-0">
                  <i class="fa fa-plus"></i> Novo Registro
                </button>
              </div>
            </template>

            <div v-if="loading" class="text-center p-5">
              <div class="spinner-border text-info" role="status"></div>
              <p class="mt-2 text-muted">Buscando informações no servidor...</p>
            </div>

            <div v-else class="table-responsive custom-table-wrapper">
            <table class="table text-nowrap table-flexbox">
              <thead>
                <tr class="header-flex-row">
                  <th class="cell-arrow"></th>
                  <th class="cell-name sortable" @click="sortBy('nome')">
                    NOME 
                    <i class="fa" :class="sortKey === 'nome' ? (sortOrder === 'asc' ? 'fa-sort-alpha-asc' : 'fa-sort-alpha-desc') : 'fa-sort text-muted'"></i>
                  </th>
                  <th class="cell-phone d-none d-md-flex">TELEFONE</th>
                  <th class="cell-email sortable" @click="sortBy('email')">
                    E-MAIL
                    <i class="fa" :class="sortKey === 'email' ? (sortOrder === 'asc' ? 'fa-sort-alpha-asc' : 'fa-sort-alpha-desc') : 'fa-sort text-muted'"></i>
                  </th>
                  <th class="cell-status text-center sortable" @click="sortBy('ativo')">
                    STATUS
                    <i class="fa" :class="sortKey === 'ativo' ? (sortOrder === 'asc' ? 'fa-sort-amount-asc' : 'fa-sort-amount-desc') : 'fa-sort text-muted'"></i>
                  </th>
                  <th class="cell-actions text-center">AÇÕES</th>
                </tr>
              </thead>
              <tbody>
                <template v-for="(item, index) in voluntariosOrdenados">
                  <tr
                    :key="'row-' + item.id"
                    @click="toggleRow(item.id)"
                    class="clickable-row-group"
                    :class="{'row-group-expanded': expandedRows.includes(item.id)}"
                  >
                    <div class="main-row-data">
                      <div class="cell-data cell-arrow">
                        <i class="fa fa-chevron-right text-muted arrow-icon" :class="{'rotate-arrow': expandedRows.includes(item.id)}"></i>
                      </div>
                      <div class="cell-data cell-name"><b>{{ item.nome }}</b></div>
                      <div class="cell-data cell-phone">{{ item.telefone || 'Não informado' }}</div>
                      <div class="cell-data cell-email">{{ item.email }}</div>
                      <div class="cell-data cell-status text-center">
                        <span :class="item.ativo ? 'badge badge-success' : 'badge badge-danger'">
                          {{ item.ativo ? 'Ativo' : 'Inativo' }}
                        </span>
                      </div>
                      <div class="cell-data cell-actions text-center" @click.stop>
                        <button class="btn btn-warning btn-link btn-xs" title="Editar" @click="handleEdit(item)">
                          <i class="fa fa-edit fa-lg"></i>
                        </button>
                        <button class="btn btn-danger btn-link btn-xs" 
                                title="Inativar" 
                                :disabled="!item.ativo" 
                                @click="handleInactivate(item)">
                          <i class="fa fa-ban fa-lg"></i>
                        </button>
                      </div>
                    </div>

                    <div v-if="expandedRows.includes(item.id)" class="full-width-dropdown animated fadeInFast" @click.stop>
                      <div class="detail-container">
                        <h5 class="detail-section-title">
                          <i class="fa fa-id-card text-info mr-2"></i> Informações Detalhadas
                        </h5>

                        <div class="row m-0 mt-3">
                          <div class="col-12 col-sm-6 col-md-3 info-box">
                            <span class="info-label">CPF</span>
                            <p class="info-value">{{ item.cpf || 'Não informado' }}</p>
                          </div>
                          <div class="col-12 col-sm-6 col-md-3 info-box">
                            <span class="info-label">Data de Nascimento</span>
                            <p class="info-value">{{ formatarData(item.dataNascimento) || 'Não informada' }}</p>
                          </div>
                          <div class="col-12 col-sm-6 col-md-3 info-box">
                            <span class="info-label">Ocupação / Cargo</span>
                            <p class="info-value">{{ item.ocupacao || 'Não informada' }}</p>
                          </div>
                        </div>

                        <div class="row m-0 mt-3">
                          <div class="col-12 info-box">
                            <span class="info-label">Observações Internas</span>
                            <p class="obs-text">{{ item.observacoes || 'Nenhuma observação cadastrada para este voluntário.' }}</p>
                          </div>
                        </div>

                        <!-- Perfils -->
                         <div class="row m-0 mt-3">
                          <div class="col-12 col-sm-6 info-box">
                            <span class="info-label">Perfis de Acesso ao Sistema</span>
                            <p class="info-value">
                              <template v-if="item.perfisAcesso && item.perfisAcesso.length > 0">
                                <span v-for="perfil in item.perfisAcesso" :key="perfil" class="badge badge-info mr-1">
                                  {{ perfil }}
                                </span>
                              </template>
                              <template v-else>
                                <span class="text-muted font-italic">Sem acesso ao sistema configurado</span>
                              </template>
                            </p>
                          </div>
                        </div>
                        <!-- Perfils -->
                      </div>
                    </div>
                  </tr>
                </template>
              </tbody>
            </table>
          </div>

            <div v-if="!loading && tableData.data.length === 0" class="text-center p-4 text-muted">
              Nenhum registro encontrado.
            </div>
          </card>
        </div>
      </div>
    </div>

    <div v-if="modalInativar.show" class="custom-modal-backdrop d-flex align-items-center justify-content-center">
      <div class="custom-modal-card animated fadeIn">
        <div class="modal-icon-wrapper text-danger">
          <i class="fa fa-exclamation-circle"></i>
        </div>
        <h3 class="modal-title">Desativar Voluntário</h3>
        <p class="modal-text">
          Você está prestes a desativar <strong>{{ modalInativar.item ? modalInativar.item.nome : '' }}</strong>.<br>
          Este registro não constará mais na lista de voluntários ativos.
        </p>
        <div class="modal-actions-buttons">
          <button class="btn btn-neutral btn-fill" @click="fecharModalInativar">Cancelar</button>
          <button class="btn btn-danger btn-fill" :disabled="modalInativar.submitting" @click="confirmarInativacao">
            <i v-if="modalInativar.submitting" class="fa fa-spinner fa-spin"></i>
            {{ modalInativar.submitting ? 'Desativando...' : 'Sim, Desativar' }}
          </button>
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
    data () {
      return {
        loading: false,
        tableData: {
          columns: [],
          data: []
        },
        expandedRows: [],
        modalInativar: {
          show: false,
          submitting: false,
          item: null
        },
        // NOVAS VARIÁVEIS DE ORDENAÇÃO
        sortKey: 'nome',
        sortOrder: 'asc'
      }
    },
    // NOVO BLOCO COMPUTED
    computed: {
      voluntariosOrdenados() {
        if (!this.tableData.data) return [];
        
        return [...this.tableData.data].sort((a, b) => {
          let valorA = a[this.sortKey] || '';
          let valorB = b[this.sortKey] || '';
          
          // Ignora maiúsculas/minúsculas na ordenação de texto
          if (typeof valorA === 'string') valorA = valorA.toLowerCase();
          if (typeof valorB === 'string') valorB = valorB.toLowerCase();
          
          if (valorA < valorB) return this.sortOrder === 'asc' ? -1 : 1;
          if (valorA > valorB) return this.sortOrder === 'asc' ? 1 : -1;
          return 0;
        });
      }
    },
    watch: {
      '$route': 'fetchData'
    },
    methods: {
      async fetchData() {
        this.loading = true;
        try {
          const endpoint = this.$route.path.replace('/admin/', '');
          const response = await axios.get(`/api/voluntarios`);
          this.tableData.data = response.data;
        } catch (error) {
          console.error("Erro ao carregar dados", error);
        } finally {
          this.loading = false;
        }
      },

      toggleRow(id) {
        const index = this.expandedRows.indexOf(id);
        if (index > -1) {
          this.expandedRows.splice(index, 1);
        } else {
          this.expandedRows.push(id);
        }
      },

      formatarData(dataIso) {
        if (!dataIso) return 'Não informada';

        // Trata strings de data puras como '1995-10-25' quebrando os hífenes
        // Isso evita bugs de fuso horário que o "new Date()" nativo costuma causar
        const partes = dataIso.split('-');
        if (partes.length === 3) {
          const [ano, mes, dia] = partes;
          return `${dia}/${mes}/${ano}`;
        }

        return dataIso;
      },

      handleEdit(item) {
        this.$router.push(`/admin/voluntarios/editar/${item.id}`);
      },

      handleInactivate(item) {
        this.modalInativar.item = item;
        this.modalInativar.show = true;
      },

      fecharModalInativar() {
        this.modalInativar.show = false;
        this.modalInativar.item = null;
        this.modalInativar.submitting = false;
      },

      async confirmarInativacao() {
        const item = this.modalInativar.item;
        if (!item) return;

        this.modalInativar.submitting = true;
        try {
          const endpoint = this.$route.path.replace('/admin/', '');
          await axios.delete(`/api/${endpoint}/${item.id}`);

          this.fecharModalInativar();
          this.$notifications.notify({
            message: `<span><b>Sucesso!</b> O voluntário ${item.nome} foi desativado.</span>`,
            icon: 'fa fa-check-circle',
            horizontalAlign: 'right',
            verticalAlign: 'top',
            type: 'success'
          });
          this.fetchData();
        } catch (error) {
          console.error("Erro ao desativar:", error);
          this.modalInativar.submitting = false;
          this.$notifications.notify({
            message: `<span><b>Erro</b> Não foi possível inativar o registro.</span>`,
            icon: 'fa fa-exclamation-triangle',
            horizontalAlign: 'right',
            verticalAlign: 'top',
            type: 'danger'
          });
        }
      },
      // NOVO MÉTODO DE ORDENAÇÃO
      sortBy(key) {
        if (this.sortKey === key) {
          this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
        } else {
          this.sortKey = key;
          this.sortOrder = 'asc';
        }
      }
    },
    mounted() {
      this.fetchData();
    }
  }
</script>

<style scoped>
/* Torna os cabeçalhos clicáveis */
th.sortable {
  cursor: pointer;
  user-select: none;
  transition: color 0.2s;
}

th.sortable:hover {
  color: #00bcd4 !important; /* Cor info do seu tema */
}

th.sortable i {
  margin-left: 5px;
  font-size: 0.85em;
}

/* Ajustes responsivos para evitar colunas espremidas em telas pequenas (< 768px) */
@media (max-width: 767px) {
  .cell-name  { width: 40%; } /* Pega o espaço do telefone */
  .cell-email { width: 35%; }
  .cell-status{ width: 15%; }
  .cell-actions{ width: 10%; min-width: 70px;}
  
  /* Ajusta o texto da linha expandida para não quebrar */
  .info-box { margin-bottom: 20px; }
}

.custom-table-wrapper {
  width: 100%;
}

/* Força a tabela inteira a seguir flexbox para harmonizar com o tema */
.table-flexbox {
  display: flex !important;
  flex-direction: column !important;
  width: 100% !important;
  border: none !important;
}

.table-flexbox thead,
.table-flexbox tbody {
  display: flex !important;
  flex-direction: column !important;
  width: 100% !important;
}

/* Alinhamento perfeito do cabeçalho */
.header-flex-row {
  display: flex !important;
  width: 100% !important;
  border-bottom: 2px solid #e3e8ee;
  background-color: #fafbfc;
}

.header-flex-row th {
  border: none !important;
  padding: 12px 15px !important;
  font-weight: 600;
  color: #666;
}

/* Estrutura das Linhas de Registro */
.clickable-row-group {
  cursor: pointer;
  display: flex !important;
  flex-wrap: wrap !important; /* Permite que o painel expandido caia para baixo */
  width: 100% !important;
  transition: background-color 0.2s ease;
  border-bottom: 1px solid #eeeeee !important;
}

.clickable-row-group:hover {
  background-color: rgba(0, 188, 212, 0.04) !important;
}

.row-group-expanded {
  background-color: #fcfdfe !important;
}

/* Alinhamento estrito horizontal dos dados superiores */
.main-row-data {
  display: flex !important;
  width: 100% !important;
  align-items: center;
}

.cell-data {
  padding: 14px 15px !important;
  box-sizing: border-box;
  text-overflow: ellipsis;
  overflow: hidden;
}

/* Definição exata das larguras das colunas */
.cell-arrow    { flex: 0 0 5%;  width: 5%;  min-width: 45px; }
.cell-name     { flex: 0 0 25%; width: 25%; }
.cell-phone    { flex: 0 0 20%; width: 20%; }
.cell-email    { flex: 0 0 30%; width: 30%; }
.cell-status   { flex: 0 0 10%; width: 10%; text-align: center; }
.cell-actions  { flex: 0 0 10%; width: 10%; text-align: center; }

/* Dropdown Ocupando 100% real abaixo dos dados principais */
.full-width-dropdown {
  flex: 0 0 100% !important;
  width: 100% !important;
  max-width: 100% !important;
  box-sizing: border-box;
}

.detail-container {
  padding: 25px 30px 25px 50px; /* Recuo extra para alinhar após a seta */
  border-left: 4px solid #00bcd4;
  background-color: #f8fafc;
  width: 100%;
  box-sizing: border-box;
  border-bottom: 1px solid #e3e8ee;
}

.detail-section-title {
  margin: 0 0 15px 0;
  font-size: 13px;
  font-weight: 700;
  color: #555555;
  text-transform: uppercase;
  letter-spacing: 0.8px;
}

/* Configuração dos blocos internos de informação */
.info-box {
  margin-bottom: 15px;
  text-align: left !important;
}

.info-label {
  font-size: 11px;
  text-transform: uppercase;
  color: #888888;
  font-weight: 600;
  margin-bottom: 4px;
  display: block;
  letter-spacing: 0.5px;
}

.info-value {
  font-size: 14px;
  color: #333333;
  margin: 0 !important;
  font-weight: 500;
}

.obs-text {
  background: #ffffff;
  padding: 12px 16px;
  border-radius: 6px;
  border: 1px solid #e3e8ee;
  font-style: italic;
  color: #555555;
  margin-top: 5px;
  line-height: 1.5;
}

/* Animações e Ícones */
.arrow-icon {
  transition: transform 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.rotate-arrow {
  transform: rotate(90deg);
  color: #00bcd4 !important;
}

.badge {
  padding: 5px 10px;
  font-size: 0.75rem;
  border-radius: 4px;
}

.action-cells .btn-link {
  padding: 5px 10px;
  margin: 0 2px;
  border: none;
}

.animated { animation-duration: 0.25s; animation-fill-mode: both; }
.fadeInFast { animation-name: fadeIn; animation-duration: 0.18s; }
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-4px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Modal CSS */
.custom-modal-backdrop { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background-color: rgba(0, 0, 0, 0.4); z-index: 1050; backdrop-filter: blur(4px); }
.custom-modal-card { background: #FFFFFF; padding: 30px; border-radius: 12px; width: 100%; max-width: 420px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); text-align: center; }
.modal-icon-wrapper { font-size: 52px; margin-bottom: 15px; }
.modal-title { font-size: 22px; font-weight: 700; margin-bottom: 12px; }
.modal-text { color: #666; font-size: 15px; margin-bottom: 25px; }
.modal-actions-buttons { display: flex; justify-content: center; gap: 15px; }
.modal-actions-buttons .btn { padding: 10px 24px; font-weight: 600; min-width: 120px; }

/* Ajustes responsivos para evitar colunas espremidas em telas pequenas (< 768px) */
@media (max-width: 767px) {
  .cell-phone { 
    display: none !important; 
  }
  
  /* Redistribui o espaço do telefone para as outras colunas */
  .cell-name  { flex: 0 0 35%; width: 35%; } 
  .cell-email { flex: 0 0 35%; width: 35%; }
  .cell-status{ flex: 0 0 15%; width: 15%; }
  .cell-actions{ flex: 0 0 15%; width: 15%; min-width: 70px; }
  
  /* Ajusta o texto da linha expandida para não quebrar */
  .info-box { margin-bottom: 20px; }
}
</style>
