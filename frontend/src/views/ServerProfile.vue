<template>
    <v-container>
        <v-row justify="space-around">
            <v-card width="500" class="elevation-10">
                <v-toolbar title="Server Profile">
                </v-toolbar>
                <v-card-text>
                    <v-text-field
                        v-model="server.name"
                        label="Name"
                        variant="underlined"
                        readonly
                    >
                    <template v-slot:append>
                        <v-btn icon>
                            <v-icon>mdi-pencil</v-icon>
                            <v-dialog
                            activator="parent"
                            v-model="changeNameDialog"
                            width="500"
                            >
                                <v-card>
                                <v-card-title>
                                    Change Name
                                </v-card-title>
                                <v-card-text>
                                    <v-form @submit.prevent="" ref="editNameForm">
                                        <v-text-field
                                            v-model="newName"
                                            label="New Name"
                                            variant="underlined"
                                            :rules="nameRules"
                                            :error-messages="nameError"
                                        ></v-text-field>
                                    </v-form>
                                </v-card-text>
                                <v-card-actions>
                                    <v-btn color="primary" @click="updateName()" >Confirm</v-btn>
                                    <v-btn @click="closeNameDialog()" color="red">Cancel</v-btn>
                                </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-btn>
                    </template>
                    </v-text-field>
                    <v-card-item :prepend-avatar="server.icon">
                    <template v-slot:append>
                        <v-btn icon>
                            <v-icon>mdi-pencil</v-icon>
                            <v-dialog
                            activator="parent"
                            v-model="changeIconDialog"
                            width="500"
                            >
                                <v-card>
                                <v-card-title>
                                    Change Icon
                                </v-card-title>
                                <v-card-text>
                                    <v-form @submit.prevent="" ref="iconForm">
                                        <v-file-input
                                            v-model="newIcon"
                                            label="Avatar"
                                            variant="underlined"
                                            :rules="iconRules"
                                            :error-messages="iconError"
                                        ></v-file-input>
                                    </v-form>
                                </v-card-text>
                                <v-card-actions>
                                    <v-btn color="primary" @click="updateIcon()" >Confirm</v-btn>
                                    <v-btn @click="closeIconDialog()" color="red">Cancel</v-btn>
                                </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-btn>
                    </template>
                    </v-card-item>
                    <v-card-item>
                        <v-btn color="red">
                            <v-icon>mdi-delete</v-icon>
                            Delete Server
                            <v-dialog
                                activator="parent"
                                v-model="deleteServerDialog"
                                width="500"
                                >
                                <v-card>
                                <v-card-title>
                                    Delete Server Confirmation
                                </v-card-title>
                                <v-card-text>
                                    Do you really want to delete this server?
                                </v-card-text>
                                <v-card-actions>
                                    <v-btn color="primary" @click="deleteServer()" >Confirm</v-btn>
                                    <v-btn @click="closeDeleteServerConfirmationDialog()" color="red">Cancel</v-btn>
                                </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-btn>
                    </v-card-item>
                </v-card-text>
            </v-card>
        </v-row>
    </v-container>
</template>

<script>
import axios from 'axios';
import { useUserStore } from "../stores/users";
import { storeToRefs } from "pinia";
import { useErrorStore } from '../stores/errors';
import { useServerStore } from '../stores/servers';

export default {
data() {
return {
    server: {
        id: null,
        name: '',
        icon: '',
    },
    newName: '',
    newIcon: '',
    nameRules: [
        v => !!v || 'Name is required',
    ],
    iconRules: [
        v => !!v || 'Icon is required',
    ],
    nameError: '',
    iconError: '',
    changeNameDialog: false,
    changeIconDialog: false,
    deleteServerDialog: false,
};
},

setup() {
    return {};
},

mounted() {
    this.loadServer();
},


methods: {
    clearErrors() {
        this.nameError = '';
        this.iconError = '';
    },

    showSnackbarError(error) {
        const errors = useErrorStore();
        errors.showError(error);
    },

    getAuthorizationHeader() {
        const {
            jwt,
        } = storeToRefs(useUserStore());
        return { 'Authorization': 'Bearer ' + jwt.value };
    },

    loadServer() {
        const {
            activeServer,
        } = storeToRefs(useServerStore());

        if(activeServer.value !== null && activeServer.value !== undefined) {
            this.server.id = activeServer.value.id;
            this.server.name = activeServer.value.name;
            this.server.icon = activeServer.value.picture;
        }
    },

    async updateName() {
        this.clearErrors();
        await this.$refs.editNameForm.validate()
        if (this.$refs.editNameForm.isValid) {
            const formData = new FormData();
            formData.append('server_id', this.server.id);
            formData.append('name', this.newName);
            const url = import.meta.env.VITE_BACKEND_URL + '/servers/name';

            try {
                const response = await axios.post(
                    url, 
                    formData,
                    {
                        headers: this.getAuthorizationHeader(),
                });

                if(response.status === 200) {
                    const data = response.data; 
                    this.server.name = data.name;
                    this.closeNameDialog();
                }
            } catch (error) {
                if(error.response.status === 400) {
                    const errors = JSON.parse(error.response.data.message);
                    if(errors.name) {
                        this.nameError = errors.name;
                    }
                } else if(error.response.status === 401) {
                    this.logout();
                } else {
                    this.showSnackbarError(error);
                }
            }
        }
    },

    async updateIcon() {
        this.clearErrors();
        await this.$refs.iconForm.validate();
        if (this.$refs.iconForm.isValid) {
            const formData = new FormData();
            formData.append('server_id', this.server.id);
            formData.append('icon', this.newIcon[0]);
            const url = import.meta.env.VITE_BACKEND_URL + '/servers/picture';

            try {
                const response = await axios.post(
                    url, 
                    formData,
                    {
                        headers: this.getAuthorizationHeader(),
                });

                if(response.status === 200) {
                    const data = response.data; 
                    this.server.icon = data.picture;
                    this.closeIconDialog();
                }
            } catch (error) {
                if(error.response.status === 400) {
                    const errors = JSON.parse(error.response.data.message);
                    if(errors.icon) {
                        this.iconError = errors.icon;
                    }
                } else if(error.response.status === 401) {
                    this.logout();
                } else {
                    this.showSnackbarError(error);
                }
            }
        }
    },

    async deleteServer() {
        const formData = new FormData();
        const url = import.meta.env.VITE_BACKEND_URL + '/servers/' + this.server.id;

        try {
            const response = await axios.delete(
                url, 
                {
                    headers: this.getAuthorizationHeader(),
            });

            if(response.status === 200) {
                this.closeDeleteServerConfirmationDialog();
                this.$router.push('/servers');
            }
        } catch (error) {
            if(error.response.status === 401) {
                this.logout();
            } else {
                this.showSnackbarError(error);
            }
        }
    },

    logout() {
        const user = useUserStore();
        user.reset();
    },

    closeNameDialog() {
        this.changeNameDialog = false;
        this.newName = '';
    },

    closeIconDialog() {
        this.changeIconDialog = false;
        this.newIcon = '';
    },

    closeDeleteServerConfirmationDialog() {
        this.deleteServerDialog = false;
    },
},
};
</script>