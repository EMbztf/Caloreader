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
                        <v-text-field
                            v-model="user.passwordRepeat"
                            label="Repeat Password"
                            type="password"
                            variant="underlined"
                            :rules="passwordRepeatRules"
                            required
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
                            type="number"
                            :rules="heightRule"
                            :error-messages="heightErrors"
                            required
                        ></v-text-field>
                        <v-text-field
                            type="number"
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
                            type="number"
                            :rules="ageRule"
                            :error-messages="ageErrors"
                            required
                        ></v-text-field>
                        <v-card-actions>
                            Already have an account?&nbsp
                            <router-link to="/login">Login</router-link>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="signUp(user)" type="submit">Signup
                            </v-btn>
                        </v-card-actions>
                    </v-form>
                </v-card-text>
            </v-card>
        </v-row>
    </v-container>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            user: {
                username: '',
                password: '',
                passwordRepeat: '',
                sex: '',
                height: '',
                weight: '',
                age: null,
            },
            usernameRules: [
                v => !!v || 'Username is required',
            ],
            passwordRules: [
                v => !!v || 'Password is required',
            ],
            passwordRepeatRules: [
                v => !!v || 'Password repeat is required',
                v => v === this.user.password || 'Passwords must match',
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
            this.sexErrors = '';
            this.heightErrors = '';
            this.weightErrors = '';
            this.ageErrors = '';
        },

        async signUp() {
            this.clearErrors();
            await this.$refs.singUpForm.validate()
            if (this.$refs.singUpForm.isValid) {
                const body = {
                    username: this.user.username,
                    password: this.user.password,
                    sex: this.user.sex.toLowerCase(),
                    height: this.user.height,
                    weight: this.user.weight,
                    age: this.user.age,
                }
                console.log(body);
                const url = import.meta.env.VITE_BACKEND_URL + '/api/auth/signup';

                try {
                    axios.post(url, body).then((response) => {
                        if(response.status === 200) {
                            this.$router.push('/login');
                        }
                    })

                    // if (response.status === 201) {
                    //     this.$router.push('/login');
                    // }
                } catch (error) {
                    // if (error.response.status === 400) {
                    //     const errors = JSON.parse(error.response.data.message);
                    //     if (errors.username) {
                    //         this.usernameErrors = errors.username;
                    //     }
                    //     if (errors.password) {
                    //         this.passwordErrors = errors.password;
                    //     }
                    //     if (errors.aboutMe) {
                    //         this.aboutMeErrors = errors.aboutMe;
                    //     }
                    //     if (errors.profilePicture) {
                    //         this.profilePictureErrors = errors.profilePicture;
                    //     }
                    // } else {
                    //     this.showSnackbarError(error);
                    // }
                }
            }
        },
    },
};
</script>
