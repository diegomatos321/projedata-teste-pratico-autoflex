<template>
  <label class="block">
    <span class="text-xs font-semibold text-slate-400">{{ label }}</span>
    <input
      class="mt-2 w-full rounded-xl border border-white/10 bg-black/20 px-3 py-2 outline-none
        focus:ring-2 focus:ring-indigo-500/40"
      :type="type"
      :required="required"
      :min="min"
      :step="step"
      :maxlength="maxlength"
      v-model="model"
    />
  </label>
</template>

<script setup lang="ts">
const model = defineModel<string | number>({ required: true })

withDefaults(
  defineProps<{
    label: string
    type?: string
    required?: boolean
    min?: number
    step?: number
    maxlength?: number
  }>(),
  {
    type: 'text',
    required: false
  }
)
</script>