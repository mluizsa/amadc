import Sidebar from './SideBar.vue'
import SidebarLink from './SidebarLink.vue'

const SidebarStore = {
  showSidebar: false,
  sidebarLinks: [], // Começa vazio

  // Função para popular o menu baseado nas rotas e permissões
  setLinksFromRoutes (routes, userPermissions = []) {
    const adminRoot = routes.find(r => r.path === '/admin');
    if (!adminRoot) return;

    // Garante que é um array para não dar erro no .includes()
    const list = Array.isArray(userPermissions) ? userPermissions : [];

    this.sidebarLinks = adminRoot.children
      .filter(route => {
        if (route.meta && route.meta.hidden) return false;

        if (!route.meta || !route.meta.permission) return true;

        // Verifica a permissão específica ou se o usuário tem a role global ADMIN
        return list.includes(route.meta.permission) || list.includes('ADMIN');
      })
      .map(route => ({
        name: route.name,
        path: `/admin/${route.path}`,
        icon: route.meta && route.meta.icon ? route.meta.icon : '',
        meta: route.meta // Garante o transporte do 'category' e 'hidden' para o componente Sidebar
      }));
  },
  displaySidebar (value) {
    this.showSidebar = value
  }
}

const SidebarPlugin = {

  install (Vue) {
    Vue.mixin({
      data () {
        return {
          sidebarStore: SidebarStore
        }
      }
    })

    Object.defineProperty(Vue.prototype, '$sidebar', {
      get () {
        return this.$root.sidebarStore
      }
    })
    Vue.component('side-bar', Sidebar)
    Vue.component('sidebar-link', SidebarLink)
  }
}

export default SidebarPlugin