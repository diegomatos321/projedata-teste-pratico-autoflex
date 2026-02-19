import { defineStore } from 'pinia'
import { bomService } from '@/services/bomService'
import type { ProductMaterial, ProductMaterialInput } from '@/types/entities'

interface BomState {
  productMaterials: ProductMaterial[]
  loading: boolean
  error: string | null
}

function parseError(error: unknown): string {
  return error instanceof Error ? error.message : 'Unexpected error'
}

export const useBomStore = defineStore('bomStore', {
  state: (): BomState => ({
    productMaterials: [],
    loading: false,
    error: null,
  }),
  actions: {
    async fetchByProduct(productId: number) {
      this.loading = true
      this.error = null
      try {
        this.productMaterials = await bomService.findByProduct(productId)
      } catch (error: unknown) {
        this.error = parseError(error)
      } finally {
        this.loading = false
      }
    },
    async createAssociation(payload: ProductMaterialInput) {
      this.loading = true
      this.error = null
      try {
        await bomService.create(payload)
        await this.fetchByProduct(payload.productId)
      } catch (error: unknown) {
        this.error = parseError(error)
        throw error
      } finally {
        this.loading = false
      }
    },
    async updateAssociation(id: number, payload: ProductMaterialInput) {
      this.loading = true
      this.error = null
      try {
        await bomService.update(id, payload)
        await this.fetchByProduct(payload.productId)
      } catch (error: unknown) {
        this.error = parseError(error)
        throw error
      } finally {
        this.loading = false
      }
    },
    async deleteAssociation(id: number) {
      this.loading = true
      this.error = null
      try {
        await bomService.remove(id)
        this.productMaterials = this.productMaterials.filter((item) => item.id !== id)
      } catch (error: unknown) {
        this.error = parseError(error)
        throw error
      } finally {
        this.loading = false
      }
    },
  },
})
