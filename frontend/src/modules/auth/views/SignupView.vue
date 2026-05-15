<template>
  <div class="container">
    <div class="panel" style="max-width: 420px; margin: 2rem auto">
      <h2>Signup</h2>
      <input
        v-model="form.email"
        class="input"
        type="email"
        placeholder="Email"
      />
      <input
        v-model="form.password"
        class="input"
        type="password"
        placeholder="Password"
      />
      <input v-model="form.otp" class="input" type="text" placeholder="OTP" />
      <div style="display: flex; gap: 0.5rem; margin-bottom: 0.75rem">
        <button class="btn btn-muted" @click="requestOtp">Send OTP</button>
        <button class="btn btn-primary" @click="submit">Create Account</button>
      </div>
      <p><RouterLink to="/auth/login">Already have an account?</RouterLink></p>
      <p v-if="message" style="color: #86efac">{{ message }}</p>
      <p v-if="error" style="color: #f87171">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { RouterLink, useRouter } from "vue-router";
import api from "../../../services/api";

const router = useRouter();
const form = reactive({ email: "", password: "", otp: "" });
const message = ref("");
const error = ref("");

const requestOtp = async () => {
  error.value = "";
  await api.post("/auth/otp", { email: form.email, purpose: "SIGNUP" });
  message.value = "OTP sent to email";
};

const submit = async () => {
  error.value = "";
  try {
    await api.post("/auth/signup", form);
    router.push("/auth/login");
  } catch (e) {
    error.value = e.response?.data?.message || "Signup failed";
  }
};
</script>
