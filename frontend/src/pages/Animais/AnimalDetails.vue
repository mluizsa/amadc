<template>
  <div class="content">
    <div class="container-fluid">
      
      <div class="row mb-3">
        <div class="col-12 d-flex justify-content-between align-items-center flex-wrap">
          <div>
            <button @click="$router.back()" class="btn btn-outline-secondary btn-sm mb-2 shadow-sm d-inline-flex align-items-center text-secondary font-weight-600 bg-white" style="border-radius: 20px; padding: 6px 14px; transition: all 0.2s ease; border: 1px solid #e3e3e3;">
              <i class="fa fa-arrow-left mr-2" style="font-size: 11px;"></i> Voltar para a Lista
            </button>
            <h3 class="m-0 text-dark font-weight-bold">Ficha de {{ animal.nome || 'Carregando...' }}</h3>
            <p class="text-muted mb-0">Visualização completa do prontuário e histórico de resgate</p>
          </div>
          
          <div class="text-right d-flex align-items-center" v-if="animal.statusId && animal.statusDescricao">
            <span class="badge badge-lg p-3 text-uppercase shadow-sm" 
                  :class="statusClass(animal.statusDescricao)" 
                  style="font-size: 13px; letter-spacing: 0.5px; border-radius: 6px;">
                {{ animal.statusDescricao.replace(/_/g, ' ') }} 
            </span>
          </div>
          <div class="text-right d-flex align-items-center" v-else-if="!loading">
            <span class="badge badge-secondary badge-lg p-3 shadow-sm" 
                  style="font-size: 13px; border-radius: 6px;">
              SEM STATUS
            </span>
          </div>
        </div>
      </div>

      <div v-if="loading" class="text-center p-5">
        <div class="spinner-border text-info" role="status"></div>
        <p class="mt-2 text-muted">Carregando ficha clínica do animal...</p>
      </div>

      <div v-else class="row">
        
        <div class="col-12 col-md-4">
          <card class="card-user text-center shadow-sm">
            <div class="p-4">
              <div class="avatar shadow mb-3 bg-light d-inline-flex align-items-center justify-content-center" style="width: 110px; height: 110px; border-radius: 50%;">
                <i class="fa fa-paw fa-4x text-muted"></i>
              </div>
              <h4 class="title text-dark font-weight-bold mb-1">{{ animal.nome }}</h4>
              <p class="text-muted small mb-3">ID do Registro: #{{ animal.id }}</p>
              
              <ul class="list-group list-group-flush text-left" style="font-size: 13px;">
                <li class="list-group-item d-flex justify-content-between px-1">
                  <span><b>Espécie:</b></span> <span>{{ animal.especie }}</span>
                </li>
                <li class="list-group-item d-flex justify-content-between px-1">
                  <span><b>Raça:</b></span> <span class="text-muted">{{ animal.raca || 'Sem raça definida' }}</span>
                </li>
                <li class="list-group-item d-flex justify-content-between px-1">
                  <span><b>Sexo:</b></span> 
                  <span>
                    <i :class="animal.sexo === 'MACHO' ? 'fa fa-mars text-info' : 'fa fa-venus text-danger'"></i>
                    {{ animal.sexo | capitalizar }}
                  </span>
                </li>
                <li class="list-group-item d-flex justify-content-between px-1">
                  <span><b>Porte:</b></span> <span>{{ animal.porte | capitalizar }}</span>
                </li>
                <li class="list-group-item d-flex justify-content-between px-1">
                  <span><b>Idade Estimada:</b></span> <span>{{ animal.idadeEstimada || 'Não informada' }}</span>
                </li>
              </ul>

              <button @click="$router.push(`/admin/animais/editar/${animal.id}`)" class="btn btn-warning btn-fill btn-block mt-4 shadow">
                <i class="fa fa-edit"></i> Editar Cadastro
              </button>
            </div>
          </card>
        </div>

        <div class="col-12 col-md-8">
          <div class="nav-tabs-navigation">
            <div class="nav-tabs-wrapper">
              <ul class="nav nav-tabs" role="tablist">
                <li class="nav-item">
                  <a class="nav-link" :class="{ active: tabAtiva === 'clinico' }" @click.prevent="tabAtiva = 'clinico'" href="#">
                    <i class="fa fa-heartbeat"></i> Prontuário & Saúde
                  </a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" :class="{ active: tabAtiva === 'resgate' }" @click.prevent="tabAtiva = 'resgate'" href="#">
                    <i class="fa fa-ambulance"></i> Dados do Resgate
                  </a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" :class="{ active: tabAtiva === 'historico' }" @click.prevent="tabAtiva = 'historico'" href="#">
                    <i class="fa fa-history"></i> História do Animal
                  </a>
                </li>
              </ul>
            </div>
          </div>

          <card class="mt-2 border-top-0 shadow-sm" style="border-top-left-radius: 0; border-top-right-radius: 0;">
            
            <div v-if="tabAtiva === 'clinico'" class="tab-pane-content">
              <h5 class="text-info mb-3 font-weight-bold">Acompanhamento Clínico</h5>
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="text-muted small d-block mb-0">CONDIÇÃO DE ENTRADA</label>
                  <span class="text-dark font-weight-bold">{{ animal.condicaoEntrada || 'Não informada' }}</span>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="text-muted small d-block mb-0">PESO NA ENTRADA</label>
                  <span class="text-dark font-weight-bold">{{ animal.pesoEntrada ? `${animal.pesoEntrada} kg` : 'Não registrado' }}</span>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="text-muted small d-block mb-0">NÚMERO DO MICROCHIP</label>
                  <span class="badge badge-secondary shadow-sm" style="font-size: 12px;">{{ animal.microchip || 'Não possui' }}</span>
                </div>
                
                <div class="col-md-6 mb-3">
                  <label class="text-muted small d-block mb-0">PRONTO PARA ADOÇÃO?</label>
                  
                  <span v-if="animal.statusDescricao && ['RESGATADO', 'EM_TRATAMENTO', 'QUARENTENA', 'FALECIDO'].includes(animal.statusDescricao.toUpperCase())" 
                        class="badge badge-danger shadow-sm">
                    Não / Em Triagem ou Tratamento
                  </span>

                  <span v-else-if="animal.statusDescricao && animal.statusDescricao.toUpperCase() === 'ADOTADO'" 
                        class="badge badge-success shadow-sm">
                    Já Adotado!
                  </span>

                  <span v-else class="badge shadow-sm" :class="animal.possivelAdocao ? 'badge-success' : 'badge-danger'">
                    {{ animal.possivelAdocao ? 'Sim, Disponível' : 'Não / Indisponível' }}
                  </span>
                </div>

                <div class="col-12 mb-3">
                  <label class="text-muted small d-block mb-0">PELAGEM (COR E TIPO)</label>
                  <span class="text-dark">{{ animal.pelagemCor || 'Não informada' }} {{ animal.pelagemTipo ? `(${animal.pelagemTipo})` : '' }}</span>
                </div>
                <div class="col-12 mb-3" v-if="animal.marcasCicatrizes">
                  <label class="text-muted small d-block mb-0">MARCAS OU CICATRIZES</label>
                  <span class="text-dark text-justify">{{ animal.marcasCicatrizes }}</span>
                </div>
              </div>
            </div>

            <div v-if="tabAtiva === 'resgate'" class="tab-pane-content">
              <h5 class="text-warning mb-3 font-weight-bold">Informações sobre o Recolhimento</h5>
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="text-muted small d-block mb-0">ORIGEM DO RESGATE</label>
                  <span class="text-dark font-weight-bold">{{ animal.origem || 'Não informada' }}</span>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="text-muted small d-block mb-0">DATA / HORA DO RESGATE</label>
                  <span class="text-dark">{{ animal.dataResgate ? formatarData(animal.dataResgate) : 'Não informada' }} <span v-if="animal.horaResgate">às {{ animal.horaResgate }}</span></span>
                </div>
                <div class="col-12 mb-3">
                  <label class="text-muted small d-block mb-0">ENDEREÇO / LOCAL DE RESGATE</label>
                  <span class="text-dark d-block"><b>Bairro:</b> {{ animal.localResgateBairro || 'Não informado' }}</span>
                  <span class="text-dark d-block" v-if="animal.localResgateRua"><b>Rua:</b> {{ animal.localResgateRua }}</span>
                  <span class="text-muted small d-block" v-if="animal.localResgateReferencia"><b>Referência:</b> {{ animal.localResgateReferencia }}</span>
                </div>
                <div class="col-12"><hr class="my-3"></div>
                <div class="col-md-6 mb-3">
                  <label class="text-muted small d-block mb-0">NOME DO RESGATADOR</label>
                  <span class="text-dark">{{ animal.resgatadorNome || 'Anônimo / Não informado' }}</span>
                </div>
                <div class="col-md-6 mb-3" v-if="animal.resgatadorContato">
                  <label class="text-muted small d-block mb-0">CONTATO DO RESGATADOR</label>
                  <span class="text-dark"><i class="fa fa-phone text-muted mr-1"></i> {{ animal.resgatadorContato }}</span>
                </div>
              </div>
            </div>

            <div v-if="tabAtiva === 'historico'" class="tab-pane-content">
              <h5 class="text-success mb-3 font-weight-bold">Biografia e Histórico de Vida</h5>
              <div class="p-3 bg-light rounded text-justify text-secondary mb-4 shadow-inner" style="font-style: italic; line-height: 1.6;">
                "{{ animal.historia || 'Nenhuma história descrita para este animal.' }}"
              </div>
              
              <h6 class="text-muted font-weight-bold mb-2 small" style="letter-spacing: 0.5px;">LOG DE AUDITORIA INTERNA</h6>
              <div class="p-3 border rounded small bg-white shadow-sm">
                <p class="mb-1 text-dark"><b>Cadastrado por:</b> {{ animal.registradoPor || 'Sistema' }} em {{ formatarData(animal.dataCriacao) }}</p>
                <p class="m-0 text-dark" v-if="animal.alteradoPor"><b>Última modificação por:</b> {{ animal.alteradoPor }} em {{ formatarData(animal.dataAlteracao) }}</p>
              </div>
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
  name: 'AnimalDetails',
  components: { Card },
  filters: {
    capitalizar(valor) {
      if (!valor) return '';
      valor = valor.toString().toLowerCase();
      return valor.charAt(0).toUpperCase() + valor.slice(1);
    }
  },
  data() {
    return {
      loading: false,
      tabAtiva: 'clinico',
      animal: {}
    }
  },
  methods: {
    async buscarDetalhesAnimal() {
      this.loading = true;
      try {
        const idAnimal = this.$route.params.id;
        const response = await axios.get(`/api/animais/${idAnimal}`);
        this.animal = response.data || {};
      } catch (error) {
        console.error("Erro ao buscar detalhes do animal:", error);
      } finally {
        this.loading = false;
      }
    },
    formatarData(dataString) {
      if (!dataString) return '';

      try {
        if (typeof dataString === 'string' && dataString.includes('T')) {
          const apenasData = dataString.split('T')[0];
          const [ano, mes, dia] =  apenasData.split('-');
          return `${dia}/${mes}/${ano}`;
        }

        if (typeof dataString === 'string' && dataString.includes('-')) {
          const [ano, mes, dia] = dataString.split('-');
          return `${dia}/${mes}/${ano}`;
        }

        const data = new Date(dataString);
        return data.toLocaleDateString('pt-BR');
      } catch (error) {
        console.error("Erro ao formatar data:", error);
        return dataString;
      }
    },
    statusClass(status) {
      if (!status) return 'badge-secondary';
      
      const s = status.toUpperCase(); 
      
      switch(s) {
        case 'RESGATADO':
          return 'badge-warning';    
        case 'EM_TRATAMENTO':
        case 'QUARENTENA':
          return 'badge-danger';     
        case 'DISPONIVEL_PARA_ADOCAO':
          return 'badge-success';    
        case 'EM_ADAPTACAO':
          return 'badge-primary';    
        case 'ADOTADO':
          return 'badge-success';    
        case 'FALECIDO':
          return 'badge-dark';       
        default:
          return 'badge-secondary';  
      }
    }
  },
  mounted() {
    this.buscarDetalhesAnimal();
  }
}
</script>

<style scoped>
/* Efeito hover suave no novo botão Voltar */
button.btn-outline-secondary:hover {
  background-color: #f8fafc !important;
  color: #333 !important;
  border-color: #c8c8c8 !important;
  transform: translateY(-1px);
}

.nav-tabs {
  border-bottom: 1px solid #ddd;
}
.nav-tabs .nav-link {
  color: #555;
  cursor: pointer;
  font-weight: 600;
  border: 1px solid transparent;
  padding: 12px 18px;
  transition: all 0.15s ease-in-out;
}
.nav-tabs .nav-link:hover {
  background-color: #f8fafc;
  border-color: #eee #eee #ddd;
}
.nav-tabs .nav-link.active {
  color: #23ccef;
  background-color: #fff;
  border-color: #ddd #ddd #fff;
  border-bottom: 3px solid #23ccef;
}
.tab-pane-content {
  padding: 15px 5px;
}
.list-group-item {
  border-color: #f1f1f1;
}
.italic-text {
  font-style: italic;
  color: #666;
}
.shadow-inner {
  box-shadow: inset 0 2px 4px 0 rgba(0, 0, 0, 0.05);
}
</style>