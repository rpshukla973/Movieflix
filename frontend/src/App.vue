<template>
  <div>
    <Navbar />
    <RouterView />
  </div>
</template>

<script setup>
import { RouterView } from "vue-router";
import Navbar from "./components/Navbar.vue";
</script>
