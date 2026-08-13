<template>
  <div class="wrapper">
    <side-bar :sidebar-links="$sidebar.sidebarLinks">
      <mobile-menu slot="content"></mobile-menu>
    </side-bar>

    <div class="main-panel">
      <top-navbar></top-navbar>

      <dashboard-content @click="toggleSidebar">
      </dashboard-content>

      <content-footer></content-footer>
    </div>
  </div>
</template>

<script>
  import TopNavbar from './TopNavbar.vue'
  import ContentFooter from './ContentFooter.vue'
  import DashboardContent from './Content.vue'
  import MobileMenu from './MobileMenu.vue'
  import AuthService from '../services/AuthService'
  import routes from '../routes/routes'

  export default {
    components: {
      TopNavbar,
      ContentFooter,
      DashboardContent,
      MobileMenu
    },
    methods: {
      toggleSidebar () {
        if (this.$sidebar.showSidebar) {
          this.$sidebar.displaySidebar(false)
        }
      },
      async verificarAutenticacaoEMenu() {
        // Se o menu estiver vazio (ex: após um F5), busca os dados do usuário logado
        if (this.$sidebar.sidebarLinks.length === 0) {
          try {
            const userData = await AuthService.getMe();
            this.$sidebar.setLinksFromRoutes(routes, userData.permissoes);
          } catch (error) {
            console.error("Falha ao recuperar sessão:", error);
            this.$router.push('/login');
          }
        }
      }
    },
    mounted() {
      this.verificarAutenticacaoEMenu();
    }
  }
</script>

<style lang="scss">
/* Estilos mantidos conforme o template original */
</style>
