<template>
  <div class="container">
    <h2>Player</h2>
    <p v-if="!selectedProfileId" style="color: #fbbf24">
      Select a profile to enable continue watching.
    </p>
    <video
      ref="videoEl"
      controls
      @timeupdate="syncWatchHistory(false)"
      @pause="syncWatchHistory(true)"
      @ended="syncWatchHistory(true)"
      style="
        width: 100%;
        max-height: 75vh;
        border-radius: 0.75rem;
        background: black;
      "
    >
      <source :src="streamUrl" type="video/mp4" />
    </video>
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, ref, watch } from "vue";
import { useRoute } from "vue-router";
import api from "../../../services/api";
import { useProfileStore } from "../../../stores/profile";

const route = useRoute();
const profileStore = useProfileStore();
const videoEl = ref(null);
const lastSyncAt = ref(0);

const selectedProfileId = computed(() => profileStore.selectedProfileId);
const videoId = computed(() => Number(route.params.id));
const streamUrl = computed(
  () =>
    `${import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api"}/videos/stream/${route.params.id}`,
);

const syncWatchHistory = async (force) => {
  if (
    !selectedProfileId.value ||
    !videoEl.value ||
    Number.isNaN(videoId.value)
  ) {
    return;
  }

  const now = Date.now();
  if (!force && now - lastSyncAt.value < 10000) {
    return;
  }

  lastSyncAt.value = now;
  try {
    await api.post("/watch-history", {
      profileId: selectedProfileId.value,
      videoId: videoId.value,
      lastWatchedPosition: Math.floor(videoEl.value.currentTime || 0),
    });
  } catch {
    // ignore sync failures in player flow
  }
};

const resumeFromHistory = async () => {
  if (!selectedProfileId.value || Number.isNaN(videoId.value)) {
    return;
  }
  try {
    const { data } = await api.get(
      `/watch-history?profileId=${selectedProfileId.value}&page=0&size=100`,
    );
    const entries = data.content || [];
    const current = entries.find((entry) => entry.videoId === videoId.value);
    if (!current || !videoEl.value) {
      return;
    }
    const setPosition = () => {
      videoEl.value.currentTime = current.lastWatchedPosition || 0;
    };
    if (videoEl.value.readyState >= 1) {
      setPosition();
    } else {
      videoEl.value.addEventListener("loadedmetadata", setPosition, {
        once: true,
      });
    }
  } catch {
    // ignore resume failures in player flow
  }
};

onMounted(async () => {
  if (!profileStore.loaded) {
    try {
      await profileStore.fetchProfiles();
    } catch {
      // keep player usable even if profiles fail
    }
  }
  await resumeFromHistory();
  window.addEventListener("beforeunload", onBeforeUnload);
});

onBeforeUnmount(() => {
  window.removeEventListener("beforeunload", onBeforeUnload);
  syncWatchHistory(true);
});

watch([selectedProfileId, videoId], async () => {
  await resumeFromHistory();
});

const onBeforeUnload = () => {
  syncWatchHistory(true);
};
</script>
