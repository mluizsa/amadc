<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12 col-md-8 offset-md-2">
          <card>
            <template slot="header">
              <h4 class="card-title">{{ isEdit ? 'Editar Voluntário' : 'Cadastrar Novo Voluntário' }}</h4>
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

              <!-- Seção de Perfis de Acesso (Visível principalmente na Edição) -->
              <div class="row" v-if="isEdit">
                <div class="col-md-12">
                  <div class="form-group">
                    <label>Perfis de Acesso ao Sistema</label>
                    <div class="p-2 border rounded bg-light">
                      <template v-if="form.perfisAcesso && form.perfisAcesso.length > 0">   
                        <span v-for="perfil in form.perfisAcesso" :key="perfil" class="badge badge-info mr-2 p-2">
                          {{ perfil }}
                        </span>
                      </template>
                      <template v-else>
                        <span class="text-muted font-italic">Este voluntário não possui usuário ou perfis de acesso vinculados ao sistema.</span>
                      </template>
                    </div>
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

              <!-- SEÇÃO DE DOCUMENTOS / ANEXOS (Visível apenas na Edição) -->
              <div v-if="isEdit" class="mt-4">
                <h5 class="access-title"><i class="fa fa-folder-open"></i> Documentos e Termos Vinculados</h5>
                
                <div class="row">
                  <div class="col-md-12">
                    <div class="form-group">
                      <label>Selecione os arquivos (Imagens ou PDF):</label>
                      <div class="input-group">
                        <input type="file" class="form-control" multiple accept="image/*,application/pdf" @change="onFilesSelected">
                        <div class="input-group-append" v-if="arquivosSelecionados.length > 0">
                          <button class="btn btn-info" type="button" @click="fazerUploadArquivos" :disabled="enviandoArquivos">
                            <i v-if="enviandoArquivos" class="fa fa-spinner fa-spin"></i>
                            {{ enviandoArquivos ? 'Enviando...' : `Enviar (${arquivosSelecionados.length})` }}
                          </button>
                        </div>
                      </div>
                      <small class="form-text text-muted">Você pode enviar termos assinados, fichas ou documentos de identificação.</small>
                    </div>
                  </div>
                </div>

                <!-- Lista de Arquivos já cadastrados -->
                <div class="row mt-2" v-if="listaArquivos.length > 0">
                  <div class="col-md-12">
                    <div class="table-responsive">
                      <table class="table table-sm table-bordered bg-white">
                        <thead>
                          <tr>
                            <th>Nome do Arquivo</th>
                            <th>Data</th>
                            <th class="text-center">Ações</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr v-for="arq in listaArquivos" :key="arq.id">
                            <td><a :href="arq.url" target="_blank" class="text-info"><i class="fa fa-file-o mr-1"></i> {{ arq.nomeOriginal || 'Documento' }}</a></td>
                            <td>{{ formatarDataHora(arq.dataCriacao) }}</td>
                            <td class="text-center">
                              <a :href="arq.url" target="_blank" class="btn btn-sm btn-info btn-link" title="Visualizar/Baixar">
                                <i class="fa fa-external-link"></i>
                              </a>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
                <div v-else class="text-muted small font-italic mt-2">
                  Nenhum documento anexado a este voluntário até o momento.
                </div>
              </div>

              <hr class="audit-divider">
              
              <div class="row mb-3">
                <div class="col-md-12">
                  <div class="access-toggle-container">
                    <label class="switch-label">
                      <input type="checkbox" v-model="form.permitirAcesso" class="custom-switch">
                      <span class="switch-slider"></span>
                    </label>
                    <span class="access-text">
                      <strong>Permitir que este voluntário acesse o sistema interno</strong>
                    </span>
                  </div>
                </div>
              </div>

              <div v-if="form.permitirAcesso" class="access-box animated fadeIn">
                <h5 class="access-title"><i class="fa fa-lock"></i> Credenciais de Segurança</h5>
                
                <div class="row">
                  <div class="col-md-6">
                    <div class="form-group">
                      <label>Nome de Usuário (Login) <span class="text-danger">*</span></label>
                      <input type="text" class="form-control" v-model="form.username" :required="form.permitirAcesso" placeholder="Ex: joao.amadc">
                    </div>
                  </div>
                  <div class="col-md-6">
                    <div class="form-group">
                      <label>
                        Senha Provisória 
                        <span v-if="!isEdit" class="text-danger">*</span>
                        <span v-else class="text-muted small">(Deixe em branco para não alterar)</span>
                      </label>
                      <input type="password" class="form-control" v-model="form.senhaProvisoria" :required="form.permitirAcesso && !isEdit" placeholder="Mínimo 6 caracteres">
                    </div>
                  </div>
                </div>

                <div class="row mt-2">
                  <div class="col-md-12">
                    <label class="d-block font-weight-bold">Perfis de Acesso vinculados:</label>
                    <div class="perfis-grid">
                      <div v-for="perfil in listaPerfis" :key="perfil.id" class="perfil-checkbox-item">
                        <label class="checkbox-container">
                          <input type="checkbox" :value="perfil.id" v-model="form.perfilIds">
                          <span class="checkmark"></span>
                          <span class="perfil-name">{{ formatPerfilNome(perfil.nome) }}</span>
                        </label>
                      </div>
                    </div>
                    <div v-if="listaPerfis.length === 0" class="text-muted small">
                      <i class="fa fa-spinner fa-spin"></i> Carregando perfis disponíveis...
                    </div>
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
      isEdit: false,
      listaPerfis: [],
      arquivosSelecionados: [],
      enviandoArquivos: false,
      listaArquivos: [],
      form: {
        id: null,
        nome: '',
        cpf: '',
        dataNascimento: '',
        telefone: '',
        email: '',
        ocupacao: '',
        observacoes: '',
        ativo: true,
        permitirAcesso: false,
        username: '',
        senhaProvisoria: '',
        perfilIds: []
      }
    }
  },
  methods: {
    onFilesSelected(event) {
      this.arquivosSelecionados = event.target.files;
    },

    async carregarArquivosVoluntario(id) {
      try {
        const response = await axios.get(`/api/arquivos/voluntario/${id}`);
        this.listaArquivos = response.data;
      } catch (error) {
        console.error("Erro ao carregar arquivos do voluntário:", error);
      }
    },

    async fazerUploadArquivos() {
      if (!this.arquivosSelecionados || this.arquivosSelecionados.length === 0) return;

      this.enviandoArquivos = true;
      const formData = new FormData();
      formData.append('tipoVinculo', 'VOLUNTARIO'); 

      for (let i = 0; i < this.arquivosSelecionados.length; i++) {
        formData.append('arquivos', this.arquivosSelecionados[i]);
      }

      try {
        await axios.post(`/api/arquivos/voluntario/${this.form.id}/upload`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });

        this.$notifications.notify({
          message: '<span><b>Sucesso!</b> Arquivos enviados com sucesso.</span>',
          icon: 'fa fa-check-circle',
          horizontalAlign: 'right',
          verticalAlign: 'top',
          type: 'success'
        });

        this.arquivosSelecionados = [];
        const fileInput = document.querySelector('input[type="file"]');
        if (fileInput) fileInput.value = '';

        await this.carregarArquivosVoluntario(this.form.id);
      } catch (error) {
        console.error("Erro no upload:", error);
        this.$notifications.notify({
          message: '<span><b>Erro</b> Falha ao enviar os arquivos. Verifique o tamanho ou formato.</span>',
          icon: 'fa fa-exclamation-triangle',
          horizontalAlign: 'right',
          verticalAlign: 'top',
          type: 'danger'
        });
      } finally {
        this.enviandoArquivos = false;
      }
    },

    formatarDataHora(dataIso) {
      if (!dataIso) return '';
      const data = new Date(dataIso);
      return data.toLocaleDateString('pt-BR') + ' ' + data.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    },
    
    async carregarPerfis() {
      try {
        const response = await axios.get('/api/perfis');
        this.listaPerfis = response.data;
      } catch (error) {
        console.error("Erro ao obter perfis de segurança:", error);
      }
    },

    async carregarVoluntario(id) {
      try {
        const response = await axios.get(`/api/voluntarios/${id}`);
        const dados = response.data;
        
        this.form.id = dados.id;
        this.form.nome = dados.nome;
        this.form.cpf = dados.cpf;
        this.form.dataNascimento = dados.dataNascimento;
        this.form.telefone = dados.telefone;
        this.form.email = dados.email;
        this.form.ocupacao = dados.ocupacao;
        this.form.observacoes = dados.observacoes;
        this.form.ativo = dados.ativo;

        if (dados.usuario && dados.usuario.ativo) {
          this.form.permitirAcesso = true;
          this.form.username = dados.usuario.username;
          this.form.senhaProvisoria = ''; 
          this.form.perfilIds = dados.usuario.perfis ? dados.usuario.perfis.map(p => p.id) : [];
        } else {
          this.form.permitirAcesso = false;
          this.form.username = dados.usuario ? dados.usuario.username : '';
          this.form.senhaProvisoria = '';
          this.form.perfilIds = [];
        }

        // CORREÇÃO: Chamava carregarVoluntario em loop infinito. Agora chama a listagem de arquivos correta:
        await this.carregarArquivosVoluntario(this.form.id);
        
      } catch (error) {
        console.error("Erro ao carregar dados do voluntário:", error);
        let errorMsg = 'Não foi possível recuperar os dados do voluntário.';

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

        this.goBack();
      }
    },

    async handleSubmit() {
      this.saving = true;
      try {
        const payload = {
          ...this.form,
          cpf: this.form.cpf.replace(/\D/g, '')
        };

        if (!payload.permitirAcesso) {
          payload.permitirAcesso = false;
          payload.username = null;
          payload.senhaProvisoria = null;
          payload.perfilIds = [];
        }

        let acaoTexto = '';
        if (this.isEdit) {
          await axios.put(`/api/voluntarios/${this.form.id}`, payload);
          acaoTexto = 'atualizado';
        } else {
          await axios.post('/api/voluntarios', payload);
          acaoTexto = 'cadastrado';
        }

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
    },

    formatPerfilNome(nome) {
      if (!nome) return '';
      return nome.replace(/_/g, ' ')
                 .toLowerCase()
                 .replace(/(^\w{1})|(\s+\w{1})/g, letter => letter.toUpperCase());
    }
  },
  mounted() {
    this.carregarPerfis();

    const idVoluntario = this.$route.params.id;
    if (idVoluntario) {
      this.isEdit = true;
      this.carregarVoluntario(idVoluntario);
    }
  }
}
</script>

