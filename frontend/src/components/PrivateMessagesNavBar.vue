<template>
    <v-navigation-drawer
    >
        <v-list nav>
        <v-list-item prepend-icon="mdi-account-heart" title="Friends" to="/friends"></v-list-item>
        <v-divider></v-divider>
        <v-list-item v-for="friend in friends" density="compact" :to="'/private-messages/' + friend.id" 
            :prepend-avatar="friend.avatar" 
            :value="friend.id">{{ friend.username }}
            <template v-if="friendToNewMessages.get(friend.id) > 0 && friend.id !== activeFriendId" v-slot:append>
                <v-badge
                color="error"
                :content="friendToNewMessages.get(friend.id)"
                inline
                ></v-badge>
            </template>
        </v-list-item>

        </v-list>
    </v-navigation-drawer>
</template>

<script>

import { storeToRefs } from "pinia";
import { useUserStore } from "../stores/users";
import { usePrivateMessageStore } from "../stores/privateMessages";

export default {

computed() {

    const {
        friends
    } = storeToRefs( useUserStore() );


    const {
        friendToNewMessages,
        friendId,
    } = storeToRefs(usePrivateMessageStore());

    const activeFriendId = friendId;

    return {friends, activeFriendId, friendToNewMessages};
},

setup() {
    const {
        friends
    } = storeToRefs( useUserStore() );

    const {
        friendToNewMessages,
        friendId,
    } = storeToRefs(usePrivateMessageStore());

    const activeFriendId = friendId;

    return {friends, activeFriendId, friendToNewMessages};
},

};
</script>