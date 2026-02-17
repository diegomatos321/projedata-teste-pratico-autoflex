<template>
  <section>
    <div class="d-flex flex-column flex-sm-row ga-3 align-sm-end justify-space-between">
      <div>
        <h2 class="text-h5 font-weight-bold">{{ title }}</h2>
        <p class="text-medium-emphasis">Fill the raw material information.</p>
      </div>
      <v-btn variant="text" @click="goBack">Back</v-btn>
    </div>

    <v-alert v-if="store.error" class="mt-4" type="error" variant="tonal">
      {{ store.error }}
    </v-alert>

    <v-card class="mt-4" variant="outlined">
      <v-card-text>
        <form class="d-flex flex-column ga-3" @submit.prevent="submit">
          <v-text-field
            v-model="form.code"
            label="Code"
            variant="outlined"
            density="comfortable"
            :error-messages="errors.code ? [errors.code] : []"
          />
          <v-text-field
            v-model="form.name"
            label="Name"
            variant="outlined"
            density="comfortable"
            :error-messages="errors.name ? [errors.name] : []"
          />
          <v-text-field
            v-model="form.stockQuantity"
            type="number"
            label="Stock Quantity"
            variant="outlined"
            density="comfortable"
            :error-messages="errors.stockQuantity ? [errors.stockQuantity] : []"
          />
          <div class="d-flex justify-end ga-2">
            <v-btn variant="text" @click="goBack">Cancel</v-btn>
            <v-btn color="primary" :loading="store.loading" type="submit">Save</v-btn>
          </div>
        </form>
      </v-card-text>
    </v-card>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useToast } from '@/composables/useToast'
import { useRawMaterialStore } from '@/stores/rawMaterialStore'

interface RawMaterialForm {
  code: string
  name: string
  stockQuantity: string
}

const store = useRawMaterialStore()
const route = useRoute()
const router = useRouter()
const { show } = useToast()

const form = reactive<RawMaterialForm>({
  code: '',
  name: '',
  stockQuantity: '',
})

const errors = reactive<RawMaterialForm>({
  code: '',
  name: '',
  stockQuantity: '',
})

const rawMaterialId = computed(() => {
  const value = route.params.id
  return value ? Number(value) : null
})

const isEdit = computed(() => rawMaterialId.value !== null && !Number.isNaN(rawMaterialId.value))

const title = computed(() => (isEdit.value ? 'Edit Raw Material' : 'New Raw Material'))

onMounted(async () => {
  if (!isEdit.value) {
    return
  }

  if (!store.rawMaterials.length) {
    await store.fetchRawMaterials()
  }

  const rawMaterial = store.rawMaterials.find((item) => item.id === rawMaterialId.value)
  if (!rawMaterial) {
    show('Raw material not found', 'error')
    void router.push('/raw-materials')
    return
  }

  form.code = rawMaterial.code
  form.name = rawMaterial.name
  form.stockQuantity = String(rawMaterial.stockQuantity)
})

function validate(): boolean {
  errors.code = form.code.trim() ? '' : 'Code is required'
  errors.name = form.name.trim() ? '' : 'Name is required'

  const quantity = Number(form.stockQuantity)
  errors.stockQuantity =
    Number.isFinite(quantity) && quantity >= 0 ? '' : 'Enter a valid stock quantity'

  return !errors.code && !errors.name && !errors.stockQuantity
}

async function submit() {
  if (!validate()) {
    return
  }

  const payload = {
    code: form.code.trim(),
    name: form.name.trim(),
    stockQuantity: Number(form.stockQuantity),
  }

  try {
    if (isEdit.value && rawMaterialId.value !== null) {
      await store.updateRawMaterial(rawMaterialId.value, payload)
      show('Raw material updated successfully', 'success')
    } else {
      await store.createRawMaterial(payload)
      show('Raw material created successfully', 'success')
    }

    void router.push('/raw-materials')
  } catch {
    show('Unable to save raw material', 'error')
  }
}

function goBack() {
  void router.push('/raw-materials')
}
</script>
