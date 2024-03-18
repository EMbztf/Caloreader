import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const useTrainingSession = defineStore("trainingSession", {
  state: () => {
    const trainingSession = ref({});

    function setTrainingSession(data) {
      trainingSession.value = data;
    }

    return {
      trainingSession,
      setTrainingSession,
    };
  },
  persist: true
});