<style scoped>
/* Estilos mantidos iguais */
.btn-fill { border-radius: 4px; font-weight: 600; }
.form-control { background-color: #FFFFFF; border: 1px solid #E3E3E3; border-radius: 4px; color: #565656; padding: 8px 12px; height: auto; }
textarea.form-control { height: auto !important; }
.audit-divider { margin: 30px 0 20px 0; border-top: 1px dashed #e3e3e3; }
.access-toggle-container { display: flex; align-items: center; background: #f9f9f9; padding: 12px 15px; border-radius: 6px; border: 1px solid #f0f0f0; }
.access-text { margin-left: 12px; color: #444; font-size: 14px; }
.access-box { background-color: #f4f7f6; border-left: 4px solid #1dc7ea; padding: 20px; border-radius: 0 6px 6px 0; margin-top: 15px; }
.access-title { margin-top: 0; margin-bottom: 15px; font-size: 15px; color: #333; font-weight: 600; }
.access-title i { color: #1dc7ea; margin-right: 5px; }
.perfis-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 10px; margin-top: 8px; }
.perfil-checkbox-item { background: #ffffff; padding: 8px 12px; border-radius: 4px; border: 1px solid #e3e3e3; }
.switch-label { position: relative; display: inline-block; width: 44px; height: 22px; margin-bottom: 0; vertical-align: middle; }
.custom-switch { opacity: 0; width: 0; height: 0; }
.switch-slider { position: absolute; cursor: pointer; top: 0; left: 0; right: 0; bottom: 0; background-color: #ccc; transition: .3s; border-radius: 22px; }
.switch-slider:before { position: absolute; content: ""; height: 16px; width: 16px; left: 3px; bottom: 3px; background-color: white; transition: .3s; border-radius: 50%; }
.custom-switch:checked + .switch-slider { background-color: #1dc7ea; }
.custom-switch:checked + .switch-slider:before { transform: translateX(22px); }
.checkbox-container { display: block; position: relative; padding-left: 28px; margin-bottom: 0; cursor: pointer; font-size: 13px; user-select: none; }
.checkbox-container input { position: absolute; opacity: 0; cursor: pointer; height: 0; width: 0; }
.checkmark { position: absolute; top: 1px; left: 0; height: 18px; width: 18px; background-color: #eee; border-radius: 3px; }
.checkbox-container:hover input ~ .checkmark { background-color: #ccc; }
.checkbox-container input:checked ~ .checkmark { background-color: #1dc7ea; }
.checkmark:after { content: ""; position: absolute; display: none; }
.checkbox-container input:checked ~ .checkmark:after { display: block; }
.checkbox-container .checkmark:after { left: 6px; top: 3px; width: 5px; height: 9px; border: solid white; border-width: 0 2px 2px 0; transform: rotate(45deg); }
.perfil-name { color: #565656; font-weight: 500; }
.animated { animation-duration: 0.4s; animation-fill-mode: both; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(-10px); } to { opacity: 1; transform: translateY(0); } }
.fadeIn { animation-name: fadeIn; }
</style>