<template>
    <v-navigation-drawer
        location="right"
    >
    <v-list nav>
        <v-list-item>Members:</v-list-item>
        <v-container v-for="member in members">
            <v-list-item v-if="member.kicked == false" :prepend-avatar="member.avatar" :title="member.username">
                <template v-if="isUserOwner && member.id !== getCurrentUserId" v-slot:append>
                    <v-btn icon>
                        <v-icon color="red" >mdi-account-remove</v-icon>
                        <v-dialog
                        activator="parent"
                        v-model="removeMemberDialog"
                        width="500"
                        >
                            <v-card>
                            <v-card-title>
                                Remove Member Confirmation
                            </v-card-title>
                            <v-card-text>
                                Do you really want to remove this member?
                            </v-card-text>
                            <v-card-actions>
                                <v-btn color="primary" @click="kickMember(member)" >Yes</v-btn>
                                <v-btn @click="closeRemoveMemberDialog()" color="red">Cancel</v-btn>
                            </v-card-actions>
                            </v-card>
                        </v-dialog>
                    </v-btn>
                </template>
            </v-list-item>   
        </v-container>
    </v-list>
    </v-navigation-drawer>
</template>

<script>

import { watch } from "vue";
import { storeToRefs } from "pinia";
import { useErrorStore } from "../stores/errors";
import useServers from "../hooks/servers";
import { useServerStore } from "../stores/servers";
import { useRoute } from "vue-router";
import { useUserStore } from "../stores/users";
import axios from "axios";

export default {

data() {
    return {
        removeMemberDialog: false,
    };
},

computed: {

    getCurrentUserId() {
        const {
            id
        } = storeToRefs(useUserStore());
        return id.value;
    },

    isUserOwner() {
        const {
            isUserOwner,
        } = storeToRefs(useServerStore());
        return isUserOwner.value;
    },

    members() {
        const {
            activeMembers,
        } = storeToRefs(useServerStore());
        return activeMembers.value;
    },
},


methods: {

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

    async loadMembers() {
      const servers = useServers();
      const membersMap = await servers.getMembersFromUser();
      const {
        setMembers,
      } = useServerStore();
      setMembers(membersMap);
    },

    async kickMember(member) {
        const {
            activeServer
        } = storeToRefs(useServerStore());

        const serverId = activeServer.value.id;

        const {
            jwt,
        } = storeToRefs(useUserStore());
        try{
            const url = import.meta.env.VITE_BACKEND_URL + "/members/kick";
            const bodyFormData = new FormData();
            bodyFormData.append("member_id", member.id);
            bodyFormData.append("server_id", serverId);

            const response = await axios.post(
                url, 
                bodyFormData, {
                    headers: {
                        Authorization: "Bearer " + jwt.value,
                    },
            });

            if(response.status === 200) {
                this.closeRemoveMemberDialog();
                this.loadMembers();
            } else {
                showSnackbarError(response.statusText);
            }
        } catch (err) {
            if(err.response.status === 401) {
                resetAuthenticatedUser();
            } else {
                showSnackbarError(err);
            }
        }
    },

    closeRemoveMemberDialog() {
        this.removeMemberDialog = false;
    },
},

};
</script>