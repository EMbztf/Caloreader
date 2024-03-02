<template>
    <v-navigation-drawer
    >
        <v-list>
        <v-list-item :prepend-avatar="activeServerIcon" :title="activeServerName">
            <template v-slot:append>
                <v-btn v-if="isUserOwner" icon @click="goToServerProfile">
                    <v-icon>mdi-cog</v-icon>
                </v-btn>
                <v-btn v-if="!isUserOwner" >
                    <v-icon color="red" >mdi-logout-variant</v-icon>
                    <v-dialog
                        activator="parent"
                        v-model="leaveServerDialog"
                        width="500"
                        >
                            <v-card>
                            <v-card-title>
                                Leave Server
                            </v-card-title>
                            <v-card-text>
                                Do you really want to leave the server?
                            </v-card-text>
                            <v-card-actions>
                                <v-btn color="primary" @click="leaveServer()" >Confirm</v-btn>
                                <v-btn @click="closeLeaveServerDialog()" color="red">Cancel</v-btn>
                            </v-card-actions>
                            </v-card>
                        </v-dialog>
                </v-btn>
            </template>
        </v-list-item>
        <v-divider></v-divider>
        <v-list-item>
            <v-btn color="green">
                Invite People
                <v-dialog
                activator="parent"
                v-model="invitePeopleDialog"
                width="500"
                >
                    <v-card>
                    <v-card-title>
                        Server Code
                    </v-card-title>
                    <v-card-text>
                        <v-form @submit.prevent="" ref="invitePeopleForm">
                            <v-text-field
                                v-model="activeServerIdentifier"
                                label="Code"
                                variant="underlined"
                                readonly
                                append-icon="mdi-content-copy"
                                @click:append="copyServerCodeToClipboard"
                            ></v-text-field>
                        </v-form>
                    </v-card-text>
                    </v-card>
                </v-dialog>
            </v-btn>
        </v-list-item>
        <v-list-item>
            <v-list-item-title>Channels:</v-list-item-title>
            <template v-if="isUserOwner" v-slot:append>
                <v-btn icon @click="createChannelDialog = true">
                    <v-icon color="green" >mdi-plus</v-icon>
                </v-btn>
                <v-dialog
                v-model="createChannelDialog"
                width="500"
                >
                    <v-card>
                    <v-card-title>
                        Create Channel
                    </v-card-title>
                    <v-card-text>
                        <v-form @submit.prevent="createChannel" ref="createChannelForm">
                            <v-text-field
                                v-model="channelName"
                                label="Channel Name"
                                :rules="channelNameRules"
                                :error-messages="channelNameError"
                                required
                            ></v-text-field>
                            <v-card-actions>
                                <v-btn color="primary" type="submit">Create</v-btn>
                                <v-btn @click="closeCreateChannelDialog()" color="red">Cancel</v-btn>
                            </v-card-actions>
                        </v-form>
                    </v-card-text>
                    </v-card>
                </v-dialog>
            </template>
        </v-list-item>
        <v-item-group v-for="channel in channels"> 
            <v-list-item  density="compact" :to="'/channels/' + channel.id" :title="channel.name">
            </v-list-item>
            <v-btn v-if="isUserOwner" icon>
                <v-icon >mdi-pencil</v-icon>
                <v-dialog
                    v-model="editChannelDialogs[channel.id]"
                    activator="parent"
                    width="500"
                    >
                    <v-card>
                    <v-card-title>
                        Edit Channel
                    </v-card-title>
                    <v-card-text>
                        <v-form @submit.prevent="">
                            <v-text-field
                                v-model="newChannelName"
                                label="New Name"
                                :rules="channelNameRules"
                                :error-messages="channelNameError"
                                required
                            ></v-text-field>
                            <v-card-actions>
                                <v-btn color="primary" @click="editChannel(channel.id)" type="submit">Confirm</v-btn>
                                <v-btn @click="closeEditChannelDialog(channel.id)" color="red">Cancel</v-btn>
                            </v-card-actions>
                        </v-form>
                    </v-card-text>
                    </v-card>
                </v-dialog>
            </v-btn>
            <v-btn v-if="isUserOwner" icon>
                <v-icon color="red">mdi-delete</v-icon>
                <v-dialog
                    v-model="deleteChannelDialogs[channel.id]"
                    activator="parent"
                    width="500"
                    >
                    <v-card>
                    <v-card-title>
                        Delete Channel
                    </v-card-title>
                    <v-card-text>
                        Do you really want to delete this channel?
                        <v-form @submit.prevent="">
                            <v-card-actions>
                                <v-btn color="primary" @click="deleteChannel(channel.id)" type="submit">Confirm</v-btn>
                                <v-btn @click="closeDeleteChannelDialog(channel.id)" color="red">Cancel</v-btn>
                            </v-card-actions>
                        </v-form>
                    </v-card-text>
                    </v-card>
                </v-dialog>
            </v-btn>
        </v-item-group>
        </v-list>
    </v-navigation-drawer>
</template>

<script>

import { watch } from "vue";
import axios from "axios";
import { storeToRefs } from "pinia";
import { useServerStore } from "../stores/servers";
import { useRoute } from "vue-router";
import { useUserStore } from "../stores/users";
import useServers from "../hooks/servers";
import { useChannelMessageStore } from "../stores/channelMessages";

