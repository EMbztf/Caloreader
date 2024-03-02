import { createRouter, createWebHistory } from "vue-router";
import Friends from "../views/Friends.vue";
import Login from "../views/Login.vue";
import Signup from "../views/Signup.vue";
import TrainingSessions from "@/views/TrainingSessions.vue";
import { storeToRefs } from "pinia";
import { useUserStore } from "../stores/users";
import { useServerStore } from "../stores/servers";

function isAuthenticated() {
  const {
      isAuthenticated,
  } = storeToRefs( useUserStore() );
  return isAuthenticated.value;
}

function getCurrentUserId() {
  const {
      id,
  } = storeToRefs(useUserStore());
  return id.value;
}

function checkAuth(to, from, next) {
  if (isAuthenticated()) {
    next();
  } else {
    next('/login');
  }
}

function checkIfCurrentUserIsOwnerFromServer(serverId, next, from) {
  const {
    getServerById,
  } = useServerStore();
  const server = getServerById(serverId);
  if (server !== undefined && server.owner_id === getCurrentUserId()) {
    next();
  } else {
    next(from.path);
  }
}

const router = createRouter({

  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/login",
      name: "login",
      component: Login,
      beforeEnter: (to, from, next) => {
        if (isAuthenticated()) {
          next('/friends');
        } else {
          next();
        }
      }
    },
    {
      path: "/signup",
      name: "signup",
      component: Signup,
    },
    {
      path: "/trainingSession",
      name: "trainingSession",
      component: TrainingSessions,
    },
    {
      path: "/friends",
      name: "friends",
      component: Friends,
      beforeEnter: (to, from, next) => {
        checkAuth(to, from, next);
      },
    },
  ],
});

export default router;
