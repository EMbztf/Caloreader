<template>
  <v-app>
    <AppBar v-if="canShowAppBar()"/>
    <v-main>
      <router-view/>
    </v-main>
  </v-app>
</template>

<script>
import { useTheme } from 'vuetify'
import AppBar from "./components/AppBar.vue";
export default {

  components: { AppBar },

  data: function () {
    return {
      privateMessagesTimer: null,
    }
  },

  setup() {
    const theme = useTheme()

    function toggleTheme() {
      theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
    }

    return { toggleTheme };
  },

  methods: {
    canShowAppBar() {
        const unAuthorizedRoutes = [
            '/trainingSession/'
        ];
        for(const route of unAuthorizedRoutes) {
            if(this.$route.path.includes(route)) {
                return false;
            }
        }
      return true;
    }
  }
};
</script>
