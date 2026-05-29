import DashboardLayout from '../layout/DashboardLayout.vue'
import NotFound from '../pages/NotFoundPage.vue'
import Login from '../pages/Login.vue'

import Overview from 'src/pages/Overview.vue'
import UserProfile from 'src/pages/UserProfile.vue'
import TableList from 'src/pages/TableList.vue'
import Notifications from 'src/pages/Notifications.vue'
import VoluntarioForm from 'src/pages/VoluntarioForm.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/admin',
    component: DashboardLayout,
    redirect: '/admin/overview',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'overview',
        name: 'Painel',
        component: Overview,
        meta: { 
          icon: 'nc-icon nc-chart-pie-35',
          hidden: false
        }
      },
      // --- CATEGORIA: GESTÃO OPERACIONAL ---
      {
        path: 'animais',
        name: 'Animais',
        component: TableList, // Depois mudaremos para o componente específico de Animais
        meta: {
          icon: 'fa fa-paw',
          permission: 'ANIMAL_READ',
          category: 'Gestão Operacional',
          hidden: false
        }
      },
      // --- CATEGORIA: CONFIGURAÇÕES E RH ---
      {
        path: 'voluntarios',
        name: 'Voluntários',
        component: TableList,
        meta: {
          icon: 'nc-icon nc-badge',
          permission: 'VOLUNTARIO_READ',
          category: 'Configurações e RH',
          hidden: false
        }
      },
      // --- ROTAS INTERNAS (ESCONDIDAS DO MENU) ---
      {
        path: 'voluntarios/novo',
        name: 'Novo Voluntário',
        component: VoluntarioForm,
        meta: {
          permission: 'VOLUNTARIO_WRITE',
          hidden: true
        }
      },
      {
        path: 'voluntarios/editar/:id',
        name: 'Editar Voluntário',
        component: VoluntarioForm,
        meta: {
          permission: 'VOLUNTARIO_WRITE',
          hidden: true
        }
      }
    ]
  },
  { path: '*', component: NotFound }
]

export default routes
