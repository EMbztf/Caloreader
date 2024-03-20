<template>
  <v-container>
    <v-row justify="space-around">
      <v-card width="500" class="elevation-10 ma-4">
        <v-toolbar title="Workouts">
            <v-btn color="primary" @click="gotToYourWorkouts()">
                Your Workouts
            </v-btn>
        </v-toolbar>
        <v-card-text>
          <v-container v-for="muscleGroup in muscleGroups">
            <v-card-item :prepend-avatar="BACKEND_URL + '/api/images/' + muscleGroup.imagePath" @click="openGenerateTrainingSessionDialog(muscleGroup)">
              <v-card-title>{{ muscleGroup.name }}</v-card-title>
            </v-card-item>
            <v-divider></v-divider>
          </v-container>
        </v-card-text>
      </v-card>

      <v-dialog
          v-model="generateTrainingSessionDialog"
          width="500"
      >
        <v-card>
          <v-card-title>
            Calories to burn
          </v-card-title>
          <v-card-text>
            <v-form @submit.prevent="" ref="generateTrainingSessionForm">
              <v-text-field
                  v-model="caloriesToBurn"
                  label="Calories"
                  variant="underlined"
                  type="number"
                  :rules="caloriesToBurnRules"
                  :error-messages="caloriesToBurnErrors"
              ></v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" @click="generateTrainingSession()">Confirm</v-btn>
            <v-btn @click="closeGenerateTrainingSessionDialog()" color="red">Cancel</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-dialog
          v-model="previewTrainingSessionDialog"
          transition="dialog-bottom-transition"
          fullscreen
      >
        <v-card>
          <v-toolbar>
            <v-btn
                icon="mdi-chevron-left"
                @click="closePreviewTrainingSessionDialog"
            ></v-btn>

            <v-toolbar-title>{{ selectedMuscleGroup.name }}</v-toolbar-title>
            <v-card-subtitle>{{ 'Estimated Time: ' + estimatedTime }}</v-card-subtitle>
          </v-toolbar>

          <v-card-title>
            Exercises ({{ trainingSession.exercises.length + trainingSession.warmups.length + trainingSession.stretches.length }})
          </v-card-title>
          <v-card-text>
            <v-card-item v-for="warmup in trainingSession.warmups">
              <v-card-title>
                {{ warmup.name }}
              </v-card-title>
              <v-card-subtitle>
                {{ warmup.repetitions > 1 ? 'x ' + warmup.repetitions : this.formatTime(warmup.duration) }}
              </v-card-subtitle>
            </v-card-item>
            <v-card-item v-for="exercise in trainingSession.exercises">
              <v-card-title>
                {{ exercise.name }}
              </v-card-title>
              <v-card-subtitle>
                {{ exercise.repetitions > 1 ? 'x ' + exercise.repetitions : this.formatTime(exercise.duration) }}
              </v-card-subtitle>
            </v-card-item>
            <v-card-item v-for="stretch in trainingSession.stretches">
              <v-card-title>
                {{ stretch.name }}
              </v-card-title>
              <v-card-subtitle>
                {{ stretch.repetitions > 1 ? 'x ' + stretch.repetitions : this.formatTime(stretch.duration) }}
              </v-card-subtitle>
            </v-card-item>
<!--            two empty items to prevent bottom navigation from overlapping-->
            <v-card-item>
            </v-card-item>
            <v-card-item>
            </v-card-item>
          </v-card-text>

          <v-bottom-navigation
              bg-color="#424242"
          >
            <v-btn
                class="bg-orange-darken-3"
                @click="generateTrainingSession()"
            >
              <v-icon>mdi-refresh</v-icon>
              Refresh Session
            </v-btn>

            <v-btn
                class="bg-green"
                @click="storeTrainingSession()"
            >
              <v-icon>mdi-play</v-icon>
              Start Session
            </v-btn>
          </v-bottom-navigation>
        </v-card>
      </v-dialog>
    </v-row>
  </v-container>
    <BottomNav/>
</template>

<script>
import axios from "axios";
import BottomNav from "@/components/BottomNav.vue";

export default {
    components: { BottomNav },

  data() {
    return {
      muscleGroups: [],
      estimatedTime: '',
      trainingSession: {
        estimatedTime: 0,
        exercises: [],
        warmups: [],
        stretches: [],
      },
      // exercises: [],
      BACKEND_URL: import.meta.env.VITE_BACKEND_URL,
      generateTrainingSessionDialog: false,
      previewTrainingSessionDialog: false,
      caloriesToBurn: null,
      caloriesToBurnRules: [
        v => !!v || 'Calories to burn is required',
      ],
      caloriesToBurnErrors: '',
      selectedMuscleGroup: {
        name: '',
        imagePath: '',
      },
    };
  },

  watch:{
    generateTrainingSessionDialog: function() {
      if (!this.generateTrainingSessionDialog) {
        this.caloriesToBurn = null
      }
    }
  },

  setup() {

  },

  mounted() {
    this.loadMuscleGroups();
    axios.defaults.withCredentials = true;
  },

  methods: {
    gotToYourWorkouts() {
      this.$router.push("/trainingSessions");
    },
    async loadMuscleGroups() {
      const url = import.meta.env.VITE_BACKEND_URL + '/api/muscleGroups';
      axios.get(url).then((response) => {
        this.muscleGroups = response.data;
      })
    },

    async generateTrainingSession() {
      const url = import.meta.env.VITE_BACKEND_URL + '/api/previewTrainingSessions';

      axios.post(url, {
          calories: this.caloriesToBurn,
          muscleGroupId: this.selectedMuscleGroup.id,
      }).then((response) => {
        this.trainingSession = response.data;
        this.estimatedTime = this.formatTime(this.trainingSession.estimatedTime);
        this.openPreviewTrainingSessionDialog();
      })
    },

    async storeTrainingSession() {
      const url = import.meta.env.VITE_BACKEND_URL + '/api/trainingSessions';
      let exercisesIds = this.trainingSession.warmups.map((warmup) => warmup.id);
      exercisesIds = exercisesIds.concat(this.trainingSession.exercises.map((exercise) => exercise.id));
      exercisesIds = exercisesIds.concat(this.trainingSession.stretches.map((stretch) => stretch.id));
      const trainingSessionBody = {
        calories: this.caloriesToBurn,
        muscleGroupId: this.selectedMuscleGroup.id,
        exercises: exercisesIds
      };

      axios.post(url, trainingSessionBody).then((response) => {
        this.$router.push("/trainingSession/" + response.data.id);
      });
    },

    formatTime(seconds) {
      let minutes = Math.floor(seconds / 60); // Finds the whole minutes
      let remainingSeconds = seconds % 60; // Finds the remaining seconds

      // Formats the minutes and seconds to always display two digits
      let formattedMinutes = String(minutes).padStart(2, '0');
      let formattedSeconds = String(remainingSeconds).padStart(2, '0');

      return `${formattedMinutes}:${formattedSeconds}`;
    },

    openPreviewTrainingSessionDialog() {
      this.previewTrainingSessionDialog = true;
    },

    closePreviewTrainingSessionDialog() {
      this.previewTrainingSessionDialog = false;
    },

    openGenerateTrainingSessionDialog(muscleGroup) {
      this.generateTrainingSessionDialog = true;
      this.selectedMuscleGroup = muscleGroup;
    },

    closeGenerateTrainingSessionDialog() {
      this.generateTrainingSessionDialog = false;
    }
  },

};
</script>
