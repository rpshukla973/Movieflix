<template>
  <div class="container">
    <h2>Trending</h2>
    <div class="grid grid-cols-4">
      <VideoCard v-for="video in videos" :key="video.id" :video="video" />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import api from "../../../services/api";
import VideoCard from "../../../components/VideoCard.vue";

const videos = ref([]);

onMounted(async () => {
  const { data } = await api.get("/videos?page=0&size=12");
  videos.value = data.content || [];
});
</script>
