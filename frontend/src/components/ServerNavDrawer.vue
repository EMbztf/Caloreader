<template>
    <v-navigation-drawer
      expand-on-hover
      rail
    >
      <v-list nav>
        <v-list-item prepend-avatar="https://cdn0.iconfinder.com/data/icons/computer-web/512/16-512.png" to="/friends" title="Private Messages" value="dm"></v-list-item>
        <v-divider></v-divider>
        <v-list-item prepend-icon="mdi-plus-circle">
          <v-btn>
            Add Server
            <v-dialog
              activator="parent"
              v-model="addServerDialog"
              width="500"
              >
                  <v-card>
                  <v-card-title>
                      Create Server
                  </v-card-title>
                  <v-card-text>
                      <v-form @submit.prevent="" ref="addServerForm">
                          <v-text-field
                              v-model="name"
                              label="Server Name"
                              variant="underlined"
                              :rules="nameRules"
                              :error-messages="nameError"
                          ></v-text-field>
                          <v-file-input
                              v-model="icon"
                              label="Server Icon"
                              variant="underlined"
                              :rules="iconRules"
                              :error-messages="iconError"
                          ></v-file-input>
                      </v-form>
                  </v-card-text>
                  <v-card-actions>
                      <v-btn color="primary" @click="addServer()" >Confirm</v-btn>
                      <v-btn @click="closeServerDialog()" color="red">Cancel</v-btn>
                  </v-card-actions>
                  </v-card>
            </v-dialog>
          </v-btn>
        </v-list-item>
        <v-list-item prepend-icon="mdi-account-arrow-right">
          <v-btn>
            Join Server
            <v-dialog
              activator="parent"
              v-model="joinServerDialog"
              width="500"
              >
                  <v-card>
                  <v-card-title>
                      Join Server
                  </v-card-title>
                  <v-card-text>
                      <v-form @submit.prevent="" ref="joinServerForm">
                          <v-text-field
                              v-model="serverId"
                              label="Server ID"
                              variant="underlined"
                              :rules="serverIdRules"
                              :error-messages="serverIdError"
                          ></v-text-field>
                      </v-form>
                  </v-card-text>
                  <v-card-actions>
                      <v-btn color="primary" @click="joinServer()" >Join</v-btn>
                      <v-btn @click="closeJoinServerDialog()" color="red">Cancel</v-btn>
                  </v-card-actions>
                  </v-card>
            </v-dialog>
          </v-btn>
        </v-list-item>
        <v-divider></v-divider>
        <v-list-item v-for="[serverId, server] in servers" density="compact" :prepend-avatar="server.picture" :to="'/servers/' + serverId" :value="serverId">{{ server.name }}</v-list-item>
      </v-list>
    </v-navigation-drawer>
</template>

<script>

import axios from "axios";
import { useServerStore } from "../stores/servers";
import { storeToRefs } from "pinia";
import { useUserStore } from "../stores/users";

export default {

data() {
  return {
    addServerDialog: false,
    joinServerDialog: false,
    name: "",
    icon: null,
    serverId: "",
    nameRules: [
      v => !!v || "Server name is required",
      v => (v && v.length <= 20) || "Server name must be less than 20 characters",
    ],
    iconRules: [
      v => !!v || "Server icon is required",
    ],
    serverIdRules: [
      v => !!v || "Server ID is required",
    ],
    nameError: '',
    iconError: '',
    serverIdError: '',
  }
},

computed() {

},

setup() {
  const {
      servers
  } = storeToRefs( useServerStore() );


  return {servers};
},

methods: {
  closeServerDialog() {
    this.addServerDialog = false;
    this.clearErrors();
    this.name = "";
    this.icon = null;
  },

  closeJoinServerDialog() {
    this.joinServerDialog = false;
    this.clearErrors();
    this.serverId = "";
  },

  showSnackbarError(error) {
    const {
      showError,
    } = useErrorStore();
    showError(error);
  },

  resetAuthenticatedUser() {
    const {
      reset,
    } = useUserStore();
    reset();
  },

  clearErrors() {
    this.nameError = '';
    this.iconError = '';
    this.serverIdError = '';
  },

  async loadServers() {
    const servers = useServers();
    const serversMap = await servers.getServersFromUser();
    const {
      setServers,
    } = useServerStore();
    setServers(serversMap);
  },
  
  async addServer() {
    this.clearErrors();
    await this.$refs.addServerForm.validate();
    if (this.$refs.addServerForm.isValid) {
      const {
        jwt,
        id,
      } = storeToRefs(useUserStore());

      try {
        const url = import.meta.env.VITE_BACKEND_URL + "/servers";
        const bodyFormData = new FormData();
        bodyFormData.append("name", this.name);
        bodyFormData.append("icon", this.icon[0]);
        bodyFormData.append("owner_id", id.value);

        const response = await axios.post(
          url, 
          bodyFormData, {
          headers: {
            Authorization: "Bearer " + jwt.value,
          },
        });

        if(response.status === 200) {
          this.closeServerDialog();
        } else {
          showSnackbarError(response.statusText);
        }
      } catch (err) {
        if(err.response.status === 400) {
          const errors = err.response.data.message;
          if(errors.name) {
            this.nameError = errors.name;
          }
          if(errors.icon) {
            this.iconError = errors.icon;
          }
        } else if(err.response.status === 401) {
          resetAuthenticatedUser();
        } else {
          showSnackbarError(err);
        }
      }
    }
  },

  async joinServer() {
    this.clearErrors();
    await this.$refs.joinServerForm.validate();
    if (this.$refs.joinServerForm.isValid) {
      const {
        jwt,
        id,
      } = storeToRefs(useUserStore());

      try {
        const url = import.meta.env.VITE_BACKEND_URL + "/servers/join";
        const bodyFormData = new FormData();
        bodyFormData.append("identifier", this.serverId);
        bodyFormData.append("user_id", id.value);

        const response = await axios.post(
          url, 
          bodyFormData, {
          headers: {
            Authorization: "Bearer " + jwt.value,
          },
        });

        if(response.status === 200) {
          this.closeJoinServerDialog();
          this.loadServers();
        } else {
          showSnackbarError(response.statusText);
        }
      } catch (err) {
        if(err.response.status === 400) {
          console.error(err.response.data.message);
          const errors = err.response.data.message;
          if(errors.identifier) {
            this.serverIdError = errors.identifier;
          }
        } else if(err.response.status === 401) {
          resetAuthenticatedUser();
        } else {
          showSnackbarError(err);
        }
      }
    }
  },
},
}
</script>