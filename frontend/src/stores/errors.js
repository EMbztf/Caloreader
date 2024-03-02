import { ref, computed } from "vue";
import { defineStore } from "pinia";


export const useErrorStore = defineStore("error", () => {
    // define reactive state values
    const error = ref("");
    const errorSnackbar = ref(false);

    function showError(errorMessage) {
        setError(errorMessage);
        showSnackbar();
    }

    function setError(newError) {
        console.error(newError);
        error.value = newError;
    }

    function showSnackbar() {
        errorSnackbar.value = true;
    }
   
    function closeSnackbar() {
        errorSnackbar.value = false;
    }

    // expose state values, computed values and methods
    return {
        error,
        errorSnackbar,
        showError,
        closeSnackbar,
        showSnackbar,
    };
});
