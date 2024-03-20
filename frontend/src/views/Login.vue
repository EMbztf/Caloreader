<template>
    <v-container>
        <v-row justify="space-around">
            <v-card width="500" class="elevation-10">
                <v-toolbar title="Login">
                </v-toolbar>
                <v-card-text>
                    <v-form @submit.prevent="login" ref="loginForm">
                        <v-text-field
                            prepend-icon="mdi-account"
                            v-model="user.username"
                            label="Username"
                            variant="underlined"
                            required
                            :rules="usernameRules"
                            :error-messages="usernameErrors"
                        ></v-text-field>
                        <v-text-field
                            prepend-icon="mdi-lock"
                            v-model="user.password"
                            label="Password"
                            type="password"
                            variant="underlined"
                            required
                            :rules="passwordRules"
                            :error-messages="passwordErrors"
                        ></v-text-field>
                        <v-card-actions>
                            Don't have an account?&nbsp
                            <router-link to="/signup">Sign up</router-link>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" type="submit">Login</v-btn>
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
    },
    usernameRules: [
        v => !!v || 'Username is required',
    ],
    passwordRules: [
        v => !!v || 'Password is required',
    ],

    usernameErrors: '',
    passwordErrors: '',
};
},

setup() {

},


methods: {
    async login() {
        this.usernameErrors = '';
        this.passwordErrors = '';
        await this.$refs.loginForm.validate()
        if (this.$refs.loginForm.isValid) {
            const data = {
                username: this.user.username,
                password: this.user.password,
            };

            const url = import.meta.env.VITE_BACKEND_URL + '/api/auth/login';

            try {
                const response = await axios.post(
                    url,
                    data,
                    {
                        withCredentials: true
                });

                if(response.status === 200) {
                    this.$router.push('/trainingSessionGenerator');
                }
            } catch (error) {
                if(error.response.status === 401) {
                    this.passwordErrors = error.response.data.message
                    // if(errors.username) {
                    //     this.usernameErrors = errors.username;
                    // }
                    // if(errors.password) {
                    //     this.passwordErrors = errors.password;
                    // }
                } else {
                    this.showSnackbarError(error);
                }
            }
        }
    },
},

};
</script>
