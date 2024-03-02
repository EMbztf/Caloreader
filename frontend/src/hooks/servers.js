import { ref } from "vue";
import { useErrorStore } from "../stores/errors"
import { useUserStore } from "../stores/users"
import { storeToRefs } from "pinia";
import axios from "axios";

export default function useServers() {

  function showSnackbarError(error) {
    const {
      showError,
    } = useErrorStore();
    showError(error);
  }

  function getJWT() {
    const {
      jwt,
    } = storeToRefs(useUserStore());
    return jwt.value;
  }

  function getCurrentUserId() {
    const {
      id,
    } = storeToRefs(useUserStore());
    return id.value;
  }

  function resetAuthenticatedUser() {
    const {
      reset,
    } = useUserStore();
    reset();
  }

  async function getServersFromUser() {
    const jwt = getJWT();
    const userId = getCurrentUserId();

    try {
      const url = import.meta.env.VITE_BACKEND_URL + "/servers/" + userId;

      const response = await axios.get(url, {
        headers: {
          Authorization: "Bearer " + jwt,
        },
      });

      if(response.status === 200) {
        const serversMap = serversDataToMap(response.data);
        return serversMap;
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

  function serversDataToMap(servers) {
    const serversMap = new Map();
    for(const server of servers) {
      serversMap.set(server.id, server);
    }
    return serversMap;
  }

  async function getChannelsFromUser() {
    const jwt = getJWT();
    const userId = getCurrentUserId();

    try {
      const url = import.meta.env.VITE_BACKEND_URL + "/channels/" + userId;

      const response = await axios.get(url, {
        headers: {
          Authorization: "Bearer " + jwt,
        },
      });

      if(response.status === 200) {
        const channelsMap = channelsDataToMap(response.data);
        return channelsMap;
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


  async function getMembersFromUser() {
    const jwt = getJWT();
    const userId = getCurrentUserId();

    try {
      const url = import.meta.env.VITE_BACKEND_URL + "/members/" + userId;

      const response = await axios.get(url, {
        headers: {
          Authorization: "Bearer " + jwt,
        },
      });

      if(response.status === 200) {
        const uniqueMembersMap = await getUniqueUserMembersFromUser();
        const membersMap = membersDataToMap(response.data, uniqueMembersMap);
        return membersMap;
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

  async function getUniqueUserMembersFromUser() {
    const jwt = getJWT();
    const userId = getCurrentUserId();

    try {
      const url = import.meta.env.VITE_BACKEND_URL + "/members/unique/" + userId;

      const response = await axios.get(url, {
        headers: {
          Authorization: "Bearer " + jwt,
        },
      });

      if(response.status === 200) {
        const membersMap = uniqueMembersDataToMap(response.data);
        return membersMap;
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

  function uniqueMembersDataToMap(members) {
    const membersMap = new Map();
    for(const member of members) {
      membersMap.set(member.id, member);
    }
    return membersMap;
  }

  function membersDataToMap(members, uniqueMembersMap) {
    const membersMap = new Map();
    for(const member of members) {
      if(!membersMap.has(member.server_id)) {
        let editedMember = uniqueMembersMap.get(member.user_id);
        editedMember.kicked = member.kicked;
        membersMap.set(member.server_id, [editedMember]);
      } else {
        let editedMember = uniqueMembersMap.get(member.user_id);
        editedMember.kicked = member.kicked;
        let membersArray = membersMap.get(member.server_id);
        membersArray.push(editedMember);
        membersMap.set(member.server_id, membersArray);
      }
    }
    return membersMap;
  }

  function channelsDataToMap(channels) {
    const channelsMap = new Map();
    for(const channel of channels) {
      if(!channelsMap.has(channel.server_id)) {
        channelsMap.set(channel.server_id, [channel]);
      } else {
        let channelsArray = channelsMap.get(channel.server_id);
        channelsArray.push(channel);
        channelsMap.set(channel.server_id, channelsArray);
      }
    }
    return channelsMap;
  }

  return {
    getServersFromUser,
    getChannelsFromUser,
    getMembersFromUser,
    getUniqueUserMembersFromUser
  };
}
