<template>
  <v-container>
    <v-row justify="space-around">
      <v-card width="500" class="elevation-10 ma-4">
        <v-toolbar title="Sports">
          <v-btn color="primary" @click="goToYourSportPlans()">
            Your Sports Session
          </v-btn>
        </v-toolbar>
        <v-card-text>
          <v-container v-for="sport in sports">
            <v-card-item>
              <v-card-title>{{ sport.name }}</v-card-title>
              <template v-slot:prepend>
                <v-checkbox
                    v-model="sport.selected"
                    hide-details
                ></v-checkbox>
              </template>
            </v-card-item>
            <v-divider></v-divider>
          </v-container>
          <v-btn
              class="bg-green"
              :disabled="!isASportSelected"
              @click="openGenerateSportsSessionDialog()"
          >Generate Sports Session</v-btn>
        </v-card-text>
      </v-card>

      <v-dialog
          v-model="generateSportsSessionDialog"
          width="500"
      >
        <v-card>
          <v-card-title>
            Calories to burn
          </v-card-title>
          <v-card-text>
            <v-form @submit.prevent="" ref="generateSportsSessionForm">
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
            <v-btn color="primary" @click="generateSportsSession()">Confirm</v-btn>
            <v-btn @click="closeGenerateSportsSessionDialog()" color="red">Cancel</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-dialog
          v-model="previewSportsSessionDialog"
          transition="dialog-bottom-transition"
          fullscreen
      >
        <v-card>
          <v-toolbar>
            <v-btn
                icon="mdi-chevron-left"
                @click="closePreviewSportsSessionDialog"
            ></v-btn>

            <v-toolbar-title>{{ sportsSession.name }}</v-toolbar-title>
            <v-card-subtitle>{{ 'Estimated Time: ' + this.formatTime(this.sportsSession.estimatedTime) }}</v-card-subtitle>
          </v-toolbar>

          <v-card-title>
            Sports ({{ sportsSession.sports.length }})
          </v-card-title>
          <v-card-text>
            <v-card-item v-for="sport in sportsSession.sports">
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

          <v-bottom-navigation
              bg-color="#424242"
          >

            <v-btn
                class="bg-green"
                @click="storeSportsSession()"
            >
              <v-icon>mdi-play</v-icon>
              Save Session
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
      sports: [],
      muscleGroups: [],
      estimatedTime: '',
      sportsSession: {
        estimatedTime: 0,
        sports: [],
      },
      BACKEND_URL: import.meta.env.VITE_BACKEND_URL,
      generateSportsSessionDialog: false,
      previewSportsSessionDialog: false,
      caloriesToBurn: null,
      caloriesToBurnRules: [
        v => !!v || 'Calories to burn is required',
      ],
      caloriesToBurnErrors: '',
      // selectedMuscleGroup: {
      //   name: '',
      //   imagePath: '',
      // },
    };
  },

  computed: {
    isASportSelected() {
      return this.sports.filter(sport => sport.selected).length > 0
    }
  },

  watch:{
    generateSportsSessionDialog: function() {
      if (!this.generateSportsSessionDialog) {
        this.caloriesToBurn = null
      }
    }
  },

  setup() {

  },

  mounted() {
    axios.defaults.withCredentials = true;
    this.loadSports();
  },

  methods: {
    goToYourSportPlans() {
      this.$router.push("/sportplans");
    },

    async loadSports() {
      const url = import.meta.env.VITE_BACKEND_URL + '/api/sports';
      axios.get(url).then((response) => {
        this.sports = response.data;
      })
    },

    async generateSportsSession() {
      const url = import.meta.env.VITE_BACKEND_URL + '/api/previewSportsSession';
      const selectedSports = this.sports.filter(sport => sport.selected).map(sport => sport.id);
      const body = {
        calories: Number(this.caloriesToBurn),
        sports: selectedSports
      };

      axios.post(url, body).then((response) => {
        this.sportsSession = response.data;
        this.estimatedTime = this.formatTime(this.sportsSession.estimatedTime);
        this.openPreviewSportsSessionDialog();
      });
    },

    async storeSportsSession() {
      const url = import.meta.env.VITE_BACKEND_URL + '/api/sportsplan';
      const sportsSessionBody = {
        calories: this.caloriesToBurn,
        sports: this.sportsSession.sports.map((sport) => sport.id),
      };
      axios.post(url, sportsSessionBody).then((response) => {
        this.$router.push("/sportplans");
      });
    },

    formatTime(seconds) {
      let minutes = Math.floor(seconds / 60);
      let remainingSeconds = seconds % 60;

      let formattedMinutes = String(minutes).padStart(2, '0');
      let formattedSeconds = String(remainingSeconds).padStart(2, '0');

      return `${formattedMinutes}:${formattedSeconds}`;
    },

    openPreviewSportsSessionDialog() {
      this.previewSportsSessionDialog = true;
    },

    closePreviewSportsSessionDialog() {
      this.previewSportsSessionDialog = false;
    },

    openGenerateSportsSessionDialog() {
      this.generateSportsSessionDialog = true;
    },

    closeGenerateSportsSessionDialog() {
      this.generateSportsSessionDialog = false;
    },
  },

};
</script>
