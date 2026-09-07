<template>
  <div :class="{'nav-open': $sidebar.showSidebar}">
    <notifications></notifications>
    <router-view></router-view>
  </div>
</template>

<script>
  import routes from './routes/routes';

  export default {
    watch: {
      '$route': 'updateMenu'
    },
    methods: {
      updateMenu() {
        const storedPermissions = localStorage.getItem('user_permissions');
        const userPermissions = storedPermissions ? JSON.parse(storedPermissions) : [];

        // Verifica se o usuário tem privilégio total (ADMIN)
        const isAdmin = userPermissions.includes('ADMIN');

        // Filtra as rotas com base nas permissões reais do usuário
        const filteredRoutes = routes.map(route => {
          if (route.children) {
            const validChildren = route.children.filter(child => {
              // Se a rota não exige permissão, exibe normalmente
              if (!child.meta || !child.meta.permission) return true;

              const requiredPerm = child.meta.permission;

              // Admin vê tudo, senão checa se possui a permissão exata
              return isAdmin || userPermissions.includes(requiredPerm);
            });

            return {
              ...route,
              children: validChildren
            };
          }
          return route;
        });

        // Passa as rotas filtradas e as permissões para o plugin
        this.$sidebar.setLinksFromRoutes(filteredRoutes, userPermissions);
      }
    },
    mounted() {
      this.updateMenu();
    }
  }
</script>