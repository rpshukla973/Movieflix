<template>
  <div class="container">
    <h2>Search</h2>
    <input
      v-model="query"
      class="input"
      placeholder="Search videos"
      @input="search"
    />
    <div class="grid grid-cols-4">
      <VideoCard v-for="video in videos" :key="video.id" :video="video" />
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import api from "../../../services/api";
import VideoCard from "../../../components/VideoCard.vue";

const query = ref("");
const videos = ref([]);

const search = async () => {
  const { data } = await api.get(
    `/videos?q=${encodeURIComponent(query.value)}&page=0&size=12`,
  );
  videos.value = data.content || [];
};
</script>
