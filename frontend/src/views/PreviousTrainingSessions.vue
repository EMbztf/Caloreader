<template>
    <v-container>
        <v-row justify="space-around">
            <v-card width="500" class="elevation-10 ma-4">
                <v-toolbar title="Your Workouts">
                </v-toolbar>
                <v-card-text>
                    <v-container v-for="trainingSession in trainingSessions">
                        <v-card-item>
                            <v-row align="center" justify="space-between">
                                <v-col cols="auto">
                                    <v-card-title>{{ trainingSession.name }}</v-card-title>
                                    <v-card-subtitle>{{ formatDate(trainingSession.date) }}</v-card-subtitle>
                                </v-col>

                                <v-col cols="auto">
                                    <v-btn small color="red"
                                           @click="openDeleteTrainingSessionConfirmationDialog(trainingSession.id)"
                                    >
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                    <v-btn small color="green"
                                           @click="openPreviewTrainingSessionDialog(trainingSession)"
                                    >
                                        <v-icon>mdi-play</v-icon>
                                    </v-btn>
                                </v-col>
                            </v-row>
                        </v-card-item>
                        <v-divider></v-divider>
                    </v-container>
                </v-card-text>
            </v-card>
            <v-dialog
                v-model="deleteTrainingSessionConfirmationDialog"
                width="500"
            >
                <v-card>
                    <v-card-title>
                        Calories to burn
                    </v-card-title>
                    <v-card-text>
                        Do you really want to delete this training session?
                    </v-card-text>
                    <v-card-actions>
                        <v-btn color="primary" @click="deleteTrainingSession()">Confirm</v-btn>
                        <v-btn @click="closeDeleteTrainingSessionConfirmationDialog()" color="red">Cancel</v-btn>
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

                        <v-toolbar-title>{{ trainingSessionToPreview.name }}</v-toolbar-title>
                        <v-card-subtitle>{{ 'Time: ' + this.formatTime(trainingSessionToPreview.estimatedDuration) }}</v-card-subtitle>
                    </v-toolbar>

                    <v-card-title>
                        Exercises ({{ trainingSessionToPreview.exercises.length }})
                    </v-card-title>
                    <v-card-text>
                        <v-card-item v-for="exercise in trainingSessionToPreview.exercises">
                            <v-card-title>
                                {{ exercise.name }}
                            </v-card-title>
                            <v-card-subtitle>
                                {{ exercise.repetitions > 1 ? 'x ' + exercise.repetitions : this.formatTime(exercise.duration) }}
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
                            @click="goToTrainingSession()"
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
            trainingSessions: [],
            trainingSessionToPreview: {
                name: '',
                date: '',
                exercises: [],
                warmups: [],
                stretches: [],
                estimatedDuration: 0
            },
            trainingSessionToDelete: 0,
            deleteTrainingSessionConfirmationDialog: false,
            previewTrainingSessionDialog: false,
            selectedMuscleGroup: null,
            estimatedTime: 0
        };
    },

    setup() {

    },

    mounted() {
        axios.defaults.withCredentials = true;
        this.loadTrainingSessions();
    },

    methods: {
        loadTrainingSessions() {
            const url = import.meta.env.VITE_BACKEND_URL + '/api/trainingSessions';
            axios.get(url).then((response) => {
                this.trainingSessions = response.data;
            });
        },

        deleteTrainingSession() {
            const url = import.meta.env.VITE_BACKEND_URL + '/api/trainingSessions/' + this.trainingSessionToDelete;
            axios.delete(url).then(() => {
                this.loadTrainingSessions();
                this.closeDeleteTrainingSessionConfirmationDialog();
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

        openDeleteTrainingSessionConfirmationDialog(trainingSessionId) {
            this.trainingSessionToDelete = trainingSessionId;
            this.deleteTrainingSessionConfirmationDialog = true;
        },

        closeDeleteTrainingSessionConfirmationDialog() {
            this.deleteTrainingSessionConfirmationDialog = false;
        },

        openPreviewTrainingSessionDialog(trainingSession) {
            this.trainingSessionToPreview = trainingSession;
            console.log(this.trainingSessionToPreview);
            this.previewTrainingSessionDialog = true;
        },

        closePreviewTrainingSessionDialog() {
            this.previewTrainingSessionDialog = false;
            this.selectedMuscleGroup = null;
            this.estimatedTime = 0;
        },

    },

};
</script>

