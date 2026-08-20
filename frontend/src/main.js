/*!

 =========================================================
 * Vue Light Bootstrap Dashboard - v2.1.0 (Bootstrap 4)
 =========================================================

 * Product Page: http://www.creative-tim.com/product/light-bootstrap-dashboard
 * Copyright 2023 Creative Tim (http://www.creative-tim.com)
 * Licensed under MIT (https://github.com/creativetimofficial/light-bootstrap-dashboard/blob/master/LICENSE.md)

 =========================================================

 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

 */
import Vue from "vue";
import VueRouter from "vue-router";
import App from "./App.vue";
//import axios from 'axios';

// LightBootstrap plugin
import LightBootstrap from "./light-bootstrap-main";

import routes from "./routes/routes";

import "./registerServiceWorker";

// Configuração do plugin
Vue.use(VueRouter);
Vue.use(LightBootstrap);

// Configuração do router
const router = new VueRouter({
  routes,
  linkActiveClass: "nav-item active",
  scrollBehavior: (to) => {
    if (to.hash) {
      return { selector: to.hash };
    } else {
      return { x: 0, y: 0 };
    }
  },
});

// --- TRAVA DE SEGURANÇA (Navigation Guard) ---
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    const userPermissions = JSON.parse(localStorage.getItem('user_permissions') || '[]');
    const requiredPermission = to.meta.permission;

    if (requiredPermission) {
      // Se o usuário é ADMIN ou possui a permissão exigida, libera
      const hasAccess = userPermissions.includes('ADMIN') || userPermissions.includes(requiredPermission);

      if (hasAccess) {
        next();
      } else {
        next('/admin/overview'); // Redireciona se não tiver acesso
      }
    } else {
      next();
    }
  } else {
    next();
  }
});

/*axios.interceptors.request.use(config => {
  const token = localStorage.getItem('user_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => {
  return Promise.reject(error);
});*/

/* eslint-disable no-new */
new Vue({
  el: "#app",
  render: (h) => h(App),
  router,
});
