<template>
  <v-app>
    <v-app-bar color="dark grey" density="compact">

      <v-toolbar-title>Calorie Tracker</v-toolbar-title>

      <v-toolbar-items>
        <v-menu>
          <template #activator="{ props }">
            <v-btn v-bind="props" icon="mdi-theme-light-dark" @click="toggleTheme"></v-btn>
          </template>
        </v-menu>
        <v-btn v-if="isUserAuthenticated" icon="mdi-logout" @click="logout()"></v-btn>
        <v-btn v-if="!isUserAuthenticated" icon="mdi-account-plus" @click="goToRegister()"></v-btn>
      </v-toolbar-items>
    </v-app-bar>

    <ServerNavDrawer v-if="isUserAuthenticated"/>

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
import ServerNavDrawer from "./components/ServerNavDrawer.vue";

export default {

  components: { ServerNavDrawer },

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

  // mounted() {
  //   this.loadData();
  //   this.conversationTimer = setInterval(this.loadConversationData, 2000);
  //   this.serversTimer = setInterval(this.loadServerData, 10000);
  // },

  // beforeUnmount() {
  //   clearInterval(this.conversationTimer);
  // },

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
