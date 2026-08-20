<template>
  <card>
    <h4 slot="header" class="card-title">Meu Perfil</h4>
    <form>
      <div class="row">
        <div class="col-md-6">
          <base-input type="text"
                    label="Nome Completo"
                    placeholder="Seu nome"
                    v-model="user.nome">
          </base-input>
        </div>
        <div class="col-md-6">
          <base-input type="text"
                    label="Nome de Usuário (Username)"
                    :disabled="true"
                    placeholder="Username"
                    v-model="user.username">
          </base-input>
        </div>
      </div>

      <div class="row">
        <div class="col-md-6">
          <base-input type="email"
                    label="E-mail"
                    placeholder="E-mail"
                    v-model="user.email">
          </base-input>
        </div>
        <div class="col-md-6">
          <base-input type="text"
                    label="Telefone"
                    placeholder="Telefone"
                    v-model="user.telefone">
          </base-input>
        </div>
      </div>

      <div class="row">
        <div class="col-md-6">
          <base-input type="text"
                    label="CPF"
                    :disabled="true"
                    placeholder="CPF"
                    v-model="user.cpf">
          </base-input>
        </div>
        <div class="col-md-6">
          <base-input type="text"
                    label="Ocupação"
                    placeholder="Sua ocupação"
                    v-model="user.ocupacao">
          </base-input>
        </div>
      </div>

      <!-- Nova linha adicionada para a Data de Nascimento -->
      <div class="row">
        <div class="col-md-6">
          <base-input type="date"
                    label="Data de Nascimento"
                    v-model="user.dataNascimento">
          </base-input>
        </div>
      </div>

      <div class="row">
        <div class="col-md-12">
          <div class="form-group">
            <label>Observações / Sobre mim</label>
            <textarea rows="5" class="form-control border-input"
                      placeholder="Informações adicionais..."
                      v-model="user.observacoes">
            </textarea>
          </div>
        </div>
      </div>

      <div class="text-center">
        <button type="submit" class="btn btn-info btn-fill float-right" @click.prevent="updateProfile">
          Salvar Alterações
        </button>
      </div>
      <div class="clearfix"></div>
    </form>
  </card>
</template>

<script>
  import Card from 'src/components/Cards/Card.vue'
  import AuthService from '../../services/AuthService'
  import axios from 'axios'

  export default {
    components: {
      Card
    },
    data () {
      return {
        user: {
          username: '',
          email: '',
          nome: '',
          cpf: '',
          telefone: '',
          ocupacao: '',
          dataNascimento: '',
          observacoes: ''
        }
      }
    },
    mounted () {
      this.carregarDadosUsuario()
    },
    methods: {
      async carregarDadosUsuario () {
        try {
          const dados = await AuthService.getMe()
          this.user.username = dados.username || ''
          this.user.email = dados.email || ''
          this.user.nome = dados.nome || dados.nomeCompleto || ''
          this.user.cpf = dados.cpf || ''
          this.user.telefone = dados.telefone || ''
          this.user.ocupacao = dados.ocupacao || ''
          this.user.dataNascimento = dados.dataNascimento || ''
          this.user.observacoes = dados.observacoes || ''
        } catch (error) {
          console.error("Erro ao carregar dados do perfil:", error)
        }
      },
      async updateProfile () {
        try {
          await axios.put('/api/voluntarios/me', this.user)
          
          this.$notify({
            message: 'Perfil atualizado com sucesso!',
            type: 'success',
            verticalAlign: 'top',
            horizontalAlign: 'right'
          })

          // Aguarda um breve instante para o usuário ver o toast e recarrega a página
          setTimeout(() => {
            window.location.reload()
          }, 1000)
        } catch (error) {
          console.error("Erro ao atualizar perfil:", error)
          
          this.$notify({
            message: 'Não foi possível atualizar o perfil.',
            type: 'danger',
            verticalAlign: 'top',
            horizontalAlign: 'right'
          })
        }
      }
    }
  }
</script>

<style>
</style>