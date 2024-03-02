import { ref } from "vue";
import { useErrorStore } from "../stores/errors"
import { useUserStore } from "../stores/users"
import { storeToRefs } from "pinia";
import axios from "axios";

export default function usePrivateMessages() {


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

  async function getPrivateMessages() {
    const jwt = getJWT();
    const userId = getUserId();

    try {
      const url = import.meta.env.VITE_BACKEND_URL + "/private-messages/" + userId;

      const response = await axios.get(url, {
        headers: {
          Authorization: "Bearer " + jwt,
        },
      });

      if(response.status === 200) {
        const privateMessagesMap = privateMessageDataToMap(response.data);
        return privateMessagesMap;
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
  
  function privateMessageDataToMap(privateMessages) {
    const userId = getUserId();
    const privateMessagesMap = new Map();
    privateMessages.forEach((privateMessage) => {
      if(privateMessage.sender_id === userId) {
        if(privateMessagesMap.has(privateMessage.receiver_id)) {
          let privateMessageArray = privateMessagesMap.get(privateMessage.receiver_id);
          privateMessageArray.push(privateMessage);
          privateMessagesMap.set(privateMessage.receiver_id, privateMessageArray);
        } else {
          let privateMessagesArray = [];
          privateMessagesArray.push(privateMessage);
          privateMessagesMap.set(privateMessage.receiver_id, privateMessagesArray);
        }
      } else {
        if(privateMessagesMap.has(privateMessage.sender_id)) {
          let privateMessageArray = privateMessagesMap.get(privateMessage.sender_id);
          privateMessageArray.push(privateMessage);
          privateMessagesMap.set(privateMessage.sender_id, privateMessageArray);
        } else {
          let privateMessagesArray = [];
          privateMessagesArray.push(privateMessage);
          privateMessagesMap.set(privateMessage.sender_id, privateMessagesArray);
        }
      }
    });
    return privateMessagesMap;
  }

  return {
    getPrivateMessages,
  };
}
