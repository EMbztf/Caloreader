<template>
  <v-app-bar>
    <template v-slot:prepend>
      <v-icon @click="openQuitConfirmationDialog()">
        mdi-arrow-left
      </v-icon>
    </template>

    <v-toolbar-title>Caloreader</v-toolbar-title>

  </v-app-bar>
  <v-dialog
      v-model="quitConfirmationDialog"
      width="500"
  >
    <v-card>
      <v-card-title>
        Quit
      </v-card-title>
      <v-card-text>
        Do you really want to quit the workout?
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="goToWorkouts()">Confirm</v-btn>
        <v-btn @click="closeQuitConfirmationDialog()" color="red">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="6">
        <v-card width="fullscreen" class="ma-6" v-for="exercise in filteredExercises" >
          <v-card-title>
            {{ exercise.name }}
          </v-card-title>
          <v-card-subtitle>
            {{ exercise.repetitions > 1 ? 'x ' + exercise.repetitions : formatTime(exercise.duration) }}
          </v-card-subtitle>
          <v-card-text>

          </v-card-text>
          <v-card-actions>
            <v-btn
                v-if="currentExercise > 0"
                icon="mdi-arrow-left"
                @click="goToPreviousExercise()"
            ></v-btn>
            <v-btn
                @click="goToWorkouts()"
                v-if="currentExercise === trainingSession.exercises.length - 1"
                icon="mdi-check"
                class="bg-blue"
            >
            </v-btn>
            <v-btn
                v-if="currentExercise < trainingSession.exercises.length - 1"
                icon="mdi-arrow-right"
                @click="goToNextExercise()"
            ></v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>

import axios from "axios";

export default {
  data() {
    return {
      trainingSession: {},
      currentExercise: 0,
      intervalId: null,
      timeInSeconds: 0,
      timerActive: false,
      quitConfirmationDialog: false,
    };
  },

  setup() {

  },

  mounted() {
    this.loadTrainingSession();
    axios.defaults.withCredentials = true;
  },

  computed: {
    filteredExercises() {
      if(this.trainingSession.exercises === undefined) {
        return [];
      }
      return this.trainingSession.exercises.filter((exercise, index) => this.currentExercise === index);
    }
  },

  methods: {
    formatTime(timeInSeconds) {
      const minutes = Math.floor(timeInSeconds / 60);
      const seconds = timeInSeconds % 60;
      return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    },
    async loadTrainingSession() {
      const sessionId = this.$route.params.id;
      if(sessionId === undefined) {
        return
      }
      const url = import.meta.env.VITE_BACKEND_URL + '/api/trainingSessions/' + sessionId;
      axios.get(url).then((response) => {
        this.trainingSession = response.data;
      });
    },
    openQuitConfirmationDialog() {
      this.quitConfirmationDialog = true;
    },
    closeQuitConfirmationDialog() {
      this.quitConfirmationDialog = false;
    },
    goToWorkouts() {
      this.$router.push('/trainingSessionGenerator');
    },
    goToNextExercise() {
        this.currentExercise++;
    },
    goToPreviousExercise() {
      this.currentExercise--;
    },
    startTimer() {
      this.timerActive = true;
      this.intervalId = setInterval(() => {
        this.timeInSeconds++;
      }, 1000);
    },
    stopTimer() {
      clearInterval(this.intervalId);
      this.timerActive = false;
    },
    resetTimer() {
      clearInterval(this.intervalId);
      this.timerActive = false;
      this.timeInSeconds = 0;
    }
  },

};
</script>

