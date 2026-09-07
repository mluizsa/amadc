<template>
  <component :is="tag"
             @click.native="hideSidebar"
             v-bind="$attrs"
             tag="li">
    
    <div v-if="link && link.isHeader" class="sidebar-group-title">
      <span>{{ link.headerTitle }}</span>
    </div>

    <a class="nav-link">
      <slot>
        <i v-if="link.icon" :class="link.icon"></i>
        <p>{{ link.name }}</p>
      </slot>
    </a>

  </component>
</template>
<script>
  export default {
    inheritAttrs: false,
    inject: {
      autoClose: {
        default: true
      }
    },
    props: {
      link: {
        type: [String, Object],
        default: () => {
          return {
            name: '',
            path: '',
            icon: ''
          }
        }
      },
      tag: {
        type: String,
        default: 'router-link'
      }
    },
    methods: {
      hideSidebar () {
        if (this.autoClose && this.$sidebar && this.$sidebar.showSidebar === true) {
          this.$sidebar.displaySidebar(false)
        }
      }
    }
  }
</script>
<style>
.sidebar-group-title {
  padding: 22px 0px 8px 25px !important;
  font-size: 11px !important;
  font-weight: 800 !important;
  text-transform: uppercase !important;
  letter-spacing: 1.5px !important;
  color: rgba(255, 255, 255, 0.5) !important; /* Cor cinza sutil */
  display: block !important;
  width: 100% !important;
  pointer-events: none; /* Garante que clicar no texto não dispare navegação */
}
</style>
