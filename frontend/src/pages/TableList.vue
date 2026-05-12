<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <card class="strpied-tabled-with-hover">
            <template slot="header">
              <h4 class="card-title">Gestão de {{ $route.name }}</h4>
              <p class="card-category">Listagem completa do sistema AMADC</p>
            </template>

            <div v-if="loading" class="text-center p-4">Carregando...</div>

            <l-table v-else
                     class="table-hover table-striped"
                     :columns="tableData.columns"
                     :data="tableData.data">
            </l-table>
          </card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import LTable from 'src/components/Table.vue'
  import Card from 'src/components/Cards/Card.vue'
  import axios from 'axios' // Ou seu service de API

  export default {
    components: { LTable, Card },
    data () {
      return {
        loading: false,
        tableData: {
          columns: [],
          data: []
        }
      }
    },
    watch: {
      // Se mudar de 'Voluntários' para 'Animais', recarrega os dados
      '$route': 'fetchData'
    },
    methods: {
      async fetchData() {
        this.loading = true;
        try {
          // Ex: se a rota é /admin/voluntarios, o endpoint será /api/voluntarios
          const endpoint = this.$route.path.replace('/admin/', '');
          const response = await axios.get(`/api/${endpoint}`);

          this.tableData.data = response.data;

          // Define as colunas dinamicamente baseado no primeiro item do retorno
          if (response.data.length > 0) {
            this.tableData.columns = Object.keys(response.data[0])
              .map(key => key.charAt(0).toUpperCase() + key.slice(1));
          }
        } catch (error) {
          console.error("Erro ao carregar dados", error);
        } finally {
          this.loading = false;
        }
      }
    },
    mounted() {
      this.fetchData();
    }
  }
</script>
