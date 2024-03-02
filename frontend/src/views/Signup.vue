<template>
    <v-container>
        <v-row justify="space-around">
            <v-card width="500" class="elevation-10">
                <v-toolbar title="Sign Up">
                </v-toolbar>
                <v-card-text>
                    <v-form @submit.prevent="" ref="singUpForm">
                        <v-text-field
                            v-model="user.username"
                            label="Username"
                            variant="underlined"
                            :rules="usernameRules"
                            :error-messages="usernameErrors"
                        ></v-text-field>
                        <v-text-field
                            v-model="user.password"
                            label="Password"
                            type="password"
                            variant="underlined"
                            :rules="passwordRules"
                            :error-messages="passwordErrors"
                        ></v-text-field>
                        <v-select
                            prepend-icon="mdi-gender-transgender"
                            label="Sex"
                            v-model="user.sex"
                            variant="underlined"
                            :rules="sexRules"
                            :items="['Male', 'Female']"
                            :error-messages="sexErrors"
                            required
                        ></v-select>
                       <v-text-field
                           v-model="user.height"
                           label="Height (cm)"
                           variant="underlined"
                           :rules="heightRule"
                           :error-messages="heightErrors"
                           required
                       ></v-text-field>
                       <v-text-field
                           v-model="user.weight"
                           label="Weight (kg)"
                           variant="underlined"
                           :rules="weightRule"
                           :error-messages="weightErrors"
                           required
                       ></v-text-field>
                       <v-text-field
                           v-model="user.age"
                           label="Age"
                           variant="underlined"
                           :rules="ageRule"
                           :error-messages="ageErrors"
                           required
                       ></v-text-field>
                        <v-card-actions>
                            Already have an account?&nbsp
                            <router-link to="/login">Login</router-link>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="signUp(user)" type="submit">Signup</v-btn>
                        </v-card-actions>
                    </v-form>
                </v-card-text>
            </v-card>
        </v-row>
    </v-container>
</template>

<script>
import { ref } from "vue";
import { useErrorStore } from "../stores/errors";
import axios from 'axios';

export default {
data() {
return {
    user: {
      username: '',
      password: '',
      sex: '',
      height: '',
      weight: '',
      age: -1,
    },
    usernameRules: [
      v => !!v || 'Username is required',
    ],
    passwordRules: [
      v => !!v || 'Password is required',
    ],
    sexRules: [
      v => !!v || 'Sex is required',
    ],
    heightRule: [
      v => !!v || 'Height is required',
      v => v > 0 || 'Height must be greater than 0',
      v => v < 300 || 'Height must be less than 300',
    ],
    weightRule: [
      v => !!v || 'Weight is required',
      v => v > 0 || 'Weight must be greater than 0',
      v => v < 300 || 'Weight must be less than 300',
    ],
    ageRule: [
      v => !!v || 'Age is required',
      v => v > 0 || 'Age must be greater than 0',
      v => v < 150 || 'Age must be less than 150',
    ],

    usernameErrors: '',
    passwordErrors: '',
    sexErrors: '',
    heightErrors: '',
    weightErrors: '',
    ageErrors: '',
};
},

setup() {

    return {};
  },

  methods: {
    clearErrors() {
        this.usernameErrors = '';
        this.passwordErrors = '';
        this.aboutMeErrors = '';
        this.profilePictureErrors = '';
    },

    async signUp() {
        this.clearErrors();
        await this.$refs.singUpForm.validate()
        if (this.$refs.singUpForm.isValid) {
            const formData = new FormData();
            formData.append('username', this.user.username);
            formData.append('password', this.user.password);
            formData.append('aboutMe', this.user.aboutMe);
            formData.append('profilePicture', this.user.avatar[0]);
            const url = import.meta.env.VITE_BACKEND_URL + '/register';

            try {
                const response = await axios.post(
                    url,
                    formData,
                    {
                });

                if(response.status === 201) {
                    this.$router.push('/login');
                }
            } catch (error) {
                if(error.response.status === 400) {
                    const errors = JSON.parse(error.response.data.message);
                    if(errors.username) {
                        this.usernameErrors = errors.username;
                    }
                    if(errors.password) {
                        this.passwordErrors = errors.password;
                    }
                    if(errors.aboutMe) {
                        this.aboutMeErrors = errors.aboutMe;
                    }
                    if(errors.profilePicture) {
                        this.profilePictureErrors = errors.profilePicture;
                    }
                } else {
                    this.showSnackbarError(error);
                }
            }
        }
    },
  },
};
</script>
