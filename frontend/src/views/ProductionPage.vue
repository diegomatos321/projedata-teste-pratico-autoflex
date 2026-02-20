<template>
  <section>
    <div class="d-flex flex-column flex-sm-row ga-3 align-sm-end justify-space-between">
      <div>
        <h2 class="text-h5 font-weight-bold">Production Suggestion</h2>
        <p class="text-medium-emphasis">
          Products that can be produced based on current stock, sorted by value.
        </p>
      </div>
      <v-btn color="primary" :loading="store.loading" @click="refresh">Refresh</v-btn>
    </div>

    <v-alert v-if="store.error" class="mt-4" type="error" variant="tonal">
      {{ store.error }}
    </v-alert>

    <div class="mt-4">
      <DataTable
        :columns="columns"
        :rows="rows"
        row-key="productId"
        :loading="store.loading"
        empty-text="No production suggestions available"
      >
        <template #cell-productPrice="{ row }">
          {{ currency(Number(row.productPrice)) }}
        </template>
        <template #cell-totalValue="{ row }">
          <span class="font-semibold">{{ currency(Number(row.totalValue)) }}</span>
        </template>
      </DataTable>
    </div>

    <v-card class="mt-4" variant="outlined">
      <v-card-text class="text-right">
        <p class="text-subtitle-2 text-medium-emphasis">Total Potential Value</p>
        <p class="text-h6">{{ currency(store.data.totalValue) }}</p>
      </v-card-text>
    </v-card>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import DataTable from '@/components/DataTable.vue'
import { useToast } from '@/composables/useToast'
import { useProductionStore } from '@/stores/productionStore'

const store = useProductionStore()
const { show } = useToast()

const columns = [
  { key: 'productCode', label: 'Product Code' },
  { key: 'productName', label: 'Product Name' },
  { key: 'productPrice', label: 'Price' },
  { key: 'quantityToProduce', label: 'Quantity to Produce' },
  { key: 'totalValue', label: 'Total Value' },
]

const rows = computed(() => {
  return store.data.items.map((item) => ({
    productId: item.productId,
    productCode: item.productCode,
    productName: item.productName,
    productPrice: item.productPrice,
    quantityToProduce: item.quantityToProduce,
    totalValue: item.totalValue,
  }))
})

onMounted(async () => {
  await store.fetchSuggestions()
})

async function refresh() {
  await store.fetchSuggestions()
  if (!store.error) {
    show('Production suggestions refreshed', 'success')
  }
}

function currency(value: number): string {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(value)
}
</script>
