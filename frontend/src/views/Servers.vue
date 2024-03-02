<template>
    <ChannelsNavBar/>
    <MembersSidebar/>
    <div v-for="message in messages">
        <v-card :id="'message' + message.id">
            <v-card-item :prepend-avatar="getAvatarFromMember(message.sender_id)">
                <v-card-title>
                    {{ getUsernameFromMember(message.sender_id) }}
                    <v-menu v-if="message.sender_id === getLoggedInUserId()">
                        <template v-slot:activator="{ props }">
                        <v-btn elevation="0" icon="mdi-dots-vertical" v-bind="props"></v-btn>
                        </template>
                        <v-list>
                            <v-list-item prepend-icon="mdi-pencil" @click="showMessageBox(message)" >
                                <v-list-item-title>Edit</v-list-item-title>
                            </v-list-item>
                            <v-list-item prepend-icon="mdi-delete" @click="deleteMessageById(message.id)">
                                <v-list-item-title>Delete</v-list-item-title>
                            </v-list-item>
                        </v-list>
                    </v-menu>
                </v-card-title>
                <v-card-subtitle>{{ message.date }}</v-card-subtitle>
            </v-card-item>
            <v-card-text :id="'messageText' + message.id">
                <v-textarea variant="plain" v-model="message.message" auto-grow rows="1" readonly></v-textarea>
            </v-card-text>
            <v-card-text class="d-none" :id="'editMessage' + message.id">
                <v-textarea v-model="editedMessage" @keydown.esc="hideMessageBox(message.id)" @keydown.enter.prevent="handleEditMessageEnter($event, message)" auto-grow rows="1"></v-textarea>
            </v-card-text>
        </v-card>
        <v-divider thickness="3px"></v-divider>     
    </div>
    <v-bottom-navigation v-if="isChannel" class="elevation-20">
        <v-textarea v-model="message.message" ref="sendMessageTextArea" @keydown.enter.prevent="handleSendMessageEnter" variant="solo" auto-grow placeholder="Send a message" rows="1">
            <template v-slot:append-inner>
            <v-icon type="submit" @click="sendMessage(message)"> 
                mdi-send
            </v-icon>
            </template>
        </v-textarea>
    </v-bottom-navigation>
</template>

<script>

import ChannelsNavBar from "../components/ChannelsNavBar.vue";
import axios from 'axios';
import { useErrorStore } from "../stores/errors";
import { useUserStore } from "../stores/users";
import { storeToRefs } from "pinia";
import { useChannelMessageStore } from "../stores/channelMessages";
import { useServerStore } from "../stores/servers";
import MembersSidebar from "../components/MembersSidebar.vue";
import { useRoute } from "vue-router";
import { ref, watch } from "vue";

