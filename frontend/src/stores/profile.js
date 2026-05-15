import { defineStore } from "pinia";
import api from "../services/api";

export const useProfileStore = defineStore("profile", {
  state: () => ({
    profiles: [],
    selectedProfileId:
      Number(localStorage.getItem("selectedProfileId") || 0) || null,
    loaded: false,
  }),
  getters: {
    selectedProfile: (state) =>
      state.profiles.find(
        (profile) => profile.id === state.selectedProfileId,
      ) || null,
  },
  actions: {
    setSelectedProfile(profileId) {
      this.selectedProfileId = profileId;
      if (profileId) {
        localStorage.setItem("selectedProfileId", String(profileId));
      } else {
        localStorage.removeItem("selectedProfileId");
      }
    },
    async fetchProfiles() {
      const { data } = await api.get("/profiles");
      this.profiles = data || [];
      this.loaded = true;

      if (!this.profiles.length) {
        this.setSelectedProfile(null);
        return;
      }

      const selectedExists = this.profiles.some(
        (profile) => profile.id === this.selectedProfileId,
      );
      if (!selectedExists) {
        this.setSelectedProfile(this.profiles[0].id);
      }
    },
    async createProfile(payload) {
      const { data } = await api.post("/profiles", payload);
      await this.fetchProfiles();
      this.setSelectedProfile(data.id);
      return data;
    },
    clear() {
      this.$reset();
      localStorage.removeItem("selectedProfileId");
    },
  },
});
