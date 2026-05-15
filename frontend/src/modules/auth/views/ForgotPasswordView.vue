<template>
  <div class="container">
    <div class="panel" style="max-width: 420px; margin: 2rem auto">
      <h2>Reset Password</h2>
      <input
        v-model="form.email"
        class="input"
        type="email"
        placeholder="Email"
      />
      <input v-model="form.otp" class="input" type="text" placeholder="OTP" />
      <input
        v-model="form.newPassword"
        class="input"
        type="password"
        placeholder="New password"
      />
      <div style="display: flex; gap: 0.5rem; margin-bottom: 0.75rem">
        <button class="btn btn-muted" @click="requestOtp">Send OTP</button>
        <button class="btn btn-primary" @click="submit">Reset</button>
      </div>
      <p v-if="message" style="color: #86efac">{{ message }}</p>
      <p v-if="error" style="color: #f87171">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import api from "../../../services/api";

const form = reactive({ email: "", otp: "", newPassword: "" });
const message = ref("");
const error = ref("");

const requestOtp = async () => {
  error.value = "";
  await api.post("/auth/otp", { email: form.email, purpose: "PASSWORD_RESET" });
  message.value = "OTP sent";
};

const submit = async () => {
  error.value = "";
  try {
    await api.post("/auth/forgot-password/reset", form);
    message.value = "Password reset successful";
  } catch (e) {
    error.value = e.response?.data?.message || "Request failed";
  }
};
</script>
