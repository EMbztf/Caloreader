<template>
  <v-container>
    <v-row justify="space-around">
      <v-card width="500" class="elevation-10 ma-4">
        <v-toolbar title="Workouts">
        </v-toolbar>
        <v-card-text>
          <v-container v-for="trainingSession in trainingSessions">
            <v-card-item :prepend-avatar="BACKEND_URL + '/api/images/' + trainingSession.imagePath" @click="openGenerateTrainingSessionDialog()">
              <v-card-title>{{ trainingSession.name }}</v-card-title>
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
            Change Username
          </v-card-title>
          <v-card-text>
            <v-form @submit.prevent="" ref="generateTrainingSessionForm">
              <v-text-field
                  v-model="caloriesToBurn"
                  label="Calories to burn"
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
    </v-row>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      trainingSessions: [],
      BACKEND_URL: import.meta.env.VITE_BACKEND_URL,
      generateTrainingSessionDialog: false,
      caloriesToBurn: null,
      caloriesToBurnRules: [
        v => !!v || 'Calories to burn is required',
      ],
      caloriesToBurnErrors: '',
    };
  },

  setup() {

  },

  mounted() {
    this.loadMuscleGroups();
  },

  methods: {
    async loadMuscleGroups() {
      const url = import.meta.env.VITE_BACKEND_URL + '/api/muscleGroups';
      axios.get(url).then((response) => {
        this.trainingSessions = response.data;
      })
    },

    async generateTrainingSession() {
      // const url = import.meta.env.VITE_BACKEND_URL + '/api/trainingSessions';
      // axios.post(url, {
      //   caloriesToBurn: this.caloriesToBurn,
      // }).then((response) => {
      //   this.trainingSessions = response.data;
      // })
    },

    openGenerateTrainingSessionDialog() {
      console.log("opening dialog");
      this.generateTrainingSessionDialog = true;
    },

    closeGenerateTrainingSessionDialog() {
      this.generateTrainingSessionDialog = false;
      this.caloriesToBurn = null;
    }
  },

};
</script>
