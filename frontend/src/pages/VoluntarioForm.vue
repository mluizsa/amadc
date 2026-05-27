<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12 col-md-8 offset-md-2">
          <card>
            <template slot="header">
              <h4 class="card-title">Cadastrar Novo Voluntário</h4>
              <p class="card-category">Insira os dados para a associação AMA DC</p>
            </template>

            <form @submit.prevent="handleSubmit">
              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label>Nome Completo</label>
                    <input type="text" class="form-control" v-model="form.nome" required placeholder="Ex: João Silva">
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-6">
                  <div class="form-group">
                    <label>CPF (Apenas números)</label>
                    <input type="text" class="form-control" v-model="form.cpf" required placeholder="Ex: 12345678901" maxlength="11">
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group">
                    <label>Data de Nascimento</label>
                    <input type="date" class="form-control" v-model="form.dataNascimento" required>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-6">
                  <div class="form-group">
                    <label>Telefone</label>
                    <input type="text" class="form-control" v-model="form.telefone" required placeholder="Ex: 31988887766">
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group">
                    <label>Ocupação / Profissão</label>
                    <input type="text" class="form-control" v-model="form.ocupacao" placeholder="Ex: Veterinário, Estudante">
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label>E-mail</label>
                    <input type="email" class="form-control" v-model="form.email" required placeholder="joao@email.com">
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label>Observações</label>
                    <textarea rows="4" class="form-control" v-model="form.observacoes" placeholder="Informações adicionais relevantes sobre o voluntário..."></textarea>
                  </div>
                </div>
              </div>

              <div class="d-flex justify-content-between align-items-center mt-4">
                <button type="button" class="btn btn-secondary btn-fill" @click="goBack">
                  Cancelar
                </button>
                <button type="submit" class="btn btn-info btn-fill" :disabled="saving">
                  <i v-if="saving" class="fa fa-spinner fa-spin"></i>
                  {{ saving ? 'Salvando...' : 'Salvar Voluntário' }}
                </button>
              </div>
            </form>
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
      saving: false,
      isEdit: false, // Flag para sabermos o modo atual da tela
      form: {
        id: null,
        nome: '',
        cpf: '',
        dataNascimento: '',
        telefone: '',
        email: '',
        ocupacao: '',
        observacoes: '',
        ativo: true
      }
    }
  },
  methods: {
    // 1. Busca os dados do voluntário caso seja Edição
    async carregarVoluntario(id) {
          try {
            const response = await axios.get(`/api/voluntarios/${id}`);
            this.form = response.data;
            console.log("Dados do voluntário carregados:", this.form);
          } catch (error) {
            console.error("Erro ao carregar dados do voluntário:", error);

            let errorMsg = 'Não foi possível recuperar os dados do voluntário.';

            // Se o backend respondeu com o 404 do seu TratadorDeErros
            if (error.response && error.response.status === 404) {
              errorMsg = 'O voluntário solicitado não foi encontrado no sistema (ID inexistente).';
            } else if (error.response && error.response.data && error.response.data.message) {
              errorMsg = error.response.data.message;
            }

            this.$notifications.notify({
              message: `<span><b>Aviso:</b> ${errorMsg}</span>`,
              icon: 'fa fa-exclamation-triangle',
              horizontalAlign: 'right',
              verticalAlign: 'top',
              type: 'danger'
            });

            this.goBack(); // Retorna para a listagem com segurança
          }
        },

    // 2. Decide se faz um POST (Novo) ou PUT (Atualizar)
    async handleSubmit() {
      this.saving = true;
      try {
        // Sanitiza o CPF para salvar apenas os números exigidos pelo DTO
        const payload = {
          ...this.form,
          cpf: this.form.cpf.replace(/\D/g, '')
        };

        if (this.isEdit) {
          // Cenário de Edição: Chama o @PutMapping("/{id}") da sua controller
          await axios.put(`/api/voluntarios/${this.form.id}`, payload);
          var acaoTexto = 'atualizado';
        } else {
          // Cenário de Cadastro: Chama o @PostMapping
          await axios.post('/api/voluntarios', payload);
          var acaoTexto = 'cadastrado';
        }

        // Notificação Fluida de Sucesso
        this.$notifications.notify({
          message: `<span><b>Sucesso!</b> Voluntário ${this.form.nome} foi ${acaoTexto} com sucesso.</span>`,
          icon: 'fa fa-check-circle',
          horizontalAlign: 'right',
          verticalAlign: 'top',
          type: 'success'
        });

        setTimeout(() => {
          this.goBack();
        }, 1500);

      } catch (error) {
        console.error('Erro detalhado capturado:', error);
        let errorMsg = 'Não foi possível salvar o registro.';

        if (error.response && error.response.data) {
          const data = error.response.data;
          if (Array.isArray(data)) {
            errorMsg = data.map(e => `<b>${(e.campo || e.field || '').toUpperCase()}:</b> ${e.erro || e.message}`).join('<br>');
          } else if (data.message) {
            errorMsg = data.message;
          } else if (typeof data === 'string') {
            errorMsg = data;
          }
        }

        this.$notifications.notify({
          message: `<span><i class="fa fa-times-circle"></i> <b>Falha na operação</b><br>${errorMsg}</span>`,
          icon: 'fa fa-exclamation-triangle',
          horizontalAlign: 'right',
          verticalAlign: 'top',
          type: 'danger'
        });
      } finally {
        this.saving = false;
      }
    },
    goBack() {
      this.$router.push('/admin/voluntarios');
    }
  },
  mounted() {
    // Verifica se a rota atual possui o parâmetro id
    const idVoluntario = this.$route.params.id;
    if (idVoluntario) {
      this.isEdit = true;
      this.carregarVoluntario(idVoluntario);
    }
  }
}
</script>

<style scoped>
.btn-fill {
  border-radius: 4px;
  font-weight: 600;
}
.form-control {
  background-color: #FFFFFF;
  border: 1px solid #E3E3E3;
  border-radius: 4px;
  color: #565656;
  padding: 8px 12px;
  height: auto;
}
textarea.form-control {
  height: auto !important;
}
</style>
