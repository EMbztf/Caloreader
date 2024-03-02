import { ref, computed } from "vue";
import { defineStore, storeToRefs } from "pinia";
import { useUserStore } from "./users";

export const useServerStore = defineStore("server", {
    state: () => {

        const servers = ref(new Map());
        const channels = ref(new Map());
        const members = ref(new Map());
        const uniqueMembers = ref(new Map());
        const activeServer = ref(null);
        const activeChannels = ref(null);
        const activeMembers = ref(null);
        const isUserOwner = ref(false);

        function getCurrentUserId() {
            const {
                id,
            } = storeToRefs(useUserStore());
            return id.value;
        }

        function setServers(serversMap) {
            servers.value = serversMap;
        }

        function setActiveServer(serverId) {
            serverId = Number(serverId);
            activeServer.value = getServerById(serverId);
            activeChannels.value = channels.value.get(serverId);
            activeMembers.value = members.value.get(serverId);
            if(!activeServer.value) return;
            isUserOwner.value = activeServer.value.owner_id === getCurrentUserId();
        }

        function setChannels(channelsMap) {
            channels.value = channelsMap;
            if(activeServer.value !== undefined && activeServer.value !== null) {
                activeChannels.value = channels.value.get(activeServer.value.id);
            }
        }

        function setMembers(membersMap) {
            members.value = membersMap;
            if(activeServer.value !== undefined && activeServer.value !== null) {
                activeMembers.value = members.value.get(activeServer.value.id);
            }
        }

        function setUniqueMembers(uniqueMembersMap) {
            uniqueMembers.value = uniqueMembersMap;
        }

        function getServerById(id) {
            id = Number(id);
            return servers.value.get(id);
        }

        function getUniqueMemberById(id) {
            id = Number(id);
            return uniqueMembers.value.get(id);
        }

        return {
            servers: computed(() => servers.value),
            channels: computed(() => channels.value),
            members: computed(() => members.value),
            activeServer: computed(() => activeServer.value),
            activeChannels: computed(() => activeChannels.value),
            activeMembers: computed(() => activeMembers.value),
            isUserOwner: computed(() => isUserOwner.value),
            setServers,
            setActiveServer,
            setChannels,
            setMembers,
            setUniqueMembers,
            getServerById,
            getUniqueMemberById,
        };
    },
    persist: true
});
