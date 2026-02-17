import { ref } from 'vue'

export type ToastType = 'success' | 'error' | 'info'

export interface ToastMessage {
  id: number
  type: ToastType
  text: string
}

const toasts = ref<ToastMessage[]>([])
let toastCounter = 0

export function useToast() {
  const show = (text: string, type: ToastType = 'info', duration = 3000) => {
    const id = ++toastCounter
    toasts.value = [...toasts.value, { id, text, type }]

    window.setTimeout(() => {
      toasts.value = toasts.value.filter((toast) => toast.id !== id)
    }, duration)
  }

  const remove = (id: number) => {
    toasts.value = toasts.value.filter((toast) => toast.id !== id)
  }

  return {
    toasts,
    show,
    remove,
  }
}
