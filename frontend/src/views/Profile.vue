<template>
    <v-container>
        <v-row justify="space-around">
            <v-card width="500" class="elevation-10">
                <v-toolbar title="Profile">
                </v-toolbar>
                <v-card-text>
                    <v-text-field
                        v-model="user.username"
                        label="Username"
                        variant="underlined"
                        readonly
                    >
                    <template v-slot:append>
                        <v-btn icon>
                            <v-icon>mdi-pencil</v-icon>
                            <v-dialog
                            activator="parent"
                            v-model="changeUsernameDialog"
                            width="500"
                            >
                                <v-card>
                                <v-card-title>
                                    Change Username
                                </v-card-title>
                                <v-card-text>
                                    <v-form @submit.prevent="" ref="editUsernameForm">
                                        <v-text-field
                                            v-model="newUsername"
                                            label="New Username"
                                            variant="underlined"
                                            :rules="usernameRules"
                                            :error-messages="newUsernameError"
                                        ></v-text-field>
                                        <v-text-field
                                            v-model="changeUsernamePassword"
                                            label="Password"
                                            variant="underlined"
                                            type="password"
                                            :rules="passwordRules"
                                            :error-messages="changeUsernamePasswordError"
                                        ></v-text-field>
                                    </v-form>
                                </v-card-text>
                                <v-card-actions>
                                    <v-btn color="primary" @click="updateUsername()" >Confirm</v-btn>
                                    <v-btn @click="closeUsernameDialog()" color="red">Cancel</v-btn>
                                </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-btn>
                    </template>
                    </v-text-field>
                    <v-text-field
                        v-model="user.password"
                        label="Password"
                        type="password"
                        variant="underlined"
                        readonly
                    >
                    <template v-slot:append>
                        <v-btn icon>
                            <v-icon>mdi-pencil</v-icon>
                            <v-dialog
                            activator="parent"
                            v-model="changePasswordDialog"
                            width="500"
                            >
                                <v-card>
                                <v-card-title>
                                    Change Password
                                </v-card-title>
                                <v-card-text>
                                    <v-form @submit.prevent="" ref="editPasswordForm">
                                        <v-text-field
                                            v-model="oldPassword"
                                            label="Old Password"
                                            variant="underlined"
                                            type="password"
                                            :rules="oldPasswordRules"
                                            :error-messages="oldPasswordErrors"
                                        ></v-text-field>
                                        <v-text-field
                                            v-model="newPassword"
                                            label="Password"
                                            variant="underlined"
                                            type="password"
                                            :rules="passwordRules"
                                            :error-messages="passwordErrors"
                                        ></v-text-field>
                                        <v-text-field
                                            v-model="user.retypedPassword"
                                            label="Retype Password"
                                            variant="underlined"
                                            type="password"
                                            :rules="retypedPasswordRules"
                                        ></v-text-field>
                                    </v-form>
                                </v-card-text>
                                <v-card-actions>
                                    <v-btn color="primary" @click="updatePassword()" >Confirm</v-btn>
                                    <v-btn @click="closePasswordDialog()" color="red">Cancel</v-btn>
                                </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-btn>
                    </template>
                    </v-text-field>
                    <v-text-field
                        v-model="user.aboutMe"
                        label="About me"
                        variant="underlined"
                        :rules="aboutMeRules"
                        :error-messages="aboutMeErrors"
                        readonly
                    >
                    <template v-slot:append>
                        <v-btn icon>
                            <v-icon>mdi-pencil</v-icon>
                            <v-dialog
                            activator="parent"
                            v-model="changeAboutMeDialog"
                            width="500"
                            >
                                <v-card>
                                <v-card-title>
                                    Change About Me
                                </v-card-title>
                                <v-card-text>
                                    <v-form @submit.prevent="" ref="aboutMeForm">
                                        <v-text-field
                                            v-model="aboutMe"
                                            label="About Me"
                                            variant="underlined"
                                            :rules="aboutMeRules"
                                            :error-messages="aboutMeErrors"
                                        ></v-text-field>
                                    </v-form>
                                </v-card-text>
                                <v-card-actions>
                                    <v-btn color="primary" @click="updateAboutMe()" >Confirm</v-btn>
                                    <v-btn @click="closeAboutMeDialog()" color="red">Cancel</v-btn>
                                </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-btn>
                    </template>
                    </v-text-field>
                    <!-- <v-avatar size="100px">
                        <v-img :src="user.avatar" width="200"></v-img>
                    </v-avatar> -->
                    <v-card-item :prepend-avatar="user.avatar">
                    <template v-slot:append>
                        <v-btn icon>
                            <v-icon>mdi-pencil</v-icon>
                            <v-dialog
                            activator="parent"
                            v-model="changeProfilePictureDialog"
                            width="500"
                            >
                                <v-card>
                                <v-card-title>
                                    Change Profile Picture
                                </v-card-title>
                                <v-card-text>
                                    <v-form @submit.prevent="" ref="profilePictureForm">
                                        <v-file-input
                                            v-model="newAvatar"
                                            label="Avatar"
                                            variant="underlined"
                                            :rules="avatarRules"
                                            :error-messages="profilePictureErrors"
                                        ></v-file-input>
                                    </v-form>
                                </v-card-text>
                                <v-card-actions>
                                    <v-btn color="primary" @click="updateProfilePicture()" >Confirm</v-btn>
                                    <v-btn @click="closeProfilePictureDialog()" color="red">Cancel</v-btn>
                                </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-btn>
                    </template>
                    </v-card-item>
                </v-card-text>
            </v-card>
        </v-row>
    </v-container>
