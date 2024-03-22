import { createRouter, createWebHistory } from "vue-router";
import Login from "../views/Login.vue";
import Signup from "../views/Signup.vue";
import TrainingSessionGenerator from "@/views/TrainingSessionGenerator.vue";
import TrainingSession from "../views/TrainingSession.vue";
import PreviousTrainingSessions from "../views/PreviousTrainingSessions.vue";
import NotFound from "../views/NotFound.vue";
import Profile from "../views/Profile.vue";
import axios from "axios";
import Sports from "@/views/Sports.vue";
import Sportplans from "@/views/Sportplans.vue";

async function isAuthenticated() {
  const url = import.meta.env.VITE_BACKEND_URL + '/api/check';
  try {
    axios.defaults.withCredentials = true;
    const response = await axios.get(url);

    return response.status === 200;
  } catch (error) {
    return false;
  }
}

async function checkAuth(to, from, next) {
  if (await isAuthenticated()) {
    next();
  } else {
    next('/login');
  }
}

const router = createRouter({

  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/trainingSessionGenerator',
    },
    {
      path: '/404',
      name: 'PageNotExist',
      component: NotFound,
    },
    {
      path: "/:catchAll(.*)", // Unrecognized path automatically matches 404
      redirect: '/404',
    },
    {
      path: "/login",
      name: "login",
      component: Login,
      beforeEnter: async (to, from, next) => {
        if (await isAuthenticated()) {
          next('/trainingSessionGenerator');
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
      path: "/trainingSessions",
      name: "trainingSessions",
      component: PreviousTrainingSessions,
      beforeEnter: async (to, from, next) => {
        await checkAuth(to, from, next);
      },
    },
    {
      path: "/trainingSession/:id",
      name: "trainingSession",
      component: TrainingSession,
      beforeEnter: async (to, from, next) => {
        await checkAuth(to, from, next);
      },
    },
    {
      path: "/trainingSessionGenerator",
      name: "trainingSessionGenerator",
      component: TrainingSessionGenerator,
      beforeEnter: async (to, from, next) => {
        await checkAuth(to, from, next);
      },
    },
    {
      path: "/sports",
      name: "sports",
      component: Sports,
      beforeEnter: async (to, from, next) => {
        await checkAuth(to, from, next);
      },
    },
    {
      path: "/sportplans",
      name: "sportplans",
      component: Sportplans,
      beforeEnter: async (to, from, next) => {
        await checkAuth(to, from, next);
      },
    },
    {
      path: "/profile",
      name: "profile",
      component: Profile,
      beforeEnter: async (to, from, next) => {
        await checkAuth(to, from, next);
      },
    },
  ],
});

export default router;
