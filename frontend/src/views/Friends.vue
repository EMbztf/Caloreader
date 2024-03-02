<template>
    <PrivateMessagesNavBar/>
    <v-card>
        <v-card-item>
            <v-text-field
                hide-details
                placeholder="Search"
                append-inner-icon="mdi-magnify"
                single-line
                v-model="search"
            >
            <template v-slot:append>
                <v-btn
                    color="green"
                >
                    Add Friend
                    <v-dialog
                        activator="parent"
                        v-model="addDialog"
                        width="500"
                    >
                        <v-card>
                        <v-card-title>
                            Add Friend
                        </v-card-title>
                        <v-card-text>
                            <v-form @submit.prevent="addFriend">
                                <v-text-field
                                    label="Username"
                                    variant="underlined"
                                    v-model="friendName"
                                    :error-messages="addFriendErrorMessage"
                                ></v-text-field>
                                <v-card-actions>
                                    <v-btn color="primary" type="submit">Send Friend Request</v-btn>
                                    <v-btn color="red" @click="closeAddFriendDialog">Cancel</v-btn>
                                </v-card-actions>
                            </v-form>
                        </v-card-text>
                        </v-card>
                    </v-dialog>
                </v-btn>
            </template>
            </v-text-field>
        </v-card-item>
    </v-card>
    <div v-for="friend in filteredFriends">
      <v-divider thickness="2px"></v-divider>   
      <v-card>
          <v-card-item :prepend-avatar="friend.avatar" >
                <v-card-title>
                    {{ friend.username }}
                </v-card-title>
                <template v-slot:append>
                    <v-btn icon="mdi-chat" :to="'private-messages/' + friend.id">
                    </v-btn>
                    <v-btn icon>
                        <v-icon>mdi-delete</v-icon>
                        <v-dialog
                        activator="parent"
                        v-model="removeFriendDialogs[friend.id]"
                        width="500"
                        >
                            <v-card>
                            <v-card-title>
                                Remove Friend
                            </v-card-title>
                            <v-card-text>
                                Do you really want to remove {{ friend.username }} from your friends list?
                            </v-card-text>
                            <v-card-actions>
                                <v-btn color="primary" @click="removeFriend(friend.id)" >Confirm</v-btn>
                                <v-btn @click="closeRemoveFriendDialog(friend.id)" color="red">Cancel</v-btn>
                            </v-card-actions>
                            </v-card>
                        </v-dialog>
                    </v-btn>
                </template>
          </v-card-item>
      </v-card>
      <v-divider thickness="2px"></v-divider>   
  </div>
</template>

<script>

import PrivateMessagesNavBar from '../components/PrivateMessagesNavBar.vue';
import axios from 'axios';
import { useErrorStore } from "../stores/errors"
import { useUserStore } from "../stores/users"
import { storeToRefs } from 'pinia'

export default {

components: { PrivateMessagesNavBar },

data: function () {
    return {
        search: "",
        removeFriendDialogs: {},
        friendName: '',
        addDialog: false,
        addFriendErrorMessage: '',
    };
},


computed: {
    filteredFriends() {
        if(this.friends !== undefined) {
            return this.friends.filter(f => {
                return f.username.toLowerCase().indexOf(this.search.toLowerCase()) != -1;
            });
        }
    }
},

setup() {
    const {
        friends,
    } = storeToRefs(useUserStore());

    return {friends};
},

methods: {

    closeRemoveFriendDialog(friendId) {
      this.removeFriendDialogs[friendId] = false;
    },

    closeAddFriendDialog() {
        this.addDialog = false;
        this.addFriendErrorMessage = '';
        this.friendName = '';
    },

    getAuthorizationHeader() {
        const {
            jwt,
        } = storeToRefs(useUserStore());
        return { 'Authorization': 'Bearer ' + jwt.value };
    },

    async showSnackbarError(error) {
        const errors = useErrorStore();
        errors.showError(error);
    },

    resetAuthenticatedUser() {
        const {
            reset
        } = useUserStore();
        reset();
    },

    async addFriend() {
        const {
            id,
        } = storeToRefs(useUserStore());
        const url = import.meta.env.VITE_BACKEND_URL + '/friends/' + id.value;
        const bodyFormData = new FormData();
        bodyFormData.append('friendName', this.friendName);
        try {
            const response = await axios.post(
                url, 
                bodyFormData, 
                { 
                    headers: this.getAuthorizationHeader()
            });

            if(response.status === 200) {
                this.addDialog = false;
            }
        } catch (error) {
            if(error.response.status === 400) {
                this.addFriendErrorMessage = error.response.data.message;
            } else if(error.response.status === 401) {
                this.resetAuthenticatedUser();
            } else {
                this.showSnackbarError(error);
            }
        }
    },

    async removeFriend(friendId) {
        const {
            id
        } = useUserStore();
        const url = import.meta.env.VITE_BACKEND_URL + '/friends/' + id + '/' + friendId;
        try {
        const response = await axios.delete(
            url, 
            {
                headers: this.getAuthorizationHeader()
        });

        if(response.status === 200) {
            this.closeRemoveFriendDialog(friendId);
        }
        } catch (error) {
            if(error.response.status === 401) {
                this.resetAuthenticatedUser();
            } else {
                this.showSnackbarError(error);
            }
        }
    },
},

};
</script>

<style scoped>
</style>
    