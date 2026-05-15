<template>
  <div v-if="open" class="overlay" @click.self="$emit('close')">
    <div class="modal panel">
      <slot />
    </div>
  </div>
</template>

<script setup>
defineProps({
  open: { type: Boolean, default: false },
});
</script>

<style scoped>
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.65);
  display: grid;
  place-items: center;
  padding: 1rem;
}

.modal {
  width: min(600px, 100%);
}
</style>
