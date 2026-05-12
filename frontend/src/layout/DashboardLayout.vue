<template>
  <div class="wrapper">
   <side-bar>
     <mobile-menu slot="content"></mobile-menu>
     <sidebar-link v-for="(link, index) in $sidebar.sidebarLinks"
                   :key="index"
                   :to="link.path"
                   :link="link">
     </sidebar-link>
   </side-bar>
    <div class="main-panel">
      <top-navbar></top-navbar>

      <dashboard-content @click="toggleSidebar">

      </dashboard-content>

      <content-footer></content-footer>
    </div>
  </div>
</template>
<style lang="scss">

</style>
<script>
  import TopNavbar from './TopNavbar.vue'
  import ContentFooter from './ContentFooter.vue'
  import DashboardContent from './Content.vue'
  import MobileMenu from './MobileMenu.vue'
  import AuthService from '../services/AuthService';
  import routes from '../routes/routes';
  export default {
    components: {
      TopNavbar,
      ContentFooter,
      DashboardContent,
      MobileMenu
    },
    methods: {
      toggleSidebar () {
        if (this.$sidebar.showSidebar) {
          this.$sidebar.displaySidebar(false)
        }
      }
    }
  }


</script>
