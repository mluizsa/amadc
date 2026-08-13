<template>
  <div class="sidebar"
       :style="sidebarStyle"
       :data-color="backgroundColor"
       :data-image="backgroundImage">
    <div class="sidebar-wrapper">
      <div class="logo">
        <a href="#" class="simple-text logo__container">
            <div class="logo-img">
                <img src="../../assets/img/logo.jpg" style="width: 120px;" alt="Logo AMA DC">
            </div>
          {{title}}
        </a>
      </div>

      <slot name="content"></slot>

      <ul class="nav nav-main__links">
        <template v-for="(link, index) in sidebarLinks">

          <li v-if="link.meta && link.meta.category && shouldShowCategoryTitle(link, index)"
              :key="'cat-' + index"
              class="sidebar-category-title">
            <span>{{ link.meta.category }}</span>
          </li>

          <sidebar-link :key="link.name + index"
        <slot>
          <sidebar-link v-for="(link, index) in sidebarLinks"
                        :key="'sidebar-item-' + index"
                        :to="link.path"
                        @click="closeNavbar"
                        :link="link">
            <i :class="link.icon"></i>
            <p>{{ link.name }}</p>
          </sidebar-link>

        </template>
      </ul>

      <ul class="nav nav-bottom" v-if="$slots['bottom-links']">
        <slot name="bottom-links"></slot>
      </ul>
    </div>
  </div>
</template>

<script>
  import SidebarLink from './SidebarLink.vue'

  export default {
    components: {
      SidebarLink
    },
    props: {
      title: {
        type: String,
        default: 'AMA DC'
      },
      backgroundColor: {
        type: String,
        default: 'black',
        validator: (value) => {
          let acceptedValues = ['', 'blue', 'azure', 'green', 'orange', 'red', 'purple', 'black']
          return acceptedValues.indexOf(value) !== -1
        }
      },
      backgroundImage: {
        type: String,
        default: 'img/sidebar-6.png'
      },
      activeColor: {
        type: String,
        default: 'success',
        validator: (value) => {
          let acceptedValues = ['primary', 'info', 'success', 'warning', 'danger']
          return acceptedValues.indexOf(value) !== -1
        }
      },
      sidebarLinks: {
        type: Array,
        default: () => []
      },
      autoClose: {
        type: Boolean,
        default: true
      }
    },
    provide () {
      return {
        autoClose: this.autoClose
      }
    },
    computed: {
      sidebarStyle () {
        return {
          backgroundImage: `url(${this.backgroundImage})`
        }
      },
      // Filtra links marcados com hidden no meta para não poluírem a lista
      visibleLinks() {
        return this.sidebarLinks.filter(link => {
          if (link.meta && link.meta.hidden === true) {
            return false;
          }
          return true;
        });
      }
    },
    methods: {
      // Método lógico que descobre se precisa renderizar o cabeçalho de categoria
      shouldShowCategoryTitle(currentLink, index) {
        if (index === 0) return true;

        const previousLink = this.visibleLinks[index - 1];
        const currentCategory = currentLink.meta ? currentLink.meta.category : null;
        const previousCategory = previousLink.meta ? previousLink.meta.category : null;

        return currentCategory !== previousCategory;
      },
      closeNavbar() {
        if (this.$sidebar && this.$sidebar.showSidebar) {
          this.$sidebar.displaySidebar(false);
        }
      }
    },
    methods: {
      closeNavbar () {
        if (this.autoClose && this.$sidebar && this.$sidebar.showSidebar) {
          this.$sidebar.displaySidebar(false)
        }
      },
      // Lógica para não duplicar o título do grupo no menu
      deveExibirGrupo(link, index) {
        // Se o link não tiver grupo definido, não exibe nada
        if (!link.group) return false;

        // Se for o primeiro item da lista inteira e tem grupo, exibe
        if (index === 0) return true;

        // Só exibe se o grupo do item atual for DIFERENTE do grupo do item anterior
        return link.group !== this.sidebarLinks[index - 1].group;
      }
    }
  }
</script>

<style>

<style scoped>
  .sidebar .sidebar-wrapper {
    display: flex;
    flex-direction: column;
  }
 .sidebar .nav-main__links {
   flex: 1;
 }
 .sidebar .sidebar-wrapper .logo .logo__container {
   padding-left: 10px;
 }

 /* Margem adicional caso não seja o primeiro item do menu */
 .sidebar-group-title:not(:first-child) {
   margin-top: 15px;
 }

 .sidebar-group-title {
   padding: 22px 0px 6px 25px;
   font-size: 11px;
   font-weight: 800;
   text-transform: uppercase;
   letter-spacing: 1.5px;
   color: #ffffff !important; /* 🌟 Branco total para destacar no fundo escuro */
   list-style-type: none;
   display: block;
   width: 100%;
 }
  .sidebar .nav-main__links {
    flex: 1;
    padding-top: 10px;
  }
  .sidebar .sidebar-wrapper .logo .logo__container {
    padding-left: 10px;
  }

  /* Estilização limpa e moderna para os Subtítulos das Categorias */
  .sidebar-category-title {
    padding: 15px 20px 5px 20px;
    font-size: 11px;
    font-weight: 700;
    color: rgba(255, 255, 255, 0.45);
    text-transform: uppercase;
    letter-spacing: 1.2px;
    display: block;
    width: 100%;
    pointer-events: none; /* Evita cliques acidentais */
  }
</style>
