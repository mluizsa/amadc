<template>
  <div :class="{'nav-open': $sidebar.showSidebar}">
    <notifications></notifications>
    <router-view></router-view>
  </div>
</template>

<script>
  import routes from './routes/routes'; // Importe suas rotas

  export default {
    watch: {
      // Sempre que mudar a rota, garantimos que o menu está sincronizado
      // Útil caso o login aconteça e as permissões mudem
      '$route': 'updateMenu'
    },
    methods: {
      updateMenu() {
        // Aqui você pegaria as permissões do seu Store (Pinia/Vuex) ou do localStorage
        // Por enquanto, vamos passar um array com 'ADMIN' para testar se o menu aparece
        const userPermissions = ['ADMIN', 'VOLUNTARIO_READ'];

        this.$sidebar.setLinksFromRoutes(routes, userPermissions);
      }
    },
    mounted() {
      this.updateMenu();
    }
  }
</script>
<style lang="scss">
  .vue-notifyjs.notifications{
    .list-move {
      transition: transform 0.3s, opacity 0.4s;
    }
    .list-item {
      display: inline-block;
      margin-right: 10px;

    }
    .list-enter-active {
      transition: transform 0.2s ease-in, opacity 0.4s ease-in;
    }
    .list-leave-active {
      transition: transform 1s ease-out, opacity 0.4s ease-out;
    }

    .list-enter {
      opacity: 0;
      transform: scale(1.1);

    }
    .list-leave-to {
      opacity: 0;
      transform: scale(1.2, 0.7);
    }
  }
</style>
