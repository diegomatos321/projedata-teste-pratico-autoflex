import { defineStore } from 'pinia'
import { rawMaterialService } from '@/services/rawMaterialService'
import type { RawMaterial, RawMaterialInput } from '@/types/entities'

interface RawMaterialState {
  rawMaterials: RawMaterial[]
  loading: boolean
  error: string | null
}

function parseError(error: unknown): string {
  return error instanceof Error ? error.message : 'Unexpected error'
}

export const useRawMaterialStore = defineStore('rawMaterialStore', {
  state: (): RawMaterialState => ({
    rawMaterials: [],
    loading: false,
    error: null,
  }),
  actions: {
    async fetchRawMaterials() {
      this.loading = true
      this.error = null
      try {
        this.rawMaterials = await rawMaterialService.findAll()
      } catch (error: unknown) {
        this.error = parseError(error)
      } finally {
        this.loading = false
      }
    },
    async createRawMaterial(payload: RawMaterialInput) {
      this.loading = true
      this.error = null
      try {
        const item = await rawMaterialService.create(payload)
        this.rawMaterials = [...this.rawMaterials, item]
        return item
      } catch (error: unknown) {
        this.error = parseError(error)
        throw error
      } finally {
        this.loading = false
      }
    },
    async updateRawMaterial(id: number, payload: RawMaterialInput) {
      this.loading = true
      this.error = null
      try {
        const updated = await rawMaterialService.update(id, payload)
        this.rawMaterials = this.rawMaterials.map((item) => (item.id === id ? updated : item))
        return updated
      } catch (error: unknown) {
        this.error = parseError(error)
        throw error
      } finally {
        this.loading = false
      }
    },
    async deleteRawMaterial(id: number) {
      this.loading = true
      this.error = null
      try {
        await rawMaterialService.remove(id)
        this.rawMaterials = this.rawMaterials.filter((item) => item.id !== id)
      } catch (error: unknown) {
        this.error = parseError(error)
        throw error
      } finally {
        this.loading = false
      }
    },
  },
})
