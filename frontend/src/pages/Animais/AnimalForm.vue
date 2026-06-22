<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <card class="shadow-sm border-0 bg-white" style="border-radius: 8px;">
            
            <template slot="header">
              <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
                <div>
                  <h4 class="card-title m-0 text-dark font-weight-bold">
                    {{ isEdit ? 'Editar Cadastro' : 'Novo Registro de Animal' }}
                  </h4>
                  <p class="card-category text-muted mb-0">
                    Siga as etapas para preencher a Ficha Clínica da AMA DC
                  </p>
                </div>
                <button type="button" @click="$router.push('/admin/animais')" class="btn btn-default btn-outline btn-sm font-weight-600 shadow-sm">
                  <i class="fa fa-arrow-left mr-1"></i> Voltar para a Lista
                </button>
              </div>
            </template>

            <div v-if="loading" class="text-center p-5">
              <div class="spinner-border text-info my-3" role="status"></div>
              <p class="text-muted font-weight-600">Processando informações...</p>
            </div>

            <div v-else class="p-2">
              
              <div class="stepper-wrapper mb-4">
                <div class="stepper-item" :class="{ 'active': etapaAtual === 1, 'completed': etapaAtual > 1 }">
                  <div class="step-counter">1</div>
                  <div class="step-name d-none d-sm-block">Identificação</div>
                </div>
                <div class="stepper-item" :class="{ 'active': etapaAtual === 2, 'completed': etapaAtual > 2 }">
                  <div class="step-counter">2</div>
                  <div class="step-name d-none d-sm-block">Saúde</div>
                </div>
                <div class="stepper-item" :class="{ 'active': etapaAtual === 3, 'completed': etapaAtual > 3 }">
                  <div class="step-counter">3</div>
                  <div class="step-name d-none d-sm-block">Resgate</div>
                </div>
                <div class="stepper-item" :class="{ 'active': etapaAtual === 4 }">
                  <div class="step-counter">4</div>
                  <div class="step-name d-none d-sm-block">História</div>
                </div>
              </div>

              <form @submit.prevent="avancarOuSalvar">
                
                <div v-if="etapaAtual === 1" class="step-content-fade">
                  <h5 class="text-dark font-weight-bold mb-3 section-step-title">1. Identificação Básica</h5>
                  
                  <div class="row">
                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Nome do Animal *</label>
                      <input type="text" v-model="animal.nome" required placeholder="Ex: Duque" class="form-control custom-form-input">
                    </div>

                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Espécie *</label>
                      <select v-model="animal.especie" required class="form-control custom-select-form">
                        <option value="" disabled>Selecione a Espécie</option>
                        <option value="CACHORRO">Cachorro</option>
                        <option value="GATO">Gato</option>
                        <option value="OUTRO">Outro</option>
                      </select>
                    </div>

                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Raça *</label>
                      <input type="text" v-model="animal.raca" required placeholder="Ex: Pastor Alemão" class="form-control custom-form-input">
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-12 col-md-3 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Situação / Status *</label>
                      <select v-model="animal.statusId" required class="form-control custom-select-form">
                        <option :value="null" disabled>Selecione um Status</option>
                        <option v-for="status in listaStatus" :key="status.id || status.stanId" :value="status.id || status.stanId">
                          {{ (status.descricao || status.nome) | removerUnderline | capitalizar }}
                        </option>
                      </select>
                    </div>

                    <div class="col-12 col-md-3 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Porte *</label>
                      <select v-model="animal.porte" required class="form-control custom-select-form">
                        <option value="" disabled>Selecione o Porte</option>
                        <option value="PEQUENO">Pequeno</option>
                        <option value="MEDIO">Médio</option>
                        <option value="GRANDE">Grande</option>
                      </select>
                    </div>

                    <div class="col-12 col-md-3 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Sexo *</label>
                      <select v-model="animal.sexo" required class="form-control custom-select-form">
                        <option value="" disabled>Selecione o Sexo</option>
                        <option value="MACHO">Macho</option>
                        <option value="FEMEA">Fêmea</option>
                      </select>
                    </div>

                    <div class="col-12 col-md-3 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Idade Estimada *</label>
                      <input type="text" v-model="animal.idadeEstimada" required placeholder="Ex: FILHOTE, ADULTO, IDOSO ou 2 anos" class="form-control custom-form-input">
                    </div>
                  </div>
                </div>

                <div v-if="etapaAtual === 2" class="step-content-fade">
                  <h5 class="text-dark font-weight-bold mb-3 section-step-title">2. Prontuário & Saúde</h5>
                  
                  <div class="row">
                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Condição de Entrada</label>
                      <input type="text" v-model="animal.condicaoEntrada" placeholder="Ex: Saudável, Debilitado" class="form-control custom-form-input">
                    </div>

                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Peso na Entrada (kg)</label>
                      <input type="number" step="0.01" min="0" v-model.number="animal.pesoEntrada" placeholder="Ex: 28.5" class="form-control custom-form-input">
                    </div>

                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Número do Microchip</label>
                      <input type="text" v-model="animal.microchip" placeholder="Se possuir, digite aqui" class="form-control custom-form-input">
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Cor da Pelagem</label>
                      <input type="text" v-model="animal.pelagemCor" placeholder="Ex: Preto e Marrom, Branco" class="form-control custom-form-input">
                    </div>

                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Tipo de Pelagem</label>
                      <input type="text" v-model="animal.pelagemTipo" placeholder="Ex: Curto, Longo, Duro" class="form-control custom-form-input">
                    </div>

                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Marcas ou Cicatrizes</label>
                      <input type="text" v-model="animal.marcasCicatrizes" placeholder="Ex: Cicatriz na orelha esquerda" class="form-control custom-form-input">
                    </div>
                  </div>

                  <div class="row align-items-center bg-light p-3 rounded mb-3 mx-0 border">
                    <div class="col-12 col-md-3 d-flex align-items-center mb-2 mb-md-0">
                      <div class="custom-control custom-checkbox">
                        <input type="checkbox" id="formCastrado" v-model="animal.castrado" class="custom-control-input" @change="aoMudarCastrado">
                        <label class="custom-control-label font-weight-bold text-dark mb-0" for="formCastrado" style="cursor: pointer;">
                          O animal é castrado?
                        </label>
                      </div>
                    </div>

                    <div class="col-12 col-md-9 p-0 m-0">
                      <div v-if="animal.castrado" class="row m-0 p-0 w-100">
                        <div class="col-12 col-md-6 mb-2 mb-md-0">
                          <label class="control-label font-weight-bold mb-1 shadow-label" style="font-size: 11px;">Data da Castração</label>
                          <input type="date" v-model="animal.dataCastracao" class="form-control custom-form-input" :disabled="animal.dataCastracaoDesconhecida">
                        </div>

                        <div class="col-12 col-md-6 d-flex align-items-center" style="height: 40px; margin-top: 20px;">
                          <div class="custom-control custom-checkbox">
                            <input type="checkbox" id="formDataDesconhecida" v-model="animal.dataCastracaoDesconhecida" class="custom-control-input" @change="aoMudarDataDesconhecida">
                            <label class="custom-control-label font-weight-bold text-secondary mb-0" for="formDataDesconhecida" style="cursor: pointer; font-size: 13px;">
                              Não sei a data exata de castração
                            </label>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <div v-if="etapaAtual === 3" class="step-content-fade">
                  <h5 class="text-dark font-weight-bold mb-3 section-step-title">3. Dados do Resgate</h5>
                  
                  <div class="row">
                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Data do Resgate *</label>
                      <input type="date" v-model="uiResgate.data" required class="form-control custom-form-input">
                    </div>

                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Hora aproximada</label>
                      <input type="time" step="1" v-model="uiResgate.hora" class="form-control custom-form-input">
                    </div>

                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Origem do Resgate</label>
                      <input type="text" v-model="animal.origem" placeholder="Ex: RUA, CASUÍSTICA, MAUS_TRATOS" class="form-control custom-form-input">
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Bairro</label>
                      <input type="text" v-model="animal.localResgateBairro" placeholder="Ex: Jardim Europa" class="form-control custom-form-input">
                    </div>

                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Rua</label>
                      <input type="text" v-model="animal.localResgateRua" placeholder="Ex: Rua dos Pinheiros" class="form-control custom-form-input">
                    </div>

                    <div class="col-12 col-md-4 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Referência do Local</label>
                      <input type="text" v-model="animal.localResgateReferencia" placeholder="Ex: Perto da escola estadual" class="form-control custom-form-input">
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-12 col-md-6 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Nome do Resgatador / Protetor</label>
                      <input type="text" v-model="animal.resgatadorNome" placeholder="Ex: Carlos Silva" class="form-control custom-form-input">
                    </div>

                    <div class="col-12 col-md-6 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Contato do Resgatador</label>
                      <input type="text" v-model="animal.resgatadorContato" placeholder="Ex: (11) 98765-4321" class="form-control custom-form-input">
                    </div>
                  </div>
                </div>

                <div v-if="etapaAtual === 4" class="step-content-fade">
                  <h5 class="text-dark font-weight-bold mb-3 section-step-title">4. História do Animal</h5>
                  
                  <div class="row mb-3">
                    <div class="col-12 d-flex align-items-center">
                      <div class="custom-control custom-checkbox bg-light p-3 rounded border w-100 pl-5">
                        <input type="checkbox" id="formPossivelAdocao" v-model="animal.possivelAdocao" class="custom-control-input">
                        <label class="custom-control-label font-weight-bold text-dark mb-0" for="formPossivelAdocao" style="cursor: pointer;">
                          Este animal já está pronto / disponível para adoção?
                        </label>
                      </div>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-12 mb-3">
                      <label class="control-label font-weight-bold mb-1 shadow-label">Biografia / Histórico de Vida</label>
                      <textarea v-model="animal.historia" rows="5" placeholder="Conte um pouco sobre o temperamento dele e detalhes do recolhimento..." class="form-control custom-textarea-form"></textarea>
                    </div>
                  </div>
                </div>

                <div class="row mt-4">
                  <div class="col-12 d-flex justify-content-between align-items-center">
                    <button type="button" v-if="etapaAtual > 1" @click="voltarEtapa" class="btn btn-light px-4 font-weight-bold text-muted border style-action-btn">
                      <i class="fa fa-angle-left mr-1"></i> Voltar Anterior
                    </button>
                    <div v-else></div>

                    <button type="submit" class="btn btn-info btn-fill px-5 font-weight-bold text-white shadow-sm style-action-btn" style="background-color: #23ccef; border: none;">
                      <span v-if="etapaAtual < 4">Próxima Etapa <i class="fa fa-angle-right ml-1"></i></span>
                      <span v-else><i class="fa fa-save mr-1"></i> Salvar Ficha Final</span>
                    </button>
                  </div>
                </div>

              </form>
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
  name: 'AnimalForm',
  components: { Card },
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
      etapaAtual: 1,
      loading: false,
      isEdit: false,
      listaStatus: [],
      // Objetos locais para a UI manipular data e hora de forma isolada
      uiResgate: {
        data: new Date().toISOString().split('T')[0], // Começa com a data de hoje padrão
        hora: "00:00:00"
      },
      animal: {
        id: null,
        nome: '',
        especie: '',
        raca: '',
        statusId: null,
        porte: '',
        sexo: '',
        idadeEstimada: '',
        condicaoEntrada: '',
        pesoEntrada: null,
        microchip: '',
        pelagemCor: '',
        pelagemTipo: '',
        marcasCicatrizes: '',
        castrado: false,
        dataCastracao: null,
        dataCastracaoDesconhecida: false,
        origem: '',
        localResgateBairro: '',
        localResgateRua: '',
        localResgateReferencia: '',
        resgatadorNome: '',
        resgatadorContato: '',
        possivelAdocao: true,
        historia: '',
        fotosGaleria: []
      }
    }
  },
  methods: {
    avancarOuSalvar() {
      if (this.etapaAtual < 4) {
        if (this.etapaAtual === 1) {
          if (!this.animal.nome.trim() || !this.animal.especie || !this.animal.raca.trim() || !this.animal.sexo || !this.animal.statusId) {
            alert("Por favor, preencha todos os campos obrigatórios da Identificação!");
            return;
          }
        }
        this.etapaAtual++;
        window.scrollTo(0, 0);
      } else {
        this.salvarAnimalFinal();
      }
    },
    voltarEtapa() {
      if (this.etapaAtual > 1) {
        this.etapaAtual--;
        window.scrollTo(0, 0);
      }
    },
    async carregarCombosForm() {
      try {
        const [resStatus] = await axios.all([
          axios.get('/api/animais/status')
        ]);
        this.listaStatus = resStatus.data || [];
      } catch (error) {
        console.error("Erro ao carregar os seletores:", error);
      }
    },
    async buscarAnimal(id) {
      this.loading = true;
      try {
        const response = await axios.get(`/api/animais/${id}`);
        if (response.data) {
          // Faz o bind dos dados recebidos do DTO para a UI
          this.animal = { ...this.animal, ...response.data };
          
          if (response.data.dataResgate) {
            this.uiResgate.data = response.data.dataResgate;
          }
          if (response.data.horaResgate) {
            this.uiResgate.hora = response.data.horaResgate;
          }
          if (this.animal.dataCastracao) {
            this.animal.dataCastracao = this.animal.dataCastracao.split('T')[0];
          }
        }
      } catch (error) {
        console.error("Erro ao resgatar ficha do pet:", error);
        this.$router.push('/admin/animais');
      } finally {
        this.loading = false;
      }
    },
    aoMudarCastrado() {
      if (!this.animal.castrado) {
        this.animal.dataCastracao = null;
        this.animal.dataCastracaoDesconhecida = false;
      }
    },
    aoMudarDataDesconhecida() {
      if (this.animal.dataCastracaoDesconhecida) {
        this.animal.dataCastracao = null;
      }
    },
    async salvarAnimalFinal() {
      if (!this.uiResgate.data) {
        alert("A data de resgate da Etapa 3 é obrigatória!");
        this.etapaAtual = 3;
        return;
      }

      this.loading = true;
      try {
        const payload = { ...this.animal };
        
        // Acopla os campos de data e hora formatados como o DTO exige
        payload.dataResgate = this.uiResgate.data;
        
        // Garante que a hora possua os segundos (HH:mm:ss) exigido pelo Spring
        if (this.uiResgate.hora) {
          payload.horaResgate = this.uiResgate.hora.length === 5 ? `${this.uiResgate.hora}:00` : this.uiResgate.hora;
        } else {
          payload.horaResgate = null;
        }

        // Garante o envio explícito dos booleanos novos mapeados na Entidade/DTO
        payload.castrado = this.animal.castrado === true;
        payload.dataCastracaoDesconhecida = this.animal.dataCastracaoDesconhecida === true;
        payload.possivelAdocao = this.animal.possivelAdocao === true;

        // Limpa strings vazias substituindo por null
        Object.keys(payload).forEach(key => {
          if (typeof payload[key] === 'string' && !payload[key].trim()) {
            payload[key] = null;
          }
        });

        if (this.isEdit) {
          await axios.put(`/api/animais/${this.animal.id}`, payload);
        } else {
          await axios.post('/api/animais', payload);
        }
        
        this.$router.push('/admin/animais');
      } catch (error) {
        // CORRIGIDO: Removido o System.out.println que quebrava o fluxo
        console.error("Erro na comunicação com servidor:", error);
        
        let mensagemErro = "Erro de validação ao salvar a ficha. Verifique as informações.";
        if (error.response && error.response.data && error.response.data.message) {
          mensagemErro = error.response.data.message;
        }
        
        alert(mensagemErro);
      } finally {
        this.loading = false;
      }
    }
  },
  async mounted() {
    await this.carregarCombosForm();
    const id = this.$route.params.id;
    if (id) {
      this.isEdit = true;
      this.buscarAnimal(id);
    }
  }
}
</script>

