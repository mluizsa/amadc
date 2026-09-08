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
              {{ formatarStatusTexto(animal.statusDescricao) }}
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

      <div v-if="loading" class="text-center p-5 bg-white rounded shadow-sm">
        <div class="spinner-border text-info my-3" role="status"></div>
        <p class="text-muted font-weight-600">Buscando o prontuário completo do animal...</p>
      </div>

      <div v-else class="row">

        <!-- Coluna Esquerda: Perfil do Animal -->
        <div class="col-12 col-lg-4 mb-4">
          <div class="card card-user border-0 shadow-sm bg-white" style="border-radius: 8px; overflow: hidden;">
            <div class="image-cover-header position-relative">
              <div class="overlay-gradient-pet"></div>
            </div>

            <div class="content text-center position-relative px-3" style="margin-top: -65px; padding-bottom: 25px;">
              <div class="author">
                <a href="#">
                  <img v-if="animal.urlFotoCapa" class="avatar border-gray" :src="formatarUrlImagem(animal.urlFotoCapa)" :alt="animal.nome">
                  <img v-else class="avatar border-gray" :src="formatarUrlImagem('/uploads/img/placeholder-animal.png')" alt="Animal Placeholder">
                </a>
                <h4 class="title">{{ animal.nome }}</h4>
                <p class="text-muted small font-weight-600 mb-3" style="letter-spacing: 0.3px;">
                  RACA: {{ animal.raca || 'N/I' }} | PORTE: {{ animal.porte || 'N/I' }}
                </p>
              </div>

              <hr class="my-3" style="border-top: 1px solid #f1f5f9;">

              <div class="row text-left px-2">
                <div class="col-6 mb-2">
                  <small class="text-uppercase text-secondary font-weight-bold block-label">Espécie</small>
                  <p class="m-0 text-dark font-weight-600 font-size-14">{{ animal.especie || 'Não informada' }}</p>
                </div>
                <div class="col-6 mb-2">
                  <small class="text-uppercase text-secondary font-weight-bold block-label">Sexo</small>
                  <p class="m-0 text-dark font-weight-600 font-size-14">{{ animal.sexo || 'Não informado' }}</p>
                </div>
                <div class="col-6 mb-2">
                  <small class="text-uppercase text-secondary font-weight-bold block-label">Idade Estimada</small>
                  <p class="m-0 text-dark font-weight-600 font-size-14">{{ animal.idadeEstimada || 'Não cadastrada' }}</p>
                </div>
                <div class="col-6 mb-2">
                  <small class="text-uppercase text-secondary font-weight-bold block-label">Microchip</small>
                  <p class="m-0 text-dark font-weight-600 font-size-14 text-truncate" :title="animal.microchip">{{ animal.microchip || 'Sem chip' }}</p>
                </div>
              </div>

              <div class="bg-light rounded p-2 mt-2 text-left border">
                <small class="text-uppercase text-secondary font-weight-bold block-label px-1">Pelagem</small>
                <p class="m-0 text-dark font-size-13 px-1 font-weight-500">
                  {{ animal.pelagemCor || 'Cor não definida' }} ({{ animal.pelagemTipo || 'Tipo não definido' }})
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- Coluna Direita: Abas de Conteúdo -->
        <div class="col-12 col-lg-8 mb-4">
          <div class="card border-0 shadow-sm bg-white" style="border-radius: 8px; min-height: 485px;">
            <div class="card-header bg-white pb-0 border-0 pt-3 px-3">
              <ul class="nav nav-tabs border-bottom-0 gap-1" role="tablist">
                <li class="nav-item">
                  <a class="nav-link" :class="{ 'active': activeTab === 'historico' }" @click="activeTab = 'historico'">
                    <i class="fa fa-history mr-1"></i> Histórico
                  </a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" :class="{ 'active': activeTab === 'clinica' }" @click="activeTab = 'clinica'">
                    <i class="fa fa-file-text-o mr-1"></i> Ficha Clínica
                  </a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" :class="{ 'active': activeTab === 'atendimentos' }" @click="activeTab = 'atendimentos'">
                    <i class="fa fa-stethoscope mr-1"></i> Atendimentos
                  </a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" :class="{ 'active': activeTab === 'vacinas' }" @click="activeTab = 'vacinas'">
                    <i class="fa fa-shield mr-1"></i> Vacinas
                  </a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" :class="{ 'active': activeTab === 'medicamentos' }" @click="activeTab = 'medicamentos'">
                    <i class="fa fa-flask mr-1"></i> Medicamentos
                  </a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" :class="{ 'active': activeTab === 'galeria' }" @click="activeTab = 'galeria'">
                    <i class="fa fa-camera mr-1"></i> Galeria de Fotos
                  </a>
                </li>
              </ul>
            </div>

            <div class="card-body p-4 tab-content">

              <!-- Aba Histórico -->
              <div v-if="activeTab === 'historico'" class="tab-pane-fade">
                <h5 class="text-dark font-weight-bold mb-3 border-bottom pb-2">Biografia & Origem</h5>
                <p class="text-dark font-weight-500 bg-light p-3 rounded border text-justify style-biografia-text" v-if="animal.historia">
                  "{{ animal.historia }}"
                </p>
                <p class="text-muted italic bg-light p-3 rounded border text-center mb-4" v-else>
                  Nenhuma biografia ou detalhe comportamental preenchido para este animal.
                </p>

                <h5 class="text-dark font-weight-bold mb-3 border-bottom pb-2 mt-4">Dados Técnicos do Resgate</h5>
                <div class="row">
                  <div class="col-12 col-md-6 mb-3">
                    <small class="text-muted font-weight-bold d-block mb-1">DATA E HORA DO REGISTRO</small>
                    <p class="text-dark font-weight-600 m-0 bg-light border p-2 rounded">
                      <i class="fa fa-calendar text-info mr-2"></i>{{ formatarData(animal.dataResgate) }} às {{ animal.horaResgate || 'Horário N/I' }}
                    </p>
                  </div>
                  <div class="col-12 col-md-6 mb-3">
                    <small class="text-muted font-weight-bold d-block mb-1">ORIGEM RECOLHIMENTO</small>
                    <p class="text-dark font-weight-600 m-0 bg-light border p-2 rounded">
                      <i class="fa fa-map-signs text-info mr-2"></i>{{ animal.origem || 'Não especificada' }}
                    </p>
                  </div>
                  <div class="col-12 mb-3">
                    <small class="text-muted font-weight-bold d-block mb-1">LOCAL EXATO DA OCORRÊNCIA</small>
                    <div class="bg-light border p-2 rounded text-dark font-weight-600">
                      <i class="fa fa-map-marker text-danger mr-2"></i>
                      <span>Rua: {{ animal.localResgateRua || 'N/I' }}, Bairro: {{ animal.localResgateBairro || 'N/I' }}</span>
                      <div class="small text-muted pl-4 mt-1" v-if="animal.localResgateReferencia">
                        <strong>Ref:</strong> {{ animal.localResgateReferencia }}
                      </div>
                    </div>
                  </div>
                  <div class="col-12 col-md-6 mb-2">
                    <small class="text-muted font-weight-bold d-block mb-1">PROTETOR / RESGATADOR RESPONSÁVEL</small>
                    <p class="text-dark font-weight-600 m-0 bg-light border p-2 rounded">
                      <i class="fa fa-user-circle-o text-secondary mr-2"></i>{{ animal.resgatadorNome || 'Não registrado' }}
                    </p>
                  </div>
                  <div class="col-12 col-md-6 mb-2">
                    <small class="text-muted font-weight-bold d-block mb-1">CONTATO DO RESGATADOR</small>
                    <p class="text-dark font-weight-600 m-0 bg-light border p-2 rounded">
                      <i class="fa fa-phone text-secondary mr-2"></i>{{ animal.resgatadorContato || 'Sem contato' }}
                    </p>
                  </div>
                </div>
              </div>

              <!-- Aba Ficha Clínica -->
              <div v-if="activeTab === 'clinica'" class="tab-pane-fade">
                <h5 class="text-dark font-weight-bold mb-3 border-bottom pb-2">Status Clínico de Entrada</h5>
                <div class="row">
                  <div class="col-12 col-md-6 mb-3">
                    <small class="text-muted font-weight-bold d-block mb-1">PESO DE ENTRADA</small>
                    <p class="text-dark font-weight-600 m-0 bg-light border p-2 rounded">
                      <i class="fa fa-balance-scale text-info mr-2"></i>{{ animal.pesoEntrada ? `${animal.pesoEntrada} kg` : 'Não pesado' }}
                    </p>
                  </div>
                  <div class="col-12 col-md-6 mb-3">
                    <small class="text-muted font-weight-bold d-block mb-1">CONDIÇÃO GERAL CORPORAL</small>
                    <p class="text-dark font-weight-600 m-0 bg-light border p-2 rounded">
                      <i class="fa fa-heartbeat text-info mr-2"></i>{{ animal.condicaoEntrada || 'Não avaliada' }}
                    </p>
                  </div>
                </div>

                <h5 class="text-dark font-weight-bold mb-3 border-bottom pb-2 mt-4">Controle reprodutivo</h5>
                <div class="row">
                  <div class="col-12 mb-2">
                    <div class="p-3 rounded border d-flex align-items-center" :class="animal.castrado ? 'bg-success-light border-success-200' : 'bg-warning-light border-warning-200'">
                      <i class="fa fa-2x mr-3" :class="animal.castrado ? 'fa-check-circle text-success' : 'fa-times-circle text-warning'"></i>
                      <div>
                        <h6 class="m-0 font-weight-bold text-dark">
                          {{ animal.castrado ? 'Animal Castrado' : 'Animal Não Castrado / Pendente' }}
                        </h6>
                        <small class="text-secondary" v-if="animal.castrado && animal.dataCastracao">
                          Procedimento realizado em: {{ formatarData(animal.dataCastracao) }}
                        </small>
                        <small class="text-secondary" v-else-if="animal.castrado && animal.dataCastracaoDesconhecida">
                          Castrado antes do acolhimento (Data exata desconhecida)
                        </small>
                        <small class="text-secondary" v-else>
                          Necessário agendamento na clínica parceira da AMA DC.
                        </small>
                      </div>
                    </div>
                  </div>
                </div>

                <h5 class="text-dark font-weight-bold mb-3 border-bottom pb-2 mt-4">Mapeamento Adotivo</h5>
                <div class="row">
                  <div class="col-12">
                    <div class="p-3 rounded border bg-light d-flex align-items-center">
                      <i class="fa fa-home fa-2x mr-3 text-info"></i>
                      <div>
                        <h6 class="m-0 font-weight-bold text-dark">Disponibilidade de Adoção</h6>
                        <p class="m-0 small text-secondary">
                          Status atual: <span class="badge" :class="animal.possivelAdocao ? 'badge-success' : 'badge-secondary'">{{ animal.possivelAdocao ? 'PRONTO PARA ADOÇÃO' : 'RESERVADO / EM TRATAMENTO' }}</span>
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Aba Atendimentos (Integração do Componente Novo) -->
              <div v-if="activeTab === 'atendimentos'" class="tab-pane-fade">
                <AtendimentosTab :animalId="animal.id" v-if="animal.id" />
              </div>

              <!-- Aba Vacinas -->
              <div v-if="activeTab === 'vacinas'" class="tab-pane-fade">
                <div class="d-flex justify-content-between align-items-center mb-3 border-bottom pb-2">
                  <h5 class="text-dark font-weight-bold m-0">Esquema Vacinal</h5>
                  <button class="btn btn-outline-info btn-xs font-weight-bold"><i class="fa fa-plus mr-1"></i> Registrar Vacina</button>
                </div>
                <p class="text-muted text-center p-4 bg-light rounded border small">
                  Nenhum registo de imunização acoplado a este prontuário.
                </p>
              </div>

              <!-- Aba Medicamentos -->
              <div v-if="activeTab === 'medicamentos'" class="tab-pane-fade">
                <div class="d-flex justify-content-between align-items-center mb-3 border-bottom pb-2">
                  <h5 class="text-dark font-weight-bold m-0">Prescrições Ativas & Tratamentos</h5>
                  <button class="btn btn-outline-info btn-xs font-weight-bold"><i class="fa fa-plus mr-1"></i> Nova Prescrição</button>
                </div>
                <p class="text-muted text-center p-4 bg-light rounded border small">
                  Nenhum tratamento clínico em andamento para este pet.
                </p>
              </div>

              <!-- Aba Galeria -->
              <div v-if="activeTab === 'galeria'" class="tab-pane-fade">
                <div class="d-flex justify-content-between align-items-center mb-3 border-bottom pb-2">
                  <div>
                    <h5 class="text-dark font-weight-bold m-0">Linha do Tempo de Mídias</h5>
                    <p class="text-muted small mb-0">Imagens registadas em lote (Mais recentes primeiro). Clique para ampliar.</p>
                  </div>
                  <button @click="$router.push(`/admin/animais/editar/${animal.id}`)" class="btn btn-outline-info btn-sm font-weight-bold shadow-sm">
                    <i class="fa fa-plus mr-1"></i> Adicionar Fotos
                  </button>
                </div>

                <div v-if="animal.fotosGaleria && animal.fotosGaleria.length > 0" class="row row-gap-3">
                  <div
                    v-for="foto in [...animal.fotosGaleria].reverse()"
                    :key="foto.id"
                    class="col-12 col-sm-6 col-md-4 position-relative"
                  >
                    <div class="card m-0 p-1 border shadow-sm rounded-lg overflow-hidden position-relative card-galeria-item" @click="ampliarFoto(foto.url)" style="cursor: pointer;" title="Clique para ampliar">

                      <span
                        v-if="animal.urlFotoCapa === foto.url || foto.isCapa"
                        class="badge badge-success position-absolute badge-capa-indicator shadow-sm"
                      >
                        <i class="fa fa-star text-warning mr-1"></i> Foto de Capa
                      </span>
                      <span
                        v-if="foto.dataCriacao"
                        class="badge badge-dark position-absolute badge-data-indicator shadow-sm"
                      >
                        <i class="fa fa-calendar mr-1"></i> {{ formatarData(foto.dataCriacao) }}
                      </span>

                      <img :src="formatarUrlImagem(foto.url)" class="img-fluid rounded image-galeria-cover" alt="Evolução" crossorigin="anonymous">

                      <div class="galeria-actions-overlay d-flex align-items-center justify-content-center" @click.stop>
                        <button
                          v-if="animal.urlFotoCapa !== foto.url && !foto.isCapa"
                          type="button"
                          @click="definirFotoComoCapa(foto)"
                          class="btn btn-sm btn-info text-white font-weight-bold shadow-sm mr-2"
                          style="background-color: #23ccef; border: none;"
                        >
                          <i class="fa fa-check-circle mr-1"></i> Capa
                        </button>
                        <button
                          type="button"
                          @click="ampliarFoto(foto.url)"
                          class="btn btn-sm btn-secondary text-white font-weight-bold shadow-sm"
                        >
                          <i class="fa fa-search-plus"></i> Zoom
                        </button>
                      </div>

                    </div>
                  </div>
                </div>

                <div v-else class="text-center p-5 bg-light rounded border text-muted">
                  <i class="fa fa-picture-o fa-3x mb-3 text-secondary"></i>
                  <h6 class="font-weight-bold mb-1">Nenhuma foto neste álbum</h6>
                  <p class="mb-0 small">Utilize o botão acima para anexar o primeiro lote de imagens do pet.</p>
                </div>
              </div>

            </div>
          </div>
        </div>

      </div>
    </div>

    <!-- Modal Simples de Zoom/Lightbox para as fotos -->
    <div v-if="modalZoomAtivo" class="modal-zoom-backdrop" @click="fecharZoom">
      <div class="modal-zoom-content position-relative" @click.stop>
        <button type="button" class="close-zoom-btn" @click="fecharZoom">
          <i class="fa fa-times"></i>
        </button>
        <img :src="formatarUrlImagem(fotoZoomUrl)" class="img-fluid rounded shadow-lg" alt="Zoom Foto">
      </div>
    </div>

  </div>