export default {

components: { ChannelsNavBar, MembersSidebar }, 

data() {
return {
    message: {
        message: '',
        sender_id: 0,
        receiver_id: 0,
    },
    editedMessage: '',
    usernameRules: [
        v => !!v || 'Username is required',
        v => (v && v.length <= 30) || 'Username must be less than 30 characters',
    ],
};
},

computed: {
    messages() {
        const {
            channelMessages,
        } = storeToRefs(useChannelMessageStore());

        if(channelMessages !== undefined && this.isChannel) {
            return channelMessages.value;
        }

        return [];
    }
},


setup() {
    const route = useRoute();
    const channel = ref(null);
    const user = ref(null);
    const isChannel = ref(false);

    watch(route, async (to) => {
        if(route.path.includes('channel')) {    
            if(route.params.id) {
                isChannel.value = true;
                const {
                    setChannel,
                } = useChannelMessageStore();
                const channelId = route.params.id;
                setChannel(channelId);

  
                const {
                    getUserData,
                } = useUserStore();

                const userObject = getUserData();
                user.value = userObject; 
                channel.value = channel;
            }
        } else {
            isChannel.value = false;
            const {
                setActiveServer,
            } = useServerStore();
            const activeServerId = route.params.id;
            setActiveServer(activeServerId);
        }
    },  { immediate: true })


    return { channel, user, isChannel};
},

updated: function() {
    if(this.isChannel) {
        this.updateConversationLastSeen();
    }
},

methods: {

    getJWT() {
        const {
            jwt,
        } = storeToRefs(useUserStore());
        return jwt.value;
    },

    showError(error) {
        const {
            showError
        } = useErrorStore();
        showError(error);
    },

    trimLeadingWhitespaceAndNewlines(string) {
        return string.replace(/^\s+/, '');
    },

    hideMessageText(messageId) {
        const messageText = document.getElementById('messageText' + messageId);
        messageText.classList.add("d-none");
    },

    showMessageText(messageId) {
        const messageText = document.getElementById('messageText' + messageId);
        messageText.classList.remove("d-none");
    },

    showEditMessageBox(messageId) {
        const message = document.getElementById('editMessage' + messageId);
        message.classList.remove("d-none");
    },

    hideEditMessageBox(messageId) {
        const message = document.getElementById('editMessage' + messageId);
        message.classList.add("d-none");
    },

    showMessageBox(message) {
        this.editedMessage = message.message;
        this.showEditMessageBox(message.id);
        this.hideMessageText(message.id);
    },

    cancelEdit(messageId) {
        this.hideMessageBox(messageId);
        this.message.message = this.editedMessage;
    },

    hideMessageBox(messageId) {
        this.hideEditMessageBox(messageId);
        this.showMessageText(messageId);
    },

    handleSendMessageEnter(event) {
        if (event.shiftKey) {
        this.newLineToMessage();
        } else {
        this.sendMessage();
        }
    },

    handleEditMessageEnter(event, message) {
        if (event.shiftKey) {
        this.newLineToEditMessage();
        } else {
        this.editMessage(message);
        }
    },

    getAvatarFromMember(memberId) {
        const {
            getUniqueMemberById,
        } = useServerStore();

        const member = getUniqueMemberById(memberId);
        return member.avatar;
    },

    getUsernameFromMember(memberId) {
        const {
            getUniqueMemberById,
        } = useServerStore();

        const member = getUniqueMemberById(memberId);
        return member.username;
    },

    getLoggedInUserId() {
        const {
            id
        } = storeToRefs(useUserStore());
        return id.value;
    },

    async updateConversationLastSeen() {

        const channelId = Number(this.$route.params.id);
        const bodyFormData = new FormData();
        bodyFormData.append('user_id', this.user.id);
        bodyFormData.append('channel_id', channelId);

        const {
            jwt,
        } = storeToRefs(useUserStore());

        try {
            const url = import.meta.env.VITE_BACKEND_URL + "/channels/last-seen";
            const response = axios.post(
                url, 
                bodyFormData, {
                headers: {
                    'Authorization': 'Bearer ' + jwt.value,
                }
            });

            if(!response.status === 200) {
                console.error(response.statusText);
            }
            } catch (err) {
                this.showError(err);
        }
    },

    async sendMessage() {
        let message = this.message;
        let trimmedMessage = message.message.trimStart().trimEnd();

        if(trimmedMessage === '') {
            return;
        }
        const url = import.meta.env.VITE_BACKEND_URL + '/channel-messages';
        const jwt = this.getJWT();
        const bodyFormData = new FormData();
        const channelId = Number(this.$route.params.id);
        bodyFormData.append('message', trimmedMessage);
        bodyFormData.append('channel_id', channelId);

        try {
        const response = await axios.post(
            url, 
            bodyFormData, {
            headers: {
                'Authorization': 'Bearer ' + jwt,
            }
        });

        if(response.status === 200) {
            message.message = '';
            this.scrollToLastMessage();
        } else {
            this.showError(response.statusText);
        }
        } catch (error) {
        this.showError(error);
        }
    },

    async editMessage(message) {
        const url = import.meta.env.VITE_BACKEND_URL + '/channel-messages/' + message.id;
        const jwt = this.getJWT();
        const bodyFormData = new FormData();
        const trimmedMessage = this.editedMessage.trimStart().trimEnd();
        bodyFormData.append('message', trimmedMessage);
        try {
        const response = await axios.post(
            url, 
            bodyFormData, 
            {
            headers: {
                'Authorization': 'Bearer ' + jwt,
            }
        });

        if(response.status === 200) {
            this.hideMessageBox(message.id);
            message.message = trimmedMessage;
        } else {
            this.showError(response.statusText);
        }
        } catch (error) {
        this.showError(error);
        }
    },

    async deleteMessageById(messageId) {
        const url = import.meta.env.VITE_BACKEND_URL + '/channel-messages/' + messageId;
        const jwt = this.getJWT();

        try {
        const response = await axios.delete(
            url, {
            headers: {
                'Authorization': 'Bearer ' + jwt,
            }
        });

        if(!response.status === 200) {
            this.showError(response.statusText);
        }
        } catch (error) {
            this.showError(error);
        }
    },

    newLineToMessage() {
        const textField = this.$refs.sendMessageTextArea;
        if (textField.selectionStart !== undefined) {
            const cursorPosition = textField.selectionStart;
            const text = this.message.message;
            const textBefore = text.substring(0, cursorPosition);
            const textAfter  = text.substring(cursorPosition, text.length);
            this.message.message = textBefore + "\n" + textAfter;
        }
    },

    newLineToEditMessage() {
        this.editedMessage += "\n";
    },

    scrollToLastMessage() {
        if(this.messages === undefined || this.messages.length === 0) {
        return;
        }
        const lastMessage = this.messages[this.messages.length - 1];
        if (lastMessage) {
        let messageText = document.getElementById('messageText' + lastMessage.id);

        messageText.scrollIntoView({behavior: 'smooth'});
        }
    },

    scrollDown() {
        const scrollY = window.scrollY + 1000; 

        window.scrollTo({
        top: scrollY,
        behavior: 'smooth', 
        });
    },
},
};

</script>
    