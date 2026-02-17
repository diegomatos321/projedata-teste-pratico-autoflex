import { defineStore } from 'pinia'
import { productionService } from '@/services/productionService'
import type { ProductionSuggestionResponse } from '@/types/entities'

interface ProductionState {
  data: ProductionSuggestionResponse
  loading: boolean
  error: string | null
}

function parseError(error: unknown): string {
  return error instanceof Error ? error.message : 'Unexpected error'
}

export const useProductionStore = defineStore('productionStore', {
  state: (): ProductionState => ({
    data: {
      items: [],
      totalValue: 0,
    },
    loading: false,
    error: null,
  }),
  actions: {
    async fetchSuggestions() {
      this.loading = true
      this.error = null
      try {
        const response = await productionService.getSuggestions()
        this.data = {
          ...response,
          items: [...response.items].sort((a, b) => b.totalValue - a.totalValue),
        }
      } catch (error: unknown) {
        this.error = parseError(error)
      } finally {
        this.loading = false
      }
    },
  },
})
