<template>
  <div class="container">
    <h2>Admin Dashboard</h2>
    <div class="panel" style="margin-bottom: 1rem">
      <p><strong>Total Users:</strong> {{ stats.users || 0 }}</p>
      <p><strong>Total Videos:</strong> {{ stats.videos || 0 }}</p>
    </div>

    <div class="panel">
      <h3>Upload Video</h3>
      <input v-model="form.title" class="input" placeholder="Title" />
      <input v-model="form.genre" class="input" placeholder="Genre" />
      <textarea
        v-model="form.description"
        class="input"
        placeholder="Description"
      />
      <input type="file" class="input" @change="onVideoSelect" />
      <input type="file" class="input" @change="onThumbSelect" />
      <button class="btn btn-primary" @click="upload">Upload</button>
      <p v-if="message" style="color: #86efac">{{ message }}</p>
      <p v-if="error" style="color: #f87171">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import api from "../../../services/api";

const stats = ref({});
const message = ref("");
const error = ref("");

const form = reactive({
  title: "",
  genre: "",
  description: "",
});

const videoFile = ref(null);
const thumbnailFile = ref(null);

const onVideoSelect = (event) => {
  videoFile.value = event.target.files?.[0] || null;
};

const onThumbSelect = (event) => {
  thumbnailFile.value = event.target.files?.[0] || null;
};

const upload = async () => {
  error.value = "";
  message.value = "";
  try {
    const payload = new FormData();
    payload.append("title", form.title);
    payload.append("genre", form.genre);
    payload.append("description", form.description);
    if (videoFile.value) payload.append("video", videoFile.value);
    if (thumbnailFile.value) payload.append("thumbnail", thumbnailFile.value);
    await api.post("/videos/upload", payload);
    message.value = "Video uploaded successfully";
  } catch (e) {
    error.value = e.response?.data?.message || "Upload failed";
  }
};

onMounted(async () => {
  const { data } = await api.get("/admin/dashboard");
  stats.value = data;
});
</script>
