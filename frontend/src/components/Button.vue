<template>
  <button
    :type="type"
    class="inline-flex items-center justify-center rounded-xl px-4 py-2 text-sm font-semibold border transition
      border-white/10 bg-white/5 hover:bg-white/10 active:scale-[0.99]"
    :class="variantClass"
    :disabled="disabled"
  >
    <slot />
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue'

type Variant = 'primary' | 'secondary' | 'danger'

const props = withDefaults(
  defineProps<{ type?: 'button' | 'submit'; variant?: Variant; disabled?: boolean }>(),
  {
    type: 'button',
    variant: 'primary',
    disabled: false
  }
)

const variantClass = computed(() => {
  if (props.disabled) return 'opacity-60 cursor-not-allowed'

  switch (props.variant) {
    case 'danger':
      return 'bg-rose-500/10 hover:bg-rose-500/20 border-rose-500/20'
    case 'secondary':
      return 'bg-white/5 hover:bg-white/10'
    default:
      return 'bg-indigo-500/15 hover:bg-indigo-500/25 border-indigo-500/25'
  }
})
</script>