import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const usePrivateMessageStore = defineStore("privateMessage", () => {
    const privateMessages = ref(new Map());
    const friendId = ref(0);
    const friendMessages = computed(() => privateMessages.value.get(friendId.value));
    const friendToNewMessages = ref(new Map());

    function setPrivateMessagesMap(privateMessagesMap) {
        privateMessages.value = privateMessagesMap;
        setFriendToNewMessages();
    }

    function setFriend(friend) {
        friendId.value = friend;
    }

    function setFriendToNewMessages() {
        friendToNewMessages.value.clear();
        privateMessages.value.forEach((value, key) => {
            if(value.length > 0) {
                value.forEach((message) => {
                    if(message.new === true) {
                        if(friendToNewMessages.value.has(key)) {
                            friendToNewMessages.value.set(key, friendToNewMessages.value.get(key) + 1);
                        } else {
                            friendToNewMessages.value.set(key, 1);
                        }
                    }
                });
            }
        });
    }

    return {
        privateMessages: computed(() => privateMessages.value),
        friendMessages: friendMessages.value,
        friendToNewMessages: computed(() => friendToNewMessages.value),
        friendId: computed(() => friendId.value),
        setFriend,
        setPrivateMessagesMap,
    };
});