</template>

<script>
import axios from 'axios'
import AtendimentosTab from './AtendimentosTab.vue'

export default {
  name: 'AnimalDetails',
  components: {
    AtendimentosTab
  },
  filters: {
    removerUnderline(valor) {
      if (!valor) return '';
      return valor.toString().replace(/_/g, ' ');
    }
  },
  data() {
    return {
      loading: false,
      activeTab: 'historico',
      modalZoomAtivo: false,
      fotoZoomUrl: '',
      animal: {
        id: null,
        nome: '',
        especie: '',
        raca: '',
        statusId: null,
        statusDescricao: '',
        porte: '',
        sexo: '',
        idadeEstimada: '',
        condicaoEntrada: '',
        pesoEntrada: null,
        microchip: '',
        pelagemCor: '',
        pelagemTipo: '',
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
        urlFotoCapa: null,
        fotosGaleria: []
      }
    }
  },
  methods: {
    async buscarDetalhesAnimal() {
      this.loading = true;
      try {
        const id = this.$route.params.id;
        const response = await axios.get(`/api/animais/${id}`);
        if (response.data) {
          this.animal = {
            ...response.data,
            fotosGaleria: response.data.fotosGaleria || []
          };
        }
      } catch (error) {
        console.error("Erro ao buscar prontuário do animal:", error);
        alert("Não foi possível carregar os detalhes deste animal.");
        this.$router.push('/admin/animais');
      } finally {
        this.loading = false;
      }
    },

    async definirFotoComoCapa(foto) {
      try {
        await axios.patch(`/api/animais/${this.animal.id}/definir-capa`, {
          arquivoId: foto.id
        });

        this.animal.urlFotoCapa = foto.url;

        if (this.animal.fotosGaleria) {
          this.animal.fotosGaleria.forEach(f => {
            f.isCapa = (f.id === foto.id);
          });
        }
      } catch (error) {
        console.error("Erro ao definir foto de capa:", error);
        const msgErro = (error.response && error.response.data && error.response.data.message)
          || "Erro ao salvar alteração da capa no servidor.";
        alert(msgErro);
      }
    },

    ampliarFoto(url) {
      this.fotoZoomUrl = url;
      this.modalZoomAtivo = true;
    },

    fecharZoom() {
      this.modalZoomAtivo = false;
      this.fotoZoomUrl = '';
    },

    formatarUrlImagem(url) {
      if (!url) return '';
      if (url.startsWith('http://') || url.startsWith('https://')) {
        return url;
      }
      const baseUrl = axios.defaults.baseURL || 'http://localhost:8080';
      return `${baseUrl.replace(/\/$/, '')}/${url.replace(/^\//, '')}`;
    },
    formatarStatusTexto(status) {
      if (!status) return '';
      return status.toString().replace(/_/g, ' ');
    },
    formatarData(dataStr) {
      if (!dataStr) return 'Não cadastrada';
      try {
        const dataPura = dataStr.split('T')[0].split(' ')[0];
        const partes = dataPura.split('-');
        
        if (partes.length === 3) {
          return `${partes[2]}/${partes[1]}/${partes[0]}`;
        }
        return dataStr;
      } catch (e) {
        return dataStr;
      }
    },
    statusClass(status) {
      if (!status) return 'badge-secondary';
      switch (status.toUpperCase()) {
        case 'RESGATADO': return 'badge-warning';
        case 'EM_TRATAMENTO':
        case 'QUARENTENA': return 'badge-danger';
        case 'DISPONIVEL_PARA_ADOCAO': return 'badge-success';
        case 'EM_ADAPTACAO': return 'badge-primary';
        case 'ADOTADO': return 'badge-success';
        case 'FALECIDO': return 'badge-dark';
        default: return 'badge-secondary';
      }
    }
  },
  mounted() {
    this.buscarDetalhesAnimal();
  }
}
</script>

