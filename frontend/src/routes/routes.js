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
        name: 'Overview',
        component: Overview
      },
      {
        path: 'user',
        name: 'Usuário',
        component: UserProfile
      },
      {
        path: 'animais',
        name: 'Animais',
        component: TableList
      },
      {
        path: 'notifications',
        name: 'Notificações',
        component: Notifications
      }
    ]
  },
  { path: '*', component: NotFound }
]

export default routes
