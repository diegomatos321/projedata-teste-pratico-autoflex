<template>
  <section>
    <h2 class="text-h5 font-weight-bold">Dashboard</h2>
    <p class="text-medium-emphasis">Overview of products, raw materials and production value.</p>

    <v-row class="mt-2" dense>
      <v-col cols="12" sm="6" lg="3">
        <v-card variant="outlined">
          <v-card-text>
            <p class="text-subtitle-2 text-medium-emphasis">Products</p>
            <p class="text-h5 mt-2">{{ productStore.products.length }}</p>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12" sm="6" lg="3">
        <v-card variant="outlined">
          <v-card-text>
            <p class="text-subtitle-2 text-medium-emphasis">Raw Materials</p>
            <p class="text-h5 mt-2">{{ rawMaterialStore.rawMaterials.length }}</p>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12" sm="6" lg="3">
        <v-card variant="outlined">
          <v-card-text>
            <p class="text-subtitle-2 text-medium-emphasis">Suggested Products</p>
            <p class="text-h5 mt-2">{{ productionStore.data.items.length }}</p>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12" sm="6" lg="3">
        <v-card variant="outlined">
          <v-card-text>
            <p class="text-subtitle-2 text-medium-emphasis">Potential Value</p>
            <p class="text-h5 mt-2">{{ currency(productionStore.data.totalValue) }}</p>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <v-card class="mt-4" variant="outlined">
      <v-card-title>Top Production Suggestions</v-card-title>
      <v-divider />
      <v-card-text>
        <v-progress-linear v-if="productionStore.loading" indeterminate color="primary" />
        <v-alert v-else-if="productionStore.error" type="error" variant="tonal">
          {{ productionStore.error }}
        </v-alert>
        <v-list v-else>
          <v-list-item
            v-for="item in productionStore.data.items.slice(0, 5)"
            :key="item.productId"
            :title="`${item.productCode} - ${item.productName}`"
            :subtitle="`Quantity to Produce: ${item.quantityToProduce}`"
          >
            <template #append>
              <strong>{{ currency(item.totalValue) }}</strong>
            </template>
          </v-list-item>
          <v-list-item
            v-if="!productionStore.data.items.length"
            title="No suggestions available."
          />
        </v-list>
      </v-card-text>
    </v-card>
  </section>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useProductStore } from '@/stores/productStore'
import { useProductionStore } from '@/stores/productionStore'
import { useRawMaterialStore } from '@/stores/rawMaterialStore'

const productStore = useProductStore()
const rawMaterialStore = useRawMaterialStore()
const productionStore = useProductionStore()

onMounted(async () => {
  await Promise.all([
    productStore.fetchProducts(),
    rawMaterialStore.fetchRawMaterials(),
    productionStore.fetchSuggestions(),
  ])
})

function currency(value: number): string {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(value)
}
</script>
