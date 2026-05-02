<template>
  <div class="login-page">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-5 col-sm-8">
          <div class="card login-card shadow-lg">
            <div class="card-header text-center">
              <div class="logo-container mb-3">
                <img src="../assets/img/logo.jpg" style="width: 120px;" alt="Logo AMA DC">
              </div>
              <h3 class="card-title mt-0">AMA DC</h3>
              <p class="card-category">Gestão de Resgates e Adoções</p>
            </div>
            <hr>
            <div class="card-body">
              <form @submit.prevent="handleLogin">
                <div class="form-group mb-3">
                  <label class="text-uppercase small font-weight-bold">Usuário</label>
                  <div class="input-group">
                    <input type="text" 
                           class="form-control" 
                           placeholder="Ex: administrador" 
                           v-model="user.username"
                           required>
                  </div>
                </div>
                
                <div class="form-group mb-4">
                  <label class="text-uppercase small font-weight-bold">Senha</label>
                  <input type="password" 
                         class="form-control" 
                         placeholder="••••••••" 
                         v-model="user.password"
                         required>
                </div>

                <div class="text-center">
                  <button type="submit" class="btn btn-info btn-fill btn-block btn-lg">
                    ACESSAR SISTEMA
                  </button>
                </div>
              </form>
            </div>
            <div class="card-footer text-center pb-4">
              <small class="text-muted">&copy; 2026 Associação Amigos dos Animais</small>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  /* Gradiente moderno que combina com o template Light Bootstrap */
  background: linear-gradient(135deg, #1DC7EA 0%, #4091ff 100%);
  height: 100vh;
  display: flex;
  align-items: center;
}

.login-card {
  border: none;
  border-radius: 12px;
  overflow: hidden;
  padding: 20px;
}

.card-title {
  font-weight: 700;
  color: #333;
  letter-spacing: 1px;
}

.logo-icon {
  font-size: 48px;
  color: #1DC7EA;
}

.form-control {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  transition: all 0.3s;
}

.form-control:focus {
  background-color: #fff;
  border-color: #1DC7EA;
  box-shadow: 0 0 8px rgba(29, 199, 234, 0.2);
}

.btn-fill {
  font-weight: 600;
  letter-spacing: 0.5px;
  transition: transform 0.2s;
}

.btn-fill:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.card-category {
  color: #888;
  font-size: 0.9rem;
}
</style>

<script>
import AuthService from '../services/AuthService';

export default {
  name: 'Login',
  data() {
    return {
      user: {
        username: '',
        password: ''
      }
    };
  },
  methods: {
    handleLogin() {
    console.log("Iniciando processo de login...");
  
  AuthService.login(this.user)
    .then((response) => {
      console.log("Login bem-sucedido no servidor!");
      
      this.$router.push('/admin/overview');
    })
    .catch(error => {
      console.error("Erro real no login:", error);
      
      if (error.message && error.message.includes('undefined')) return;

      const msg = error.response && error.response.data ? error.response.data : "Credenciais inválidas";
      alert("Erro no login: " + msg);
    });
  }
  }
};
</script>