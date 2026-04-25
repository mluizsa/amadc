<template>
  <div class="container-fluid" style="height: 100vh; display: flex; align-items: center; justify-content: center; background-color: #eee;">
    <div class="col-md-4">
      <div class="card">
        <div class="card-header">
          <h4 class="card-title">Login - AMA DC</h4>
        </div>
        <div class="card-body">
          <form @submit.prevent="handleLogin">
            <div class="row">
              <div class="col-md-12">
                <div class="form-group">
                  <label>Usuário</label>
                  <input type="text" 
                         class="form-control" 
                         placeholder="Seu login" 
                         v-model="user.username">
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-12">
                <div class="form-group">
                  <label>Senha</label>
                  <input type="password" 
                         class="form-control" 
                         placeholder="Sua senha" 
                         v-model="user.password">
                </div>
              </div>
            </div>
            <div class="text-center">
              <button type="submit" class="btn btn-info btn-fill btn-wd">
                Entrar
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AuthService from '../services/AuthService';

export default {
  name: 'Login',
  data() {
    return {
      // O objeto user PRECISA estar aqui dentro para ser reativo
      user: {
        username: '',
        password: ''
      }
    };
  },
  methods: {
    handleLogin() {
      console.log("Tentando logar com:", this.user.username); // Para debugar no console
      
      AuthService.login(this.user)
        .then(() => {
          this.$router.push('/admin/overview');
        })
        .catch(error => {
          console.error(error);
          alert("Erro no login: " + (error.response && error.response.data ? error.response.data : "Verifique sua conexão"));
        });
    }
  }
};
</script>