import Sidebar from './SideBar.vue'
import SidebarLink from './SidebarLink.vue'

const SidebarStore = {
  showSidebar: false,
  sidebarLinks: [], // Começa vazio

  // Função para popular o menu baseado nas rotas e permissões
  setLinksFromRoutes (routes, userPermissions = []) {
    const adminRoot = routes.find(r => r.path === '/admin');
    if (!adminRoot) return;

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
        icon: route.meta.icon || 'ti-view-list',
        path: `/admin/${route.path}`,
        isHeader: route.meta && route.meta.isHeader ? true : false,
        headerTitle: route.meta && route.meta.headerTitle ? route.meta.headerTitle : ''
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
