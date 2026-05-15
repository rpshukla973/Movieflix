<template>
  <div class="card" @click="$router.push(`/player/${video.id}`)">
    <img :src="video.thumbnailUrl || fallback" alt="thumbnail" class="thumb" />
    <div class="body">
      <h4>{{ video.title }}</h4>
      <p>{{ video.genre }}</p>
    </div>
  </div>
</template>

<script setup>
defineProps({
  video: {
    type: Object,
    required: true,
  },
});

const fallback = "https://placehold.co/400x220/111827/F3F4F6?text=Movieflix";
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