<style scoped>
/* Mantido as mesmas estilizações do Stepper e transições */
.stepper-wrapper { display: flex; justify-content: space-between; margin-bottom: 2rem; position: relative; }
.stepper-wrapper::before { content: ""; position: absolute; top: 18px; left: 0; width: 100%; height: 3px; background-color: #f3f3f3; z-index: 1; }
.stepper-item { position: relative; display: flex; flex-direction: column; align-items: center; flex: 1; z-index: 2; }
.step-counter { width: 38px; height: 38px; border-radius: 50%; background-color: #f3f3f3; border: 3px solid #f3f3f3; display: flex; justify-content: center; align-items: center; font-weight: bold; color: #999; transition: all 0.3s ease; }
.step-name { font-size: 11px; font-weight: 600; color: #aaa; margin-top: 6px; text-transform: uppercase; letter-spacing: 0.5px; }
.stepper-item.active .step-counter { background-color: #fff; border-color: #23ccef; color: #23ccef; }
.stepper-item.active .step-name { color: #23ccef; font-weight: bold; }
.stepper-item.completed .step-counter { background-color: #23ccef; border-color: #23ccef; color: #fff; }
.step-content-fade { animation: fadeIn 0.4s ease-in-out; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(6px); } to { opacity: 1; transform: translateY(0); } }
.section-step-title { border-bottom: 2px solid #f1f1f1; padding-bottom: 8px; }
.shadow-label { font-size: 12px; color: #555; }
.custom-form-input, .custom-select-form { height: 40px !important; border: 1px solid #E3E3E3 !important; border-radius: 4px !important; font-size: 13.5px !important; color: #444 !important; }
.custom-textarea-form { border: 1px solid #E3E3E3 !important; border-radius: 4px !important; font-size: 13.5px !important; padding: 10px 12px; }
.style-action-btn { height: 42px; border-radius: 4px; }
.custom-checkbox .custom-control-input:checked ~ .custom-control-label::before { background-color: #23ccef !important; border-color: #23ccef !important; }
.gap-2 { gap: 0.5rem; }
</style>