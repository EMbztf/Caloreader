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
                        readonly="true"
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
                        v-model="placeholderPassword"
                        label="Password"
                        type="password"
                        variant="underlined"
                        readonly="true"
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
                                            v-model="password"
                                            label="Password"
                                            variant="underlined"
                                            type="password"
                                            :rules="passwordRules"
                                            :error-messages="passwordErrors"
                                        ></v-text-field>
                                        <v-text-field
                                            v-model="newPassword"
                                            label="New Password"
                                            variant="underlined"
                                            type="password"
                                            :rules="newPasswordRules"
                                            :error-messages="newPasswordErrors"
                                        ></v-text-field>
                                        <v-text-field
                                            v-model="retypedPassword"
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
                        v-model="user.sex"
                        label="Sex"
                        variant="underlined"
                        readonly="true"
                    >
                    <template v-slot:append>
                        <v-btn icon>
                            <v-icon>mdi-pencil</v-icon>
                            <v-dialog
                            activator="parent"
                            v-model="changeSexDialog"
                            width="500"
                            >
                                <v-card>
                                <v-card-title>
                                    Change Sex
                                </v-card-title>
                                <v-card-text>
                                    <v-form @submit.prevent="" ref="editSexForm">
                                        <v-select
                                            prepend-icon="mdi-gender-transgender"
                                            label="Sex"
                                            v-model="newSex"
                                            variant="underlined"
                                            :rules="sexRules"
                                            :items="['Male', 'Female']"
                                            :error-messages="sexErrors"
                                            required
                                        ></v-select>
                                    </v-form>
                                </v-card-text>
                                <v-card-actions>
                                    <v-btn color="primary" @click="updateSex()" >Confirm</v-btn>
                                    <v-btn @click="closeSexDialog()" color="red">Cancel</v-btn>
                                </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-btn>
                    </template>
                    </v-text-field>
                    <v-text-field
                        v-model="user.height"
                        label="Height (cm)"
                        variant="underlined"
                        readonly="true"
                    >
                    <template v-slot:append>
                        <v-btn icon>
                            <v-icon>mdi-pencil</v-icon>
                            <v-dialog
                            activator="parent"
                            v-model="changeHeightDialog"
                            width="500"
                            >
                                <v-card>
                                <v-card-title>
                                    Change Height
                                </v-card-title>
                                <v-card-text>
                                    <v-form @submit.prevent="" ref="editHeightForm">
                                        <v-text-field
                                            v-model="newHeight"
                                            label="Height (cm)"
                                            variant="underlined"
                                            type="number"
                                            :rules="heightRule"
                                            :error-messages="heightErrors"
                                            required
                                        ></v-text-field>
                                    </v-form>
                                </v-card-text>
                                <v-card-actions>
                                    <v-btn color="primary" @click="updateHeight()" >Confirm</v-btn>
                                    <v-btn @click="closeHeightDialog()" color="red">Cancel</v-btn>
                                </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-btn>
                    </template>
                    </v-text-field>
                    <v-text-field
                        v-model="user.weight"
                        label="Weight (kg)"
                        variant="underlined"
                        readonly="true"
                    >
                    <template v-slot:append>
                        <v-btn icon>
                            <v-icon>mdi-pencil</v-icon>
                            <v-dialog
                            activator="parent"
                            v-model="changeWeightDialog"
                            width="500"
                            >
                                <v-card>
                                <v-card-title>
                                    Change Weight
                                </v-card-title>
                                <v-card-text>
                                    <v-form @submit.prevent="" ref="editWeightForm">
                                        <v-text-field
                                            v-model="newWeight"
                                            label="Weight (kg)"
                                            variant="underlined"
                                            type="number"
                                            :rules="weightRule"
                                            :error-messages="weightErrors"
                                            required
                                        ></v-text-field>
                                    </v-form>
                                </v-card-text>
                                <v-card-actions>
                                    <v-btn color="primary" @click="updateWeight()" >Confirm</v-btn>
                                    <v-btn @click="closeWeightDialog()" color="red">Cancel</v-btn>
                                </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-btn>
                    </template>
                    </v-text-field>
                    <v-text-field
                        v-model="user.age"
                        label="Age"
                        variant="underlined"
                        readonly="true"
                    >
                    <template v-slot:append>
                        <v-btn icon>
                            <v-icon>mdi-pencil</v-icon>
                            <v-dialog
                            activator="parent"
                            v-model="changeAgeDialog"
                            width="500"
                            >
                                <v-card>
                                <v-card-title>
                                    Change Age
                                </v-card-title>
                                <v-card-text>
                                    <v-form @submit.prevent="" ref="editAgeForm">
                                        <v-text-field
                                            v-model="newAge"
                                            label="Age"
                                            variant="underlined"
                                            type="number"
                                            :rules="ageRule"
                                            :error-messages="ageErrors"
                                            required
                                        ></v-text-field>
                                    </v-form>
                                </v-card-text>
                                <v-card-actions>
                                    <v-btn color="primary" @click="updateAge()" >Confirm</v-btn>
                                    <v-btn @click="closeAgeDialog()" color="red">Cancel</v-btn>
                                </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-btn>
                    </template>
                    </v-text-field>
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
        id: null,
        username: '',
        sex: '',
        height: '',
        weight: '',
        age: null,
    },
    placeholderPassword: 'test1234',
    retypedPassword: '',
    newUsername: '',
    changeUsernamePassword: '',
    password: '',
    newPassword: '',
    newSex: '',
    newHeight: '',
    newWeight: '',
    newAge: null,
    usernameRules: [
        v => !!v || 'Username is required',
        v => (v && v.length <= 30) || 'Username must be less than 30 characters',
    ],
    passwordRules: [
        v => !!v || 'Password is required',
    ],
    newPasswordRules: [
        v => !!v || 'New Password is required',
        v => (v && v.length >= 8) || 'Password must be at least 8 characters long',
        v => (v && v.length <= 30) || 'Password must be less than 40 characters',
    ],
    retypedPasswordRules: [
        v => !!v || 'Please retype the password',
        v => (v && v === this.newPassword) || 'Passwords must match',
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
    newUsernameError: '',
    changeUsernamePasswordError: '',
    passwordErrors: '',
    newPasswordErrors: '',
    sexErrors: '',
    heightErrors: '',
    weightErrors: '',
    ageErrors: '',
    changeUsernameDialog: false,
    changePasswordDialog: false,
    changeSexDialog: false,
    changeHeightDialog: false,
    changeWeightDialog: false,
    changeAgeDialog: false,
};
},

