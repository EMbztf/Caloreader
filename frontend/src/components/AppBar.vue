<template>
  <v-app-bar color="dark grey" density="compact">
    <v-toolbar-title>Calorie Tracker</v-toolbar-title>

    <v-toolbar-items>
      <v-menu>
        <template #activator="{ props }">
          <v-btn v-bind="props" icon="mdi-theme-light-dark" @click="toggleTheme"></v-btn>
        </template>
      </v-menu>
      <v-btn v-if="isUserAuthenticated" icon="mdi-account" @click="goToProfile()"></v-btn>
        <v-btn v-if="isUserAuthenticated" icon="mdi-logout" @click="logout()"></v-btn>
      <v-btn v-if="!isUserAuthenticated" icon="mdi-account-plus" @click="goToRegister()"></v-btn>
    </v-toolbar-items>
  </v-app-bar>
</template>

<script>
import { useTheme } from 'vuetify'
import axios from "axios";

export default {
    data() {
        return {
            isUserAuthenticated: false,
        }

    },

    setup() {
        const theme = useTheme()

        function toggleTheme() {
            theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
        }

        return {toggleTheme};
    },


    mounted() {
        this.isAuthenticated();
    },

    methods: {

        async isAuthenticated() {
            const url = import.meta.env.VITE_BACKEND_URL + '/api/check';
            try {
                axios.defaults.withCredentials = true;
                axios.get(url).then((response) => {
                    console.log(response.status);
                    this.isUserAuthenticated = response.status === 200;
                })
            } catch (error) {
                return false;
            }
        },
        logout() {
            const url = import.meta.env.VITE_BACKEND_URL + '/api/auth/signout';

            try {
                axios.post(url).then((response) => {
                    if(response.status === 200) {
                        this.$router.push('/login');
                    }
                })
            } catch (error) {

            }
        },

        goToProfile() {
            this.$router.push('/profile');
        },

        goToRegister() {
            this.$router.push('/signup');
        },
    }
};
</script>
