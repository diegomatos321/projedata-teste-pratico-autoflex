<template>
  <section>
    <div class="d-flex flex-column flex-sm-row ga-3 align-sm-end justify-space-between">
      <div>
        <h2 class="text-h5 font-weight-bold">Raw Materials</h2>
        <p class="text-medium-emphasis">Manage stock items used in product production.</p>
      </div>
      <v-btn color="primary" @click="goNew">New Raw Material</v-btn>
    </div>

    <v-card class="mt-4" variant="outlined">
      <v-card-text>
        <v-text-field
          v-model="search"
          label="Search"
          placeholder="Search by code or name"
          variant="outlined"
          density="comfortable"
          hide-details="auto"
        />
      </v-card-text>
    </v-card>

    <v-alert v-if="store.error" class="mt-4" type="error" variant="tonal">
      {{ store.error }}
    </v-alert>

    <div class="mt-4">
      <DataTable
        :columns="columns"
        :rows="tableRows"
        row-key="id"
        :loading="store.loading"
        empty-text="No raw materials found"
      >
        <template #cell-stockQuantity="{ row }">
          {{ Number(row.stockQuantity).toLocaleString('en-US') }}
        </template>
        <template #cell-actions="{ row }">
          <div class="d-flex ga-2">
            <v-btn variant="text" size="small" @click="goEdit(Number(row.id))">Edit</v-btn>
            <v-btn variant="text" size="small" color="error" @click="askDelete(Number(row.id))">
              Delete
            </v-btn>
          </div>
        </template>
      </DataTable>
    </div>

    <v-dialog :model-value="confirmOpen" max-width="460" @update:model-value="onConfirmChange">
      <v-card>
        <v-card-title>Confirmation</v-card-title>
        <v-card-text>This action will permanently remove the raw material. Continue?</v-card-text>
        <v-card-actions class="justify-end">
          <v-btn variant="text" @click="confirmOpen = false">Cancel</v-btn>
          <v-btn color="error" variant="flat" @click="confirmDelete">Confirm</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import DataTable from '@/components/DataTable.vue'
import { useToast } from '@/composables/useToast'
import { useRawMaterialStore } from '@/stores/rawMaterialStore'

const store = useRawMaterialStore()
const router = useRouter()
const { show } = useToast()

const search = ref('')
const confirmOpen = ref(false)
const deleteId = ref<number | null>(null)

const columns = [
  { key: 'code', label: 'Code' },
  { key: 'name', label: 'Name' },
  { key: 'stockQuantity', label: 'Stock Quantity' },
  { key: 'actions', label: 'Actions' },
]

const filteredRawMaterials = computed(() => {
  const term = search.value.trim().toLowerCase()
  if (!term) {
    return store.rawMaterials
  }

  return store.rawMaterials.filter((item) => {
    return item.code.toLowerCase().includes(term) || item.name.toLowerCase().includes(term)
  })
})

const tableRows = computed(() => {
  return filteredRawMaterials.value.map((item) => ({
    id: item.id,
    code: item.code,
    name: item.name,
    stockQuantity: item.stockQuantity,
    actions: '',
  }))
})

onMounted(async () => {
  await store.fetchRawMaterials()
})

function goNew() {
  void router.push('/raw-materials/new')
}

function goEdit(id: number) {
  void router.push(`/raw-materials/${id}/edit`)
}

function askDelete(id: number) {
  deleteId.value = id
  confirmOpen.value = true
}

function onConfirmChange(value: boolean) {
  if (!value) {
    confirmOpen.value = false
  }
}

async function confirmDelete() {
  if (!deleteId.value) {
    return
  }

  try {
    await store.deleteRawMaterial(deleteId.value)
    show('Raw material deleted', 'success')
  } catch {
    show('Unable to delete raw material', 'error')
  } finally {
    confirmOpen.value = false
    deleteId.value = null
  }
}
</script>
