import { ref } from "vue";
import { useErrorStore } from "../stores/errors"
import { useUserStore } from "../stores/users"
import { storeToRefs } from "pinia";
import axios from "axios";

export default function useUsers() {

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

  async function getFriendsFromUser() {
    const {
      jwt,
      id,
    } = storeToRefs(useUserStore());

    try {
      const url = import.meta.env.VITE_BACKEND_URL + "/friends/" + id.value;

      const response = await axios.get(url, {
        headers: {
          Authorization: "Bearer " + jwt.value,
        },
      });

      if(response.status === 200) {
        return response.data;
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

  function getCurrentUser() {
    const {
      getUserData,
    } = useUserStore();
    return getUserData();
  }

  async function getUserById(userId) {
    try {
      const jwt = getJWT();
      const url = import.meta.env.VITE_BACKEND_URL + "/users/" + userId;

      const res = await fetch(url, {
        headers: {
          Authorization: "Bearer " + jwt,
        },
      });

      if(res.ok) {
        const user = await res.json();
        return user;
      } else {
        showSnackbarError(res.statusText);
      }
    } catch (err) {
      if(err.response.status === 401) {
        resetAuthenticatedUser();
      } else {
        showSnackbarError(err);
      }
    }
  }

  return {
    // friends,
    getUserById,
    getCurrentUser,
    getFriendsFromUser
  };
}