export default {

data() {
    return {
        invitePeopleDialog: false,
        leaveServerDialog: false,
        createChannelDialog: false,
        editChannelDialogs: {},
        deleteChannelDialogs: {},
        channelName: '',
        newChannelName: '',
        channelNameRules: [
            v => !!v || "Channel name is required",
        ],
        channelNameError: '',
    };
},

computed() {
    const {
        channelToNewMessages,
        channelId,
    } = storeToRefs(useChannelMessageStore());

    const activeChannelId = channelId;

    return {channelToNewMessages, activeChannelId};
},

computed: {

    activeServerName() {
        const {
            activeServer,
        } = storeToRefs(useServerStore());
        if(!activeServer.value) {
            return '';
        }
        return activeServer.value.name;
    },

    activeServerIcon() {
        const {
            activeServer,
        } = storeToRefs(useServerStore());
        if(!activeServer.value) {
            return '';
        }
        return activeServer.value.picture;
    },

    activeServerIdentifier() {
        const {
            activeServer,
        } = storeToRefs(useServerStore());
        if(!activeServer.value) {
            return '';
        }
        return activeServer.value.identifier;
    },

    async loadServers() {
      const servers = useServers();
      const serversMap = await servers.getServersFromUser();
      const {
        setServers,
      } = useServerStore();
      setServers(serversMap);
    },

    isUserOwner() {
        const {
            isUserOwner,
        } = storeToRefs(useServerStore());
        return isUserOwner.value;
    },

    channels() {
        const {
            activeChannels,
        } = storeToRefs(useServerStore());
        return activeChannels.value;
    },

},

setup() {
    const route = useRoute();

    watch(route, async (to) => {
    if(route.params.id) {
        if(route.path.includes('server')) {
            const {
                setActiveServer,
            } = useServerStore();
            const activeServerId = route.params.id;
            setActiveServer(activeServerId);
        }
    }
    },  { immediate: true });

    return;
},

methods: {
    goToServerProfile() {
        const {
            activeServer,
        } = storeToRefs(useServerStore());
        this.$router.push('/servers/' + activeServer.value.id + '/profile');
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

    copyServerCodeToClipboard() {
        navigator.clipboard.writeText(this.activeServerIdentifier);
    },

    async loadChannels() {
        const servers = useServers();
        const channelsMap = await servers.getChannelsFromUser();
        const {
            setChannels,
        } = useServerStore();
        setChannels(channelsMap);
    },

    async createChannel() {
        await this.$refs.createChannelForm.validate();
        if (this.$refs.createChannelForm.isValid) {
            const {
                jwt,
            } = storeToRefs(useUserStore());

            const {
                activeServer,
            } = storeToRefs(useServerStore());

            try {
                const url = import.meta.env.VITE_BACKEND_URL + "/channels";
                const bodyFormData = new FormData();
                bodyFormData.append("name", this.channelName);
                bodyFormData.append("server_id", activeServer.value.id);

                const response = await axios.post(
                    url, 
                    bodyFormData, {
                    headers: {
                        Authorization: "Bearer " + jwt.value,
                    },
                });

                if(response.status === 200) {
                    this.closeCreateChannelDialog();
                    this.loadChannels();
                } else {
                    this.showSnackbarError(response.statusText);
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
                    this.showSnackbarError(err);
                }
            }
        }
    },  

    async editChannel(channelId) {
        if (this.newChannelName !== '') {
            const {
                jwt,
            } = storeToRefs(useUserStore());

            try {
                const url = import.meta.env.VITE_BACKEND_URL + "/channels/name";
                const bodyFormData = new FormData();
                bodyFormData.append("name", this.newChannelName);
                bodyFormData.append("channel_id", channelId);

                const response = await axios.post(
                    url, 
                    bodyFormData, {
                    headers: {
                        Authorization: "Bearer " + jwt.value,
                    },
                });

                if(response.status === 200) {
                    this.closeEditChannelDialog(channelId);
                    this.loadChannels();
                } else {
                    this.showSnackbarError(response.statusText);
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
                    this.resetAuthenticatedUser();
                } else {
                    this.showSnackbarError(err);
                }
            }
        }
    },

    async deleteChannel(channelId) {
        const {
            jwt,
        } = storeToRefs(useUserStore());

        try {
            const url = import.meta.env.VITE_BACKEND_URL + "/channels/" + channelId;

            const response = await axios.delete(
                url, {
                headers: {
                    Authorization: "Bearer " + jwt.value,
                },
            });

            if(response.status === 200) {
                this.closeDeleteChannelDialog(channelId);
                this.loadChannels();
            } else {
                this.showSnackbarError(response.statusText);
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
                this.resetAuthenticatedUser();
            } else {
                this.showSnackbarError(err);
            }
        }
    },

    async leaveServer() {
        const {
            jwt,
        } = storeToRefs(useUserStore());

        const {
            activeServer,
        } = storeToRefs(useServerStore());

        try {
            const url = import.meta.env.VITE_BACKEND_URL + "/servers/leave";

            const bodyFormData = new FormData();
            bodyFormData.append("server_id", activeServer.value.id);

            const response = await axios.post(
                url, 
                bodyFormData,
                {
                    headers: {
                        Authorization: "Bearer " + jwt.value,
                    },
            });

            if(response.status === 200) {
                this.closeLeaveServerDialog();
                this.$router.push('/servers');
                this.loadServers();
            } else {
                this.showSnackbarError(response.statusText);
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
                this.resetAuthenticatedUser();
            } else {
                this.showSnackbarError(err);
            }
        }
    },

    closeCreateChannelDialog() {
        this.createChannelDialog = false;
        this.channelName = '';
    },

    closeEditChannelDialog(channelId) {
        this.editChannelDialogs[channelId] = false;
        this.newChannelName = '';
    },

    closeDeleteChannelDialog(channelId) {
        this.deleteChannelDialogs[channelId] = false;
    },

    closeLeaveServerDialog() {
        this.leaveServerDialog = false;
    },
},

};
</script>