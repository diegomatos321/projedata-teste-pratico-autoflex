<template>
  <div
    class="rounded-xl border px-4 py-3 text-sm"
    :class="type === 'error'
      ? 'border-rose-500/30 bg-rose-500/10 text-rose-100'
      : 'border-emerald-500/25 bg-emerald-500/10 text-emerald-100'"
  >
    {{ message }}
  </div>
</template>

<script setup lang="ts">
defineProps<{ type: 'error' | 'success'; message: string }>()
</script>