import { ref, computed } from "vue";
import { defineStore } from "pinia";
import router from "../router";

export const useUserStore = defineStore("user", {
    state: () => {
        // define reactive state values

        const friends = ref([]);

        const jwt = ref("");
        const id = ref("");
        const username = ref("");
        const avatar = ref("");
        const aboutMe = ref("");

        const isAuthenticated = computed(() => jwt.value !== "");

        function setUserData(data) {
            jwt.value = data.jwt;
            id.value = data.id;
            username.value = data.username;
            avatar.value = data.avatar;
            aboutMe.value = data.aboutMe;
        }

        function reset() {
            jwt.value = "";
            id.value = "";
            username.value = "";
            avatar.value = "";
            aboutMe.value = "";
            router.push("/login");
        }

        function getUserData() {
            return {
                jwt: jwt.value,
                id: id.value,
                username: username.value,
                avatar: avatar.value,
                aboutMe: aboutMe.value
            };
        }

        function setFriends(friendsArray) {
            friends.value = friendsArray;
        }


        return {
            jwt,
            id,
            username,
            avatar,
            aboutMe,
            isAuthenticated,
            friends: computed(() => friends.value),
            reset,
            getUserData,
            setUserData,
            setFriends,
        };
    },
    persist: true
});
