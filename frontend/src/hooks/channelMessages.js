import { ref } from "vue";
import { useErrorStore } from "../stores/errors"
import { useUserStore } from "../stores/users"
import { storeToRefs } from "pinia";
import axios from "axios";

export default function useChannelMessages() {


  function showSnackbarError(error) {
    const errorStore = useErrorStore();
    errorStore.showError(error);
  }

  function resetAuthenticatedUser() {
    const {
      reset,
    } = useUserStore();
    reset();
  }

  function getJWT() {
    const {
      jwt,
    } = storeToRefs(useUserStore());
    return jwt.value;
  }

  function getUserId() {
    const {
      id,
    } = storeToRefs(useUserStore());
    return id.value;
  }

  async function getChannelMessages() {
    const jwt = getJWT();
    const userId = getUserId();

    try {
      const url = import.meta.env.VITE_BACKEND_URL + "/channel-messages/" + userId;

      const response = await axios.get(url, {
        headers: {
          Authorization: "Bearer " + jwt,
        },
      });

      if(response.status === 200) {
        const channelMessagesMap = channelMessagesDataToMap(response.data);
        return channelMessagesMap;
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
  }

  function channelMessagesDataToMap(channelMessages) {
    const channelMessagesMap = new Map();
    channelMessages.forEach((channelMessage) => {
        if(channelMessagesMap.has(channelMessage.channel_id)) {
            let channelMessageArray = channelMessagesMap.get(channelMessage.channel_id);
            channelMessageArray.push(channelMessage);
            channelMessagesMap.set(channelMessage.channel_id, channelMessageArray);
        } else {
            let channelMessagesArray = [];
            channelMessagesArray.push(channelMessage);
            channelMessagesMap.set(channelMessage.channel_id, channelMessagesArray);
        }
    });
    return channelMessagesMap;
  }

  return {
    getChannelMessages,
  };
}
