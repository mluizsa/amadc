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
import routes from '../routes/routes';

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
    /**
     * Gerencia o processo de autenticação.
     * 1. Realiza o login.
     * 2. Busca as informações e permissões do usuário logado.
     * 3. Configura os links do menu lateral baseando-se nessas permissões.
     * 4. Redireciona para o Dashboard.
     */
    async handleLogin() {
      try {
        console.log("Iniciando processo de login...");

        // 1. Tenta realizar o login no Spring Boot
        await AuthService.login(this.user);
        console.log("Login realizado com sucesso!");

        // 2. Busca os dados do usuário (ID, Login, Permissões) do endpoint /api/auth/me
        const userData = await AuthService.getMe();
        console.log("Dados do usuário e permissões recebidos:", userData.permissoes);
        console.log("Objeto retornado pelo /me:", userData); // Veja a estrutura real aqui
        const permissoesAtuais = userData.permissions || [] ;
        // 3. Atualiza o estado global do SidebarPlugin com as rotas permitidas
        // Isso fará com que o menu "Voluntários" ou "Animais" apareça automaticamente
        this.$sidebar.setLinksFromRoutes(routes, permissoesAtuais);

        // 4. Navega para a área administrativa
        this.$router.push('/admin/overview');

      } catch (error) {
        console.error("Erro durante o fluxo de login:", error);

        // Tratamento de erro amigável
        const mensagemErro = error.response && error.response.data
          ? error.response.data
          : "Credenciais inválidas ou servidor fora do ar.";

        alert("Erro no acesso: " + mensagemErro);
      }
    }
  }
};
</script>
