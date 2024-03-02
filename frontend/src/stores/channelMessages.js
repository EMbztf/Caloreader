import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const useChannelMessageStore = defineStore("channelMessage", () => {
    const allChannelMessages = ref(new Map());
    const channelId = ref(0);
    const channelMessages = ref([]);
    const channelToNewMessages = ref(new Map());

    function setChannelMessagesMap(channelMessagesMap) {
        allChannelMessages.value = channelMessagesMap;
        if(channelId.value > 0 && allChannelMessages.value !== undefined) {
            channelMessages.value = allChannelMessages.value.get(channelId.value);
        }
        setChannelToNewMessages();
    }

    function setChannel(channel) {
        channelId.value = Number(channel);
        channelMessages.value = allChannelMessages.value.get(channelId.value);
    }

    function setChannelToNewMessages() {
        channelToNewMessages.value.clear();
        allChannelMessages.value.forEach((value, key) => {
            if(value.length > 0) {
                value.forEach((message) => {
                    if(message.new === true) {
                        if(channelToNewMessages.value.has(key)) {
                            channelToNewMessages.value.set(key, channelToNewMessages.value.get(key) + 1);
                        } else {
                            channelToNewMessages.value.set(key, 1);
                        }
                    }
                });
            }
        });
    }

    return {
        allChannelMessages: computed(() => allChannelMessages.value),
        channelMessages: computed(() => channelMessages.value),
        channelToNewMessages: computed(() => channelToNewMessages.value),
        channelId: computed(() => channelId.value),
        setChannel,
        setChannelMessagesMap,
    };
});
