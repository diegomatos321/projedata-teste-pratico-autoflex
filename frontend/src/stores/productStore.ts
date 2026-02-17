import { defineStore } from 'pinia'
import { productService } from '@/services/productService'
import type { Product, ProductInput } from '@/types/entities'

interface ProductState {
  products: Product[]
  loading: boolean
  error: string | null
}

function parseError(error: unknown): string {
  return error instanceof Error ? error.message : 'Unexpected error'
}

export const useProductStore = defineStore('productStore', {
  state: (): ProductState => ({
    products: [],
    loading: false,
    error: null,
  }),
  actions: {
    async fetchProducts() {
      this.loading = true
      this.error = null
      try {
        this.products = await productService.findAll()
      } catch (error: unknown) {
        this.error = parseError(error)
      } finally {
        this.loading = false
      }
    },
    async createProduct(payload: ProductInput) {
      this.loading = true
      this.error = null
      try {
        const product = await productService.create(payload)
        this.products = [...this.products, product]
        return product
      } catch (error: unknown) {
        this.error = parseError(error)
        throw error
      } finally {
        this.loading = false
      }
    },
    async updateProduct(id: number, payload: ProductInput) {
      this.loading = true
      this.error = null
      try {
        const updated = await productService.update(id, payload)
        this.products = this.products.map((product) => (product.id === id ? updated : product))
        return updated
      } catch (error: unknown) {
        this.error = parseError(error)
        throw error
      } finally {
        this.loading = false
      }
    },
    async deleteProduct(id: number) {
      this.loading = true
      this.error = null
      try {
        await productService.remove(id)
        this.products = this.products.filter((product) => product.id !== id)
      } catch (error: unknown) {
        this.error = parseError(error)
        throw error
      } finally {
        this.loading = false
      }
    },
  },
})
