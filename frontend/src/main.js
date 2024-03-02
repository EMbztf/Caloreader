import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";

// import "./assets/main.css";

// Vuetify
import "vuetify/styles";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import { aliases, mdi } from 'vuetify/iconsets/mdi'
//Vuetify experimental
import { VDataTable } from "vuetify/labs/VDataTable";

import '@mdi/font/css/materialdesignicons.css' 

import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'



const vuetify = createVuetify({
  components: {...components, VDataTable},
  directives,
  theme: {
    defaultTheme: 'dark'
  },
  aliases,
  sets: {
    mdi,
  },
});

const app = createApp(App);


const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

app.use(pinia);
app.use(router);

// Vuetify
app.use(vuetify);

app.mount("#app");