</template>

<script>
import axios from 'axios';
import { useUserStore } from "../stores/users";
import { storeToRefs } from "pinia";
import { useErrorStore } from '../stores/errors';

export default {
data() {
return {
    user: {
        id: null,
        username: '',
        aboutMe: '',
        avatar: '',
        password: 'test1234',
        retypedPassword: '',
    },
    aboutMe: '',
    newUsername: '',
    changeUsernamePassword: '',
    oldPassword: '',
    newPassword: '',
    newAvatar: '',
    usernameRules: [
        v => !!v || 'Username is required',
        v => (v && v.length <= 30) || 'Username must be less than 30 characters',
    ],
    oldPasswordRules: [
        v => !!v || 'Old Password is required',
    ],
    passwordRules: [
        v => !!v || 'Password is required',
        v => (v && v.length >= 8) || 'Password must be at least 8 characters long',
        v => (v && v.length <= 30) || 'Password must be less than 40 characters',
    ],
    retypedPasswordRules: [
        v => !!v || 'Please retype the password',
        v => (v && v === this.newPassword) || 'Passwords must match',
    ],
    aboutMeRules: [
        v => !!v || 'About Me is required',
    ],
    avatarRules: [
        v => !!v || 'Select your avatar',
    ],
    newUsernameError: '',
    changeUsernamePasswordError: '',
    oldPasswordErrors: '',
    passwordErrors: '',
    aboutMeErrors: '',
    profilePictureErrors: '',
    changeUsernameDialog: false,
    changePasswordDialog: false,
    changeAboutMeDialog: false,
    changeProfilePictureDialog: false,
};
},

setup() {
    return {};
},

mounted() {
    this.loadUser();
},


methods: {
    clearErrors() {
        this.newUsernameError = '';
        this.changeUsernamePasswordError = '';
        this.oldPasswordErrors = '';
        this.passwordErrors = '';
        this.aboutMeErrors = '';
        this.profilePictureErrors = '';
    },

    showSnackbarError(error) {
        const errors = useErrorStore();
        errors.showError(error);
    },

    getAuthorizationHeader() {
        const {
            jwt,
        } = storeToRefs(useUserStore());
        return { 'Authorization': 'Bearer ' + jwt.value };
    },

    loadUser() {
        const userStore = useUserStore();
        const { 
            id,
            username,
            aboutMe,
            avatar,
        } = storeToRefs(userStore);
        
        this.user.id = id;
        this.user.username = username;
        this.user.aboutMe = aboutMe.value;
        this.aboutMe = aboutMe.value;
        this.user.avatar = avatar.value;
    },

    async updateUsername() {
        this.clearErrors();
        await this.$refs.editUsernameForm.validate()
        if (this.$refs.editUsernameForm.isValid) {
            const formData = new FormData();
            formData.append('userId', this.user.id);
            formData.append('username', this.newUsername);
            formData.append('password', this.changeUsernamePassword);
            const url = import.meta.env.VITE_BACKEND_URL + '/users/username';

            try {
                const response = await axios.post(
                    url, 
                    formData,
                    {
                        headers: this.getAuthorizationHeader(),
                });

                if(response.status === 200) {
                    const data = response.data; 

                    const user = storeToRefs( useUserStore() );

                    user.username.value = data.username;

                    this.closeUsernameDialog();
                }
            } catch (error) {
                if(error.response.status === 400) {
                    const errors = JSON.parse(error.response.data.message);
                    if(errors.username) {
                        this.newUsernameError = errors.username;
                    }
                    if(errors.password) {
                        this.changeUsernamePasswordError = errors.password;
                    }
                } else {
                    this.showSnackbarError(error);
                }
            }
        }
    },

    async updatePassword() {
        this.clearErrors();
        await this.$refs.editPasswordForm.validate()
        if (this.$refs.editPasswordForm.isValid) {
            const formData = new FormData();
            formData.append('userId', this.user.id);
            formData.append('oldPassword', this.oldPassword);
            formData.append('password', this.newPassword);
            const url = import.meta.env.VITE_BACKEND_URL + '/users/password';

            try {
                const response = await axios.post(
                    url, 
                    formData,
                    {
                        headers: this.getAuthorizationHeader(),
                });

                if(response.status === 200) {
                    this.logout();
                }
            } catch (error) {
                if(error.response.status === 400) {
                    const errors = JSON.parse(error.response.data.message);
                    if(errors.oldPassword) {
                        this.oldPasswordErrors = errors.oldPassword;
                    }
                    if(errors.password) {
                        this.passwordErrors = errors.password;
                    }
                } else {
                    this.showSnackbarError(error);
                }
            }
        }
    },

    async updateAboutMe() {
        this.clearErrors();
        await this.$refs.aboutMeForm.validate()
        if (this.$refs.aboutMeForm.isValid) {
            const formData = new FormData();
            formData.append('userId', this.user.id);
            formData.append('aboutMe', this.aboutMe);
            const url = import.meta.env.VITE_BACKEND_URL + '/users/about_me';

            try {
                const response = await axios.post(
                    url, 
                    formData,
                    {
                        headers: this.getAuthorizationHeader(),
                });

                if(response.status === 200) {
                    const data = response.data; 

                    const user = storeToRefs( useUserStore() );

                    user.aboutMe.value = data.about_me;

                    this.closeAboutMeDialog();
                    this.loadUser();
                }
            } catch (error) {
                if(error.response.status === 400) {
                    const errors = JSON.parse(error.response.data.message);
                    if(errors.aboutMe) {
                        this.aboutMeErrors = errors.aboutMe;
                    }
                } else {
                    this.showSnackbarError(error);
                }
            }
        }
    },

    async updateProfilePicture() {
        this.clearErrors();
        await this.$refs.profilePictureForm.validate()
        if (this.$refs.profilePictureForm.isValid) {
            const formData = new FormData();
            formData.append('userId', this.user.id);
            formData.append('profilePicture', this.newAvatar[0]);
            const url = import.meta.env.VITE_BACKEND_URL + '/users/picture';

            try {
                const response = await axios.post(
                    url, 
                    formData,
                    {
                        headers: this.getAuthorizationHeader(),
                });

                if(response.status === 200) {
                    const data = response.data; 

                    const user = storeToRefs( useUserStore() );

                    user.avatar.value = data.avatar;

                    this.closeProfilePictureDialog();
                    this.loadUser();
                }
            } catch (error) {
                if(error.response.status === 400) {
                    const errors = JSON.parse(error.response.data.message);
                    if(errors.avatar) {
                        this.profilePictureErrors = errors.avatar;
                    }
                } else {
                    this.showSnackbarError(error);
                }
            }
        }
    },

    logout() {
        const user = useUserStore();
        user.reset();
    },

    closeUsernameDialog() {
        this.changeUsernameDialog = false;
        this.newUsername = '';
        this.changeUsernamePassword = '';
    },

    closeAboutMeDialog() {
        this.changeAboutMeDialog = false;
        this.aboutMe = '';
    },

    closePasswordDialog() {
        this.changePasswordDialog = false;
        this.oldPassword = '';
        this.user.password = '';
        this.user.retypedPassword = '';
    },

    closeProfilePictureDialog() {
        this.changeProfilePictureDialog = false;
        this.newAvatar = '';
    }
},
};
</script>