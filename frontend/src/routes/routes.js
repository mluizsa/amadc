import DashboardLayout from '../layout/DashboardLayout.vue'
import NotFound from '../pages/NotFoundPage.vue'
import Login from '../pages/Login.vue'

import Overview from 'src/pages/Overview.vue'
import UserProfile from 'src/pages/UserProfile/UserProfile.vue'

// Importações do Módulo de Voluntários
import VoluntarioList from 'src/pages/Voluntarios/VoluntarioList.vue'
import VoluntarioForm from 'src/pages/Voluntarios/VoluntarioForm.vue'

// Importações do Módulo de Animais
import AnimalList from 'src/pages/Animais/AnimalList.vue'
import AnimalDetails from 'src/pages/Animais/AnimalDetails.vue'
import AnimalForm from 'src/pages/Animais/AnimalForm.vue' // 🌟 CORREÇÃO: Adicionada a importação que faltava!

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
          isHeader: true,        // 🌟 Adicionado para criar a seção inicial
          headerTitle: 'Início'
        }
      },
      /* =========================================================================
         MÓDULO DE ANIMAIS
         ========================================================================= */
      {
        path: 'animais',
        name: 'Animais',
        component: AnimalList,
        meta: {
          icon: 'fa fa-paw',
          permission: 'VOLUNTARIO_ANIMAIS', // 🌟 Atualizado para sua nova permissão
          hidden: false,
          isHeader: true,        // Divisor do menu
          headerTitle: 'Animais'
        }
      },
      {
        path: 'animais/novo',
        name: 'Novo Animal',
        component: AnimalForm,
        meta: { 
          permission: 'VOLUNTARIO_ANIMAIS', 
          hidden: true 
        }
      },
      {
        path: 'animais/editar/:id',
        name: 'Editar Animal',
        component: AnimalForm,
        meta: {
          permission: 'VOLUNTARIO_ANIMAIS', 
          hidden: true 
        }
      },
      {
        path: 'animais/detalhes/:id',
        name: 'Detalhes do Animal',
        component: AnimalDetails,
        meta: { 
          permission: 'VOLUNTARIO_ANIMAIS',
          hidden: true
        }
      },
      /* =========================================================================
         MÓDULO FINANCEIRO (Exemplo de expansão futura)
         ========================================================================= */
      {
        path: 'financeiro',
        name: 'Caixa / Custos',
        component: Overview, // Temporário, aponte para seu futuro componente financeiro
        meta: {
          icon: 'nc-icon nc-money-coins',
          permission: 'VOLUNTARIO_FINANCEIRO', // 🌟 Sua nova permissão financeira
          hidden: false,
          isHeader: true,        // Divisor do menu
          headerTitle: 'Gestão Financeira'
        }
      },
      /* =========================================================================
         MÓDULO DE VOLUNTÁRIOS (RH | GESTÃO)
         ========================================================================= */
      {
        path: 'voluntarios',
        name: 'Voluntários',
        component: VoluntarioList,
        meta: {
          icon: 'nc-icon nc-badge',
          permission: 'VOLUNTARIO_READ',
          hidden: false,
          isHeader: true,        // Divisor do menu
          headerTitle: 'RH | Gestão'
        }
      },
      {
        path: 'voluntarios/novo',
        name: 'Novo Voluntário',
        component: VoluntarioForm,
        meta: { permission: 'VOLUNTARIO_WRITE', hidden: true }
      },
      {
        path: 'voluntarios/editar/:id',
        name: 'Editar Voluntário',
        component: VoluntarioForm,
        meta: { permission: 'VOLUNTARIO_WRITE', hidden: true }
      },
      /* =========================================================================
         PERFIL DO USUÁRIO
         ========================================================================= */
      {
        path: 'user',
        name: 'Meu Perfil',
        component: UserProfile,
        meta: { 
          icon: 'fa fa-user',
          hidden: false,
          isHeader: true,        // Divisor do menu
          headerTitle: 'Configurações'
        }
      }
    ]
  },
  { path: '*', component: NotFound }
]

export default routes