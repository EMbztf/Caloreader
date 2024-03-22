<template>
  <v-container>
    <v-row justify="space-around">
      <v-card width="500" class="elevation-10 ma-4">
        <v-toolbar title="Your Sportsplans">
        </v-toolbar>
        <v-card-text>
          <v-container v-for="sportsplan in sportsplans">
            <v-card-item>
              <v-row align="center" justify="space-between">
                <v-col cols="auto">
                  <v-card-title>{{ sportsplan.name }}</v-card-title>
                  <v-card-subtitle>{{ formatDate(sportsplan.date) }}</v-card-subtitle>
                </v-col>

                <v-col cols="auto">
                  <v-btn small color="red"
                         @click="openDeleteSportsplanConfirmationDialog(sportsplan.id)"
                  >
                    <v-icon>mdi-delete</v-icon>
                  </v-btn>
                  <v-btn small color="blue"
                         @click="openPreviewSportsplanDialog(sportsplan)"
                  >
                    <v-icon>mdi-eye</v-icon>
                  </v-btn>
                </v-col>
              </v-row>
            </v-card-item>
            <v-divider></v-divider>
          </v-container>
        </v-card-text>
      </v-card>
      <v-dialog
          v-model="deleteSportsplanConfirmationDialog"
          width="500"
      >
        <v-card>
          <v-card-title>
            Delete Confirmation
          </v-card-title>
          <v-card-text>
            Do you really want to delete this sportsplan?
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" @click="deleteSportsplan()">Confirm</v-btn>
            <v-btn @click="closeDeleteSportsplanConfirmationDialog()" color="red">Cancel</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-dialog
          v-model="previewSportsplanDialog"
          transition="dialog-bottom-transition"
          fullscreen
      >
        <v-card>
          <v-toolbar>
            <v-btn
                icon="mdi-chevron-left"
                @click="closePreviewSportsplanDialog"
            ></v-btn>

            <v-toolbar-title>{{ sportsplanToPreview.name }}</v-toolbar-title>
            <v-card-subtitle>{{ 'Time: ' + this.formatTime(sportsplanToPreview.estimatedDuration) }}</v-card-subtitle>
          </v-toolbar>

          <v-card-title>
            Sports ({{ sportsplanToPreview.sports.length }})
          </v-card-title>
          <v-card-text>
            <v-card-item v-for="sport in sportsplanToPreview.sports">
              <v-card-title>
                {{ sport.name }}
              </v-card-title>
              <v-card-subtitle>
                {{ this.formatTime(sport.duration) }}
              </v-card-subtitle>
            </v-card-item>
            <v-card-item>
            </v-card-item>
            <v-card-item>
            </v-card-item>
          </v-card-text>
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
      sportsplans: [],
      // trainingSessions: [],
      sportsplanToPreview: {
        name: '',
        date: '',
        sports: [],
      },
      // trainingSessionToPreview: {
      //   name: '',
      //   date: '',
      //   exercises: [],
      //   warmups: [],
      //   stretches: [],
      //   estimatedDuration: 0
      // },
      sportsplanToDelete: 0,
      // trainingSessionToDelete: 0,
      deleteSportsplanConfirmationDialog: false,
      // deleteTrainingSessionConfirmationDialog: false,
      previewSportsplanDialog: false,
      // previewTrainingSessionDialog: false,
      // selectedMuscleGroup: null,
      estimatedTime: 0
    };
  },

  setup() {

  },

  mounted() {
    axios.defaults.withCredentials = true;
    this.loadSportsplans();
  },

  methods: {
    loadSportsplans() {
      const url = import.meta.env.VITE_BACKEND_URL + '/api/sportsplans';
      axios.get(url).then((response) => {
        this.sportsplans = response.data;
      });
    },
    // loadTrainingSessions() {
    //   const url = import.meta.env.VITE_BACKEND_URL + '/api/trainingSessions';
    //   axios.get(url).then((response) => {
    //     this.trainingSessions = response.data;
    //   });
    // },

    deleteSportsplan() {
      const url = import.meta.env.VITE_BACKEND_URL + '/api/sportsplans/' + this.sportsplanToDelete;
      axios.delete(url).then(() => {
        this.loadSportsplans();
        this.closeDeleteSportsplanConfirmationDialog();
      });
    },
    formatDate(datetime) {
      const date = new Date(datetime);

      const day = String(date.getDate()).padStart(2, '0');
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const year = date.getFullYear();

      return `${day}.${month}.${year}`;
    },

    formatTime(seconds) {
      let minutes = Math.floor(seconds / 60); // Finds the whole minutes
      let remainingSeconds = seconds % 60; // Finds the remaining seconds

      // Formats the minutes and seconds to always display two digits
      let formattedMinutes = String(minutes).padStart(2, '0');
      let formattedSeconds = String(remainingSeconds).padStart(2, '0');

      return `${formattedMinutes}:${formattedSeconds}`;
    },

    goToTrainingSession() {
      this.$router.push("/trainingSession/" + this.trainingSessionToPreview.id);
    },

    openDeleteSportsplanConfirmationDialog(sportsplanId) {
      this.sportsplanToDelete = sportsplanId;
      this.deleteSportsplanConfirmationDialog = true;
    },

    // openDeleteTrainingSessionConfirmationDialog(trainingSessionId) {
    //   this.trainingSessionToDelete = trainingSessionId;
    //   this.deleteTrainingSessionConfirmationDialog = true;
    // },

    closeDeleteSportsplanConfirmationDialog() {
      this.deleteSportsplanConfirmationDialog = false;
    },

    // closeDeleteTrainingSessionConfirmationDialog() {
    //   this.deleteTrainingSessionConfirmationDialog = false;
    // },

    openPreviewSportsplanDialog(sportsplan) {
      this.sportsplanToPreview = sportsplan;
      this.previewSportsplanDialog = true;
    },
    openPreviewTrainingSessionDialog(trainingSession) {
      this.trainingSessionToPreview = trainingSession;
      this.previewTrainingSessionDialog = true;
    },

    closePreviewSportsplanDialog() {
      this.previewSportsplanDialog = false;
    },

    closePreviewTrainingSessionDialog() {
      this.previewTrainingSessionDialog = false;
      this.selectedMuscleGroup = null;
      this.estimatedTime = 0;
    },

  },

};
</script>

