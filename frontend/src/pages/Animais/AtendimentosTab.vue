<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3 border-bottom pb-2">
      <h5 class="text-dark font-weight-bold m-0">Histórico de Atendimentos</h5>
      <button @click="abrirModalCadastro" class="btn btn-outline-info btn-sm font-weight-bold shadow-sm">
        <i class="fa fa-plus mr-1"></i> Novo Atendimento
      </button>
    </div>

    <!-- Tabela de Listagem -->
    <div v-if="loading" class="text-center p-4">
      <div class="spinner-border text-info spinner-border-sm" role="status"></div>
      <span class="text-muted ml-2 small">Carregando atendimentos...</span>
    </div>

    <div v-else-if="atendimentos.length > 0" class="table-responsive">
      <table class="table table-hover table-striped">
        <thead>
          <tr>
            <th class="border-top-0">Data</th>
            <th class="border-top-0">Tipo</th>
            <th class="border-top-0">Peso (kg)</th>
            <th class="border-top-0">Valor</th>
            <th class="border-top-0 text-center">Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in atendimentos" :key="item.id">
            <td>{{ formatarData(item.dataAtendimento) }}</td>
            <td>
              <span class="badge badge-info p-2" style="font-size: 11px;">
                {{ item.tipoAtendimento ? formatarTexto(item.tipoAtendimento.descricao) : 'N/A' }}
              </span>
            </td>
            <td>{{ item.pesoNaData ? `${item.pesoNaData} kg` : '-' }}</td>
            <td>{{ formatarMoeda(item.valorCobrado) }}</td>
            <td class="text-center">
              <button @click="confirmarExclusao(item.id)" class="btn btn-link text-danger btn-sm p-0" title="Excluir Atendimento">
                <i class="fa fa-trash" style="font-size: 14px;"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-else class="text-center p-4 bg-light rounded border text-muted">
      <i class="fa fa-stethoscope fa-2x mb-2 text-secondary"></i>
      <h6 class="font-weight-bold mb-1">Nenhum atendimento registrado</h6>
      <p class="mb-0 small">Utilize o botão acima para lançar a primeira consulta ou procedimento.</p>
    </div>

    <!-- Modal de Cadastro / Formulário -->
    <div v-if="modalCadastro" class="modal-zoom-backdrop" @click="modalCadastro = false">
      <div class="card border-0 shadow-lg bg-white m-3" style="max-width: 550px; width: 100%; border-radius: 8px;" @click.stop>
        <div class="card-header bg-white border-bottom py-3 d-flex justify-content-between align-items-center">
          <h5 class="m-0 font-weight-bold text-dark">Registrar Atendimento</h5>
          <button type="button" class="close text-secondary border-0 bg-transparent" @click="modalCadastro = false" style="font-size: 20px;">&times;</button>
        </div>
        <div class="card-body p-4">
          <form @submit.prevent="salvarAtendimento">
            <div class="form-group">
              <label class="small font-weight-bold text-uppercase text-secondary">Tipo de Atendimento</label>
              <select v-model="novoAtendimento.tipoAtendimentoId" class="form-control" required>
                <option :value="null" disabled>Selecione o tipo...</option>
                <option v-for="tipo in tiposAtendimento" :key="tipo.id" :value="tipo.id">
                    {{ formatarTexto(tipo.descricao) }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label class="small font-weight-bold text-uppercase text-secondary">Data e Hora</label>
              <input v-model="novoAtendimento.dataAtendimento" type="datetime-local" class="form-control" required />
            </div>

            <div class="row">
              <div class="col-md-6 form-group">
                <label class="small font-weight-bold text-uppercase text-secondary">Peso na Data (kg)</label>
                <input v-model="novoAtendimento.pesoNaData" type="number" step="0.1" class="form-control" placeholder="Ex: 12.5" />
              </div>
              <div class="col-md-6 form-group">
                <label class="small font-weight-bold text-uppercase text-secondary">Valor Cobrado (R$)</label>
                <input v-model="novoAtendimento.valorCobrado" type="number" step="0.01" class="form-control" placeholder="0.00" />
              </div>
            </div>

            <div class="form-group mb-0">
              <label class="small font-weight-bold text-uppercase text-secondary">Descrição / Laudo / Observações</label>
              <textarea v-model="novoAtendimento.descricao" class="form-control" rows="3" placeholder="Detalhes da consulta ou procedimento..."></textarea>
            </div>

            <div class="text-right mt-4 pt-3 border-top">
              <button type="button" class="btn btn-outline-secondary btn-sm mr-2" @click="modalCadastro = false">Cancelar</button>
              <button type="submit" class="btn btn-info btn-sm font-weight-bold px-4" style="background-color: #23ccef; border: none;">Salvar</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import atendimentoService from '@/services/atendimentoService';

export default {
  name: 'AtendimentosTab',
  props: {
    animalId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      atendimentos: [],
      tiposAtendimento: [
        { id: 1, descricao: 'CONSULTA_ROTINA' },
        { id: 2, descricao: 'VACINACAO' },
        { id: 3, descricao: 'CASTRACAO' },
        { id: 4, descricao: 'CIRURGIA' },
        { id: 5, descricao: 'EXAME_LABORATORIAL' },
        { id: 6, descricao: 'URGENCIA_EMERGENCIA' }
      ],
      modalCadastro: false,
      novoAtendimento: {
        animalId: this.animalId,
        tipoAtendimentoId: null,
        dataAtendimento: '',
        pesoNaData: null,
        valorCobrado: null,
        descricao: ''
      }
    };
  },
  mounted() {
    this.carregarAtendimentos();
  },
  methods: {
    async carregarAtendimentos() {
      this.loading = true;
      try {
        const response = await atendimentoService.listarPorAnimal(this.animalId);
        this.atendimentos = response.data || [];
      } catch (error) {
        console.error('Erro ao carregar atendimentos:', error);
      } finally {
        this.loading = false;
      }
    },
    abrirModalCadastro() {
      // Formata a data atual para o input datetime-local (YYYY-MM-DDThh:mm)
      const agora = new Date();
      const dataIsoLocal = new Date(agora.getTime() - (agora.getTimezoneOffset() * 60000)).toISOString().slice(0, 16);

      this.novoAtendimento = {
        animalId: this.animalId,
        tipoAtendimentoId: null,
        dataAtendimento: dataIsoLocal,
        pesoNaData: null,
        valorCobrado: null,
        descricao: ''
      };
      this.modalCadastro = true;
    },
    async salvarAtendimento() {
      try {
        await atendimentoService.cadastrar(this.novoAtendimento);
        this.modalCadastro = false;
        this.carregarAtendimentos();
      } catch (error) {
        console.error('Erro ao salvar atendimento:', error);
        alert('Erro ao registrar o atendimento.');
      }
    },
    async confirmarExclusao(id) {
      if (confirm('Deseja realmente excluir este atendimento?')) {
        try {
          await atendimentoService.deletar(id);
          this.carregarAtendimentos();
        } catch (error) {
          console.error('Erro ao excluir atendimento:', error);
          alert('Erro ao excluir o registro.');
        }
      }
    },
    formatarTexto(valor) {
      if (!valor) return '';
      return valor.toString().replace(/_/g, ' ');
    },
    formatarData(dataStr) {
      if (!dataStr) return '';
      try {
        const dataObj = new Date(dataStr);
        return dataObj.toLocaleString('pt-BR', {
          day: '2-digit',
          month: '2-digit',
          year: 'numeric',
          hour: '2-digit',
          minute: '2-digit'
        });
      } catch (e) {
        return dataStr;
      }
    },
    formatarMoeda(valor) {
      if (!valor) return 'R$ 0,00';
      return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);
    }
  }
};
</script>

<style scoped>
.modal-zoom-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  padding: 20px;
}
</style>