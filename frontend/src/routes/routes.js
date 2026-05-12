import DashboardLayout from '../layout/DashboardLayout.vue'
import NotFound from '../pages/NotFoundPage.vue'
import Login from '../pages/Login.vue'

import Overview from 'src/pages/Overview.vue'
import UserProfile from 'src/pages/UserProfile.vue'
import TableList from 'src/pages/TableList.vue'
import Notifications from 'src/pages/Notifications.vue'

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
        name: 'Dashboard',
        component: Overview,
        meta: { icon: 'nc-icon nc-chart-pie-35' }
      },
      {
        path: 'voluntarios',
        name: 'Voluntários',
        component: TableList, // Usaremos a TableList como base
        meta: {
          icon: 'nc-icon nc-address-card',
          permission: 'VOLUNTARIO_READ'
        }
      },
      {
        path: 'animais',
        name: 'Animais',
        component: TableList,
        meta: {
          icon: 'nc-icon nc-prowler',
          permission: 'ANIMAL_READ'
        }
      }
    ]
  },
  { path: '*', component: NotFound }
]

export default routes
