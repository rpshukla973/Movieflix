<template>
  <div class="container">
    <div class="panel" style="max-width: 420px; margin: 2rem auto">
      <h2>Login</h2>
      <div style="display: flex; gap: 0.5rem; margin-bottom: 0.75rem">
        <button
          class="btn"
          :class="mode === 'password' ? 'btn-primary' : 'btn-muted'"
          @click="mode = 'password'"
        >
          Password
        </button>
        <button
          class="btn"
          :class="mode === 'otp' ? 'btn-primary' : 'btn-muted'"
          @click="mode = 'otp'"
        >
          OTP
        </button>
      </div>
      <input
        v-model="form.email"
        class="input"
        type="email"
        placeholder="Email"
      />
      <input
        v-if="mode === 'password'"
        v-model="form.password"
        class="input"
        type="password"
        placeholder="Password"
      />
      <template v-else>
        <input v-model="form.otp" class="input" type="text" placeholder="OTP" />
        <button class="btn btn-muted" @click="requestOtp">Send OTP</button>
      </template>
      <button class="btn btn-primary" @click="submit">
        {{ mode === "password" ? "Login" : "Login with OTP" }}
      </button>
      <p>
        <RouterLink to="/auth/forgot-password">Forgot password?</RouterLink>
      </p>
      <p><RouterLink to="/auth/signup">Create account</RouterLink></p>
      <p v-if="message" style="color: #86efac">{{ message }}</p>
      <p v-if="error" style="color: #f87171">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { RouterLink, useRouter } from "vue-router";
import { useAuthStore } from "../../../stores/auth";
import { useProfileStore } from "../../../stores/profile";
import api from "../../../services/api";

const router = useRouter();
const auth = useAuthStore();
const profileStore = useProfileStore();

const mode = ref("password");
const form = reactive({ email: "", password: "", otp: "" });
const message = ref("");
const error = ref("");

const requestOtp = async () => {
  error.value = "";
  message.value = "";
  try {
    await api.post("/auth/otp", { email: form.email, purpose: "LOGIN" });
    message.value = "OTP sent to email";
  } catch (e) {
    error.value = e.response?.data?.message || "OTP request failed";
  }
};

const submit = async () => {
  error.value = "";
  message.value = "";
  try {
    if (mode.value === "password") {
      await auth.login({ email: form.email, password: form.password });
    } else {
      await auth.loginWithOtp({ email: form.email, otp: form.otp });
    }
    await profileStore.fetchProfiles();
    router.push("/home");
  } catch (e) {
    error.value = e.response?.data?.message || "Login failed";
  }
};
</script>
