<template>
    <v-bottom-navigation>
        <v-btn @click="goToWorkouts()">
            <v-icon>mdi-dumbbell</v-icon>
            <span>Workouts</span>
        </v-btn>

        <v-btn @click="goToSports()">
            <v-icon>mdi-gymnastics</v-icon>
            <span>Sports</span>
        </v-btn>
    </v-bottom-navigation>
</template>

<script>

export default {
    data() {
    },

    setup() {
    },


    mounted() {
    },

    methods: {

        goToWorkouts() {
            this.$router.push('/trainingSessionGenerator');
        },

        goToSports() {
            this.$router.push('/sports');
        }
    }
};
</script>