setup() {
    return {};
},

mounted() {
    axios.defaults.withCredentials = true;
    this.loadUser();
},


methods: {
    clearErrors() {
        this.newUsernameError = '';
        this.changeUsernamePasswordError = '';
        this.oldPasswordErrors = '';
        this.passwordErrors = '';
        this.sexErrors = '';
        this.heightErrors = '';
        this.weightErrors = '';
        this.ageErrors = '';
    },

    loadUser() {
        const url = import.meta.env.VITE_BACKEND_URL + '/api/user';

        axios.get(url).then((response) => {
            if(response.status === 200) {
                this.user = response.data;
                this.newSex = this.user.sex;
                this.newHeight = this.user.height;
                this.newWeight = this.user.weight;
                this.newAge = this.user.age;
            }
        });
    },

    async updateUsername() {
        this.clearErrors();
        await this.$refs.editUsernameForm.validate()
        if (this.$refs.editUsernameForm.isValid) {
            const body = {
                username: this.newUsername,
                password: this.changeUsernamePassword
            }
            const url = import.meta.env.VITE_BACKEND_URL + '/api/user/username';

            try {
                const response = await axios.put(url, body);

                if(response.status === 200) {
                    this.logout();
                }
            } catch (error) {
                if(error.response.status === 400) {
                    const errors = error.response.data;
                    if(errors.username) {
                        this.newUsernameError = errors.username;
                    }
                    if(errors.password) {
                        this.changeUsernamePasswordError = errors.password;
                    }
                }
            }
        }
    },

    async updatePassword() {
        this.clearErrors();
        await this.$refs.editPasswordForm.validate()
        if (this.$refs.editPasswordForm.isValid) {
            const body = {
                password: this.password,
                newPassword: this.newPassword
            }
            const url = import.meta.env.VITE_BACKEND_URL + '/api/user/password';

            try {
                const response = await axios.put(url, body);

                if(response.status === 200) {
                    this.logout();
                }
            } catch (error) {
                if(error.response.status === 400) {
                    const errors = error.response.data;
                    if(errors.password) {
                        this.passwordErrors = errors.password;
                    }
                    if(errors.newPassword) {
                        this.newPasswordErrors = errors.newPassword;
                    }
                }
            }
        }
    },

    async updateSex() {
        this.clearErrors();
        await this.$refs.editSexForm.validate()
        if (this.$refs.editSexForm.isValid) {
            const url = import.meta.env.VITE_BACKEND_URL + '/api/user/sex';
            const body = {
                sex: this.newSex
            }
            try {
                console.log(this.newSex);
                const response = await axios.put(url, body);

                if(response.status === 200) {
                    this.user = response.data;
                    this.closeSexDialog();
                }
            } catch (error) {
                if(error.response.status === 400) {
                    const errors = error.response.data;
                    if(errors.sex) {
                        this.sexErrors = errors.sex;
                    }
                }
            }
        }
    },

    async updateHeight() {
        this.clearErrors();
        await this.$refs.editHeightForm.validate()
        if (this.$refs.editHeightForm.isValid) {
            const url = import.meta.env.VITE_BACKEND_URL + '/api/user/height';
            const body = {
                height: this.newHeight
            }
            try {
                const response = await axios.put(url, body);

                if(response.status === 200) {
                    this.user = response.data;
                    this.closeHeightDialog();
                }
            } catch (error) {
                if(error.response.status === 400) {
                    const errors = error.response.data;
                    if(errors.height) {
                        this.heightErrors = errors.message;
                    }
                }
            }
        }
    },

    async updateWeight() {
        this.clearErrors();
        await this.$refs.editWeightForm.validate()
        if (this.$refs.editWeightForm.isValid) {
            const url = import.meta.env.VITE_BACKEND_URL + '/api/user/weight';
            const body = {
                weight: this.newWeight
            }
            try {
                const response = await axios.put(url, body);

                if(response.status === 200) {
                    this.user = response.data;
                    this.closeWeightDialog();
                }
            } catch (error) {
                if(error.response.status === 400) {
                    const errors = error.response.data;
                    if(errors.weight) {
                        this.weightErrors = errors.message;
                    }
                }
            }
        }
    },

    async updateAge() {
        this.clearErrors();
        await this.$refs.editAgeForm.validate()
        if (this.$refs.editAgeForm.isValid) {
            const url = import.meta.env.VITE_BACKEND_URL + '/api/user/age';
            const body = {
                age: this.newAge
            }
            try {
                const response = await axios.put(url, body);

                if(response.status === 200) {
                    this.user = response.data;
                    this.closeAgeDialog();
                }
            } catch (error) {
                if(error.response.status === 400) {
                    const errors = error.response.data;
                    if(errors.age) {
                        this.ageErrors = errors.message;
                    }
                }
            }
        }
    },

    logout() {
        const url = import.meta.env.VITE_BACKEND_URL + '/api/auth/signout';

        axios.post(url).then((response) => {
            if (response.status === 200) {
                this.$router.push('/login');
            }
        });
    },

    closeUsernameDialog() {
        this.changeUsernameDialog = false;
        this.newUsername = '';
        this.changeUsernamePassword = '';
    },

    closePasswordDialog() {
        this.changePasswordDialog = false;
    },

    closeSexDialog() {
        this.newSex = this.user.sex;
        this.changeSexDialog = false;
    },

    closeHeightDialog() {
        this.newHeight = this.user.height;
        this.changeHeightDialog = false;
    },

    closeWeightDialog() {
        this.newWeight = this.user.weight;
        this.changeWeightDialog = false;
    },

    closeAgeDialog() {
        this.newAge = this.user.age;
        this.changeAgeDialog = false;
    },

},
};
</script>