<style scoped>
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
  background-color: #f8f9fa;
  border-color: #e3e3e3;
  border-radius: 4px 4px 0 0;
}
.nav-tabs .nav-link.active {
  color: #23ccef !important;
  border-color: #ddd #ddd #fff;
  background-color: #fff;
  border-radius: 4px 4px 0 0;
}

.tab-pane-fade {
  animation: fadeIn 0.3s ease-in-out;
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(4px); }
  to { opacity: 1; transform: translateY(0); }
}

.image-cover-header {
  height: 160px;
  background-color: #f1f5f9;
}
.object-fit-cover {
  object-fit: cover;
}
.overlay-gradient-pet {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  background: linear-gradient(to bottom, rgba(0,0,0,0) 40%, rgba(255,255,255,1) 100%);
}
.block-label {
  font-size: 10px;
  letter-spacing: 0.5px;
}
.font-size-14 { font-size: 14px; }
.font-size-13 { font-size: 13px; }
.font-weight-500 { font-weight: 500; }
.font-weight-600 { font-weight: 600; }
.style-biografia-text {
  font-style: italic;
  line-height: 1.6;
  color: #4a5568 !important;
}

.bg-success-light { background-color: #ecfdf5; }
.border-success-200 { border-color: #a7f3d0; }
.bg-warning-light { background-color: #fffbeb; }
.border-warning-200 { border-color: #fde68a; }
.gap-1 { gap: 0.25rem; }
.row-gap-3 { row-gap: 1.25rem; }

.card-galeria-item {
  height: 170px;
  background-color: #f8f9fa;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}
.image-galeria-cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.badge-capa-indicator {
  top: 8px;
  left: 8px;
  z-index: 10;
  padding: 5px 9px;
  font-size: 10px;
  border-radius: 4px;
}
.galeria-actions-overlay {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  background-color: rgba(0, 0, 0, 0.45);
  opacity: 0;
  transition: opacity 0.25s ease-in-out;
  z-index: 5;
  border-radius: 4px;
}
.card-galeria-item:hover .galeria-actions-overlay {
  opacity: 1;
}
.bg-dark-translucent {
  background-color: rgba(0, 0, 0, 0.6);
}

.modal-zoom-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.85);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  padding: 20px;
}
.modal-zoom-content {
  max-width: 90%;
  max-height: 90vh;
}
.modal-zoom-content img {
  max-height: 85vh;
  object-fit: contain;
  border-radius: 6px;
}
.close-zoom-btn {
  position: absolute;
  top: -40px;
  right: 0;
  background: transparent;
  border: none;
  color: white;
  font-size: 24px;
  cursor: pointer;
}
</style>