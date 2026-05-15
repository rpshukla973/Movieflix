import { defineStore } from "pinia";
import api from "../services/api";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    userEmail: localStorage.getItem("userEmail") || "",
    roles: JSON.parse(localStorage.getItem("roles") || "[]"),
    accessToken: localStorage.getItem("accessToken") || "",
    refreshToken: localStorage.getItem("refreshToken") || "",
  }),
  getters: {
    isAuthenticated: (state) => Boolean(state.accessToken),
    isAdmin: (state) => state.roles.includes("ROLE_ADMIN"),
  },
  actions: {
    setSession(data, email) {
      this.accessToken = data.accessToken;
      this.refreshToken = data.refreshToken;
      this.userEmail = email;
      this.roles = ["ROLE_USER"];
      localStorage.setItem("accessToken", data.accessToken);
      localStorage.setItem("refreshToken", data.refreshToken);
      localStorage.setItem("userEmail", email);
      localStorage.setItem("roles", JSON.stringify(this.roles));
    },
    async login(payload) {
      const { data } = await api.post("/auth/login", payload);
      this.setSession(data, payload.email);
    },
    async loginWithOtp(payload) {
      const { data } = await api.post("/auth/login/otp", payload);
      this.setSession(data, payload.email);
    },
    logout() {
      this.$reset();
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      localStorage.removeItem("userEmail");
      localStorage.removeItem("roles");
    },
  },
});
