import Sidebar from './SideBar.vue'
import SidebarLink from './SidebarLink.vue'

const SidebarStore = {
  showSidebar: false,
  sidebarLinks: [], // Começa vazio

  // Função para popular o menu baseado nas rotas e permissões
  setLinksFromRoutes (routes, userPermissions) {
    const adminRoot = routes.find(r => r.path === '/admin');
    if (!adminRoot) return;

    this.sidebarLinks = adminRoot.children
      .filter(route => {
        // Se a rota não tem permissão exigida, qualquer um vê
        if (!route.meta || !route.meta.permission) return true;

        // Verifica se o usuário tem a permissão ou é ADMIN
        return userPermissions.includes(route.meta.permission) ||
          userPermissions.includes('ADMIN');
      })
      .map(route => ({
        name: route.name,
        icon: route.meta.icon || 'ti-view-list',
        path: `/admin/${route.path}`
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
