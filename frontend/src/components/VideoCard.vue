<template>
  <div class="card" @click="$router.push(`/player/${video.id}`)">
    <img :src="thumbnailUrl" alt="thumbnail" class="thumb" />
    <div class="body">
      <h4>{{ video.title }}</h4>
      <p>{{ video.genre }}</p>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  video: {
    type: Object,
    required: true,
  },
});

const fallback = "https://placehold.co/400x220/111827/F3F4F6?text=Movieflix";
const thumbnailUrl = computed(() => {
  const rawPath = props.video?.thumbnailUrl;
  if (!rawPath) {
    return fallback;
  }

  if (rawPath.startsWith("http://") || rawPath.startsWith("https://")) {
    return rawPath;
  }

  return `${import.meta.env.VITE_API_BASE_URL || "http://localhost:9090/api"}`.replace(/\/api$/, "") + "/storage/" + rawPath.replace(/^\//, "");
});
</script>

<style scoped>
.card {
  background: #111827;
  border-radius: 0.75rem;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.card:hover {
  transform: translateY(-4px);
}

.thumb {
  width: 100%;
  height: 130px;
  object-fit: cover;
}

.body {
  padding: 0.75rem;
}

p {
  color: #9ca3af;
  margin: 0;
}
</style>
