<template>
  <nav class="nav">
    <div class="container nav-inner">
      <RouterLink to="/home" class="brand">Movieflix</RouterLink>
      <div class="links">
        <RouterLink to="/home">Home</RouterLink>
        <RouterLink to="/search">Search</RouterLink>
        <RouterLink v-if="isAdmin" to="/admin">Admin</RouterLink>
      </div>
      <div v-if="isAuthenticated" class="actions">
        <select
          :value="selectedProfileId ?? ''"
          class="profile-select"
          @change="onProfileChange"
        >
          <option value="" disabled>Select profile</option>
          <option
            v-for="profile in profiles"
            :key="profile.id"
            :value="profile.id"
          >
            {{ profile.name }}
          </option>
        </select>
        <button class="btn btn-muted" @click="createProfile">
          New Profile
        </button>
        <button class="btn btn-muted" @click="logout">Logout</button>
      </div>
      <RouterLink v-else class="btn btn-primary" to="/auth/login"
        >Login</RouterLink
      >
    </div>
  </nav>
</template>

<script setup>
import { computed, onMounted } from "vue";
import { RouterLink, useRouter } from "vue-router";
import { useAuthStore } from "../stores/auth";
import { useProfileStore } from "../stores/profile";

const auth = useAuthStore();
const profileStore = useProfileStore();
const router = useRouter();

const isAuthenticated = computed(() => auth.isAuthenticated);
const isAdmin = computed(() => auth.isAdmin);
const profiles = computed(() => profileStore.profiles);
const selectedProfileId = computed(() => profileStore.selectedProfileId);

onMounted(async () => {
  if (auth.isAuthenticated && !profileStore.loaded) {
    await profileStore.fetchProfiles();
  }
});

const onProfileChange = (event) => {
  const value = Number(event.target.value) || null;
  profileStore.setSelectedProfile(value);
};

const createProfile = async () => {
  const name = window.prompt("Profile name");
  if (!name || !name.trim()) {
    return;
  }
  await profileStore.createProfile({
    name: name.trim(),
    maturityRating: "PG-13",
  });
};

const logout = () => {
  profileStore.clear();
  auth.logout();
  router.push("/auth/login");
};
</script>

<style scoped>
.nav {
  border-bottom: 1px solid #1f2937;
  background: #0b0f19;
}

.nav-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.brand {
  color: #ef4444;
  font-weight: 700;
  font-size: 1.1rem;
}

.links {
  display: flex;
  gap: 1rem;
}

.actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.profile-select {
  min-width: 180px;
  background: #0f172a;
  border: 1px solid #334155;
  color: #f3f4f6;
  border-radius: 0.5rem;
  padding: 0.6rem 0.75rem;
}
</style>
