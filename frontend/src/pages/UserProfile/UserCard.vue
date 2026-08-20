<template>
  <card class="card-user">
    <img slot="image" src="https://ununsplash.imgix.net/photo-1431578500526-4d9613015464?fit=crop&fm=jpg&h=300&q=75&w=400" alt="..."/>
    <div class="author">
      <a href="#">
        <img class="avatar border-gray" :src="avatarSrc" alt="..."/>
        <h4 class="title">{{ user.nome || 'Carregando...' }}<br />
          <small>{{ user.username }}</small>
        </h4>
      </a>
    </div>
    
    <p class="description text-center">
      {{ user.observacoes || 'Nenhuma observação cadastrada.' }}
    </p>

    <!-- Seção de Data de Nascimento e Aniversário -->
    <div class="text-center mb-3" v-if="user.dataNascimento">
      <hr>
      <span class="text-muted">
        <i class="fa fa-calendar"></i> Nascimento: {{ formatarData(user.dataNascimento) }}
      </span>
      <div v-if="isAniversarioHoje" class="text-danger font-weight-bold mt-1">
        🎉 Hoje é o aniversário dele(a)! Parabéns! 🎂
      </div>
    </div>
  </card>
</template>

<script>
  import Card from 'src/components/Cards/Card.vue'
  import AuthService from '../../services/AuthService'

  export default {
    components: {
      Card
    },
    data () {
      return {
        user: {
          nome: '',
          username: '',
          observacoes: '',
          dataNascimento: ''
        },
        isAniversarioHoje: false
      }
    },
    computed: {
      avatarSrc () {
        if (!this.user.nome) {
          return '/img/faces/face-0.jpg' // Adicionado a barra '/' no início para garantir o caminho absoluto
        }

        const primeiroNome = this.user.nome.trim().split(' ')[0].toLowerCase()
        const excecoesMasculinas = ['lucas', 'nicolas', 'glasglow', 'elias', 'esdras', 'joshua', 'mattia']
        const pareceFeminino = primeiroNome.endsWith('a') && !excecoesMasculinas.includes(primeiroNome)

        if (pareceFeminino) {
          return '/img/faces/face-8.jpg' // Feminino com barra inicial
        }
        
        return '/img/faces/face-0.jpg' // Masculino com barra inicial
      }
    },
    mounted () {
      this.carregarDadosUsuario()
    },
    methods: {
      async carregarDadosUsuario () {
        try {
          const dados = await AuthService.getMe()
          // Aceita tanto 'nome' quanto 'nomeCompleto' vindos do backend
          this.user.nome = dados.nome || dados.nomeCompleto || ''
          this.user.username = dados.username || ''
          this.user.observacoes = dados.observacoes || ''
          this.user.dataNascimento = dados.dataNascimento || ''

          if (this.user.dataNascimento) {
            this.verificarAniversario(this.user.dataNascimento)
          }
        } catch (error) {
          console.error("Erro ao carregar dados do cartão do usuário:", error)
        }
      },
      formatarData (dataStr) {
        if (!dataStr) return ''
        const partes = dataStr.split('-')
        if (partes.length === 3) {
          return `${partes[2]}/${partes[1]}/${partes[0]}`
        }
        return dataStr
      },
      verificarAniversario (dataNascimentoStr) {
        const hoje = new Date()
        const partes = dataNascimentoStr.split('-')
        if (partes.length === 3) {
          const mesNasc = parseInt(partes[1], 10)
          const diaNasc = parseInt(partes[2], 10)

          const mesAtual = hoje.getMonth() + 1
          const diaAtual = hoje.getDate()

          if (mesNasc === mesAtual && diaNasc === diaAtual) {
            this.isAniversarioHoje = true
          }
        }
      }
    }
  }
</script>

<style>
</style>