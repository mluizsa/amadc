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
              <div class="table text-nowrap table-flexbox">
                
                <div class="header-flex-row">
                  <div class="cell-arrow"></div> <div class="cell-name">NOME</div>
                  <div class="cell-phone">TELEFONE</div>
                  <div class="cell-email">E-MAIL</div>
                  <div class="cell-status text-center">STATUS</div>
                  <div class="cell-access text-center">ACESSO</div>
                  <div class="cell-actions text-center">AÇÕES</div>
                </div>

                <div class="table-body-flex">
                  <template v-for="(item, index) in tableData.data">
                    <div
                      :key="'row-' + index"
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
                          <span :class="!item.ativo ? 'badge badge-danger' : 'badge badge-success'">
                            {{ !item.ativo ? 'Inativo' : 'Ativo' }}
                          </span>
                        </div>

                        <div class="cell-data cell-access text-center">
                          <span v-if="item.usuario && item.usuario.ativo" class="badge-acesso liberado">
                            <i class="fa fa-unlock-alt"></i> Liberado
                          </span>
                          <span v-else class="badge-acesso bloqueado">
                            <i class="fa fa-lock"></i> Bloqueado
                          </span>
                        </div>

                        <div class="cell-data cell-actions text-center" @click.stop>
                          <button class="btn btn-warning btn-link btn-xs" title="Editar" @click="handleEdit(item)">
                            <i class="fa fa-edit fa-lg"></i>
                          </button>
                          <button class="btn btn-danger btn-link btn-xs" title="Inativar" :disabled="item.ativo === false" @click="handleInactivate(item)">
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
                            <div v-if="item.usuario" class="col-12 col-sm-6 col-md-3 info-box">
                              <span class="info-label">Usuário do Sistema</span>
                              <p class="info-value text-info">@{{ item.usuario.username }}</p>
                            </div>
                          </div>

                          <div class="row m-0 mt-3">
                            <div class="col-12 info-box">
                              <span class="info-label">Observações Internas</span>
                              <p class="obs-text">{{ item.observacoes || 'Nenhuma observação cadastrada para este voluntário.' }}</p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </template>
                </div>

              </div>
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
          Este registro não constará mais na lista de voluntários activos.
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
        }
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
          const response = await axios.get(`/api/${endpoint}`);
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
      }
    },
    mounted() {
      this.fetchData();
    }
  }
</script>

<style scoped>
.custom-table-wrapper {
  width: 100%;
}

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

.clickable-row-group {
  cursor: pointer;
  display: flex !important;
  flex-wrap: wrap !important;
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

/* 🌟 Larguras recalculadas para incluir a nova coluna simetricamente */
.cell-arrow    { width: 5%; min-width: 45px; }
.cell-name     { width: 22%; }
.cell-phone    { width: 18%; }
.cell-email    { width: 25%; }
.cell-status   { width: 10%; }
.cell-access   { width: 10%; }
.cell-actions  { width: 10%; }

/* Estilização dos Badges de Acesso Customizados */
.badge-acesso {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 600;
}
.badge-acesso.liberated, .badge-acesso.liberado {
  background-color: #e6f9f3;
  color: #10b981;
}
.badge-acesso.bloqueado {
  background-color: #f3f4f6;
  color: #6b7280;
}

.full-width-dropdown {
  flex: 0 0 100% !important;
  width: 100% !important;
  max-width: 100% !important;
  box-sizing: border-box;
}

.detail-container {
  padding: 25px 30px 25px 50px;
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

.animated { animation-duration: 0.25s; animation-fill-mode: both; }
.fadeInFast { animation-name: fadeIn; animation-duration: 0.18s; }
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-4px); }
  to { opacity: 1; transform: translateY(0); }
}

.custom-modal-backdrop { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background-color: rgba(0, 0, 0, 0.4); z-index: 1050; backdrop-filter: blur(4px); }
.custom-modal-card { background: #FFFFFF; padding: 30px; border-radius: 12px; width: 100%; max-width: 420px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); text-align: center; }
.modal-icon-wrapper { font-size: 52px; margin-bottom: 15px; }
.modal-title { font-size: 22px; font-weight: 700; margin-bottom: 12px; }
.modal-text { color: #666; font-size: 15px; margin-bottom: 25px; }
.modal-actions-buttons { display: flex; justify-content: center; gap: 15px; }
.modal-actions-buttons .btn { padding: 10px 24px; font-weight: 600; min-width: 120px; }
</style>