<template>
  <div>
    <v-snackbar
      v-for="toast in toasts"
      :key="toast.id"
      :model-value="true"
      :color="toastColor(toast.type)"
      location="top right"
      :timeout="3000"
      @update:model-value="remove(toast.id)"
    >
      {{ toast.text }}
      <template #actions>
        <v-btn variant="text" icon="mdi-close" @click="remove(toast.id)" />
      </template>
    </v-snackbar>
  </div>
</template>

<script setup lang="ts">
import { useToast, type ToastType } from '@/composables/useToast'

const { toasts, remove } = useToast()

function toastColor(type: ToastType): string {
  const colors: Record<ToastType, string> = {
    success: 'success',
    error: 'error',
    info: 'secondary',
  }

  return colors[type]
}
</script>
