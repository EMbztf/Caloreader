<template>
  <v-app>
<!--    <v-app-bar color="dark grey" density="compact">-->

<!--      <v-toolbar-title>Calorie Tracker</v-toolbar-title>-->

<!--      <v-toolbar-items>-->
<!--        <v-menu>-->
<!--          <template #activator="{ props }">-->
<!--            <v-btn v-bind="props" icon="mdi-theme-light-dark" @click="toggleTheme"></v-btn>-->
<!--          </template>-->
<!--        </v-menu>-->
<!--      </v-toolbar-items>-->
<!--    </v-app-bar>-->
    <AppBar/>

    <v-snackbar
      v-model="errors.errorSnackbar"
    >
      {{ errors.error }}

      <template v-slot:actions>
        <v-btn
          color="pink"
          variant="text"
          @click="closeSnackbar"
        >
          Close
        </v-btn>
      </template>
    </v-snackbar>
    <v-main>
      <router-view/>
    </v-main>
  </v-app>
</template>

<script>
import { ref, watch } from "vue";
import { useTheme } from 'vuetify'
import { useErrorStore } from "@/stores/errors"
import { storeToRefs } from "pinia";
import { useUserStore } from "./stores/users";
import AppBar from "./components/AppBar.vue";
import ServerNavDrawer from "./components/ServerNavDrawer.vue";

export default {

  components: { AppBar },

  data: function () {
    return {
      privateMessagesTimer: null,
    }
  },

  setup() {
    const errors = useErrorStore();
    const {
      closeSnackbar,
    } = useErrorStore();

    const {
      isAuthenticated,
    } = storeToRefs( useUserStore() );

    const isUserAuthenticated = ref(isAuthenticated);

    const theme = useTheme()

    function toggleTheme() {
      theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
    }

    return { closeSnackbar, errors, isUserAuthenticated, toggleTheme };
  },

  methods: {

    isAuthenticated() {
      const {
          isAuthenticated,
      } = storeToRefs( useUserStore() );
      return isAuthenticated.value;
    },

    logout() {
      const user = useUserStore();
      user.reset();
      this.$router.push('/login');
    },

    goToRegister() {
      this.$router.push('/signup');
    },
  }
};
</script>
