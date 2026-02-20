<template>
  <section>
    <div class="d-flex flex-column flex-sm-row ga-3 align-sm-end justify-space-between">
      <div>
        <h2 class="text-h5 font-weight-bold">Products</h2>
        <p class="text-medium-emphasis">Manage products and their bill of materials.</p>
      </div>
      <v-btn color="primary" @click="goNew">New Product</v-btn>
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

    <v-alert v-if="productStore.error" class="mt-4" type="error" variant="tonal">
      {{ productStore.error }}
    </v-alert>

    <div class="mt-4">
      <DataTable
        :columns="columns"
        :rows="tableRows"
        row-key="id"
        :loading="productStore.loading"
        empty-text="No products found"
      >
        <template #cell-price="{ row }">
          {{ currency(Number(row.price)) }}
        </template>
        <template #cell-actions="{ row }">
          <div class="d-flex flex-wrap ga-2">
            <v-btn variant="text" size="small" @click="goEdit(Number(row.id))">Edit</v-btn>
            <v-btn variant="text" size="small" @click="goBom(Number(row.id))">BOM</v-btn>
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
        <v-card-text>This action will permanently remove the product. Continue?</v-card-text>
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
import { useProductStore } from '@/stores/productStore'

const productStore = useProductStore()
const router = useRouter()
const { show } = useToast()

const search = ref('')
const confirmOpen = ref(false)
const deleteId = ref<number | null>(null)

const columns = [
  { key: 'code', label: 'Code' },
  { key: 'name', label: 'Name' },
  { key: 'price', label: 'Price' },
  { key: 'actions', label: 'Actions' },
]

const filteredProducts = computed(() => {
  const term = search.value.trim().toLowerCase()
  if (!term) {
    return productStore.products
  }

  return productStore.products.filter((product) => {
    return product.code.toLowerCase().includes(term) || product.name.toLowerCase().includes(term)
  })
})

const tableRows = computed(() => {
  return filteredProducts.value.map((product) => ({
    id: product.id,
    code: product.code,
    name: product.name,
    price: product.price,
    actions: '',
  }))
})

onMounted(async () => {
  await productStore.fetchProducts()
})

function goNew() {
  void router.push('/products/new')
}

function goEdit(id: number) {
  void router.push(`/products/${id}/edit`)
}

function goBom(id: number) {
  void router.push(`/products/${id}/bom`)
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
    await productStore.deleteProduct(deleteId.value)
    show('Product deleted', 'success')
  } catch {
    show('Unable to delete product', 'error')
  } finally {
    confirmOpen.value = false
    deleteId.value = null
  }
}

function currency(value: number): string {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(value)
}
</script>
