import { api } from './api'
import type { Product, ProductInput } from '@/types/entities'

const path = '/api/products'

export const productService = {
  async findAll(): Promise<Product[]> {
    const { data } = await api.get<Product[]>(path)
    return data
  },
  async create(payload: ProductInput): Promise<Product> {
    const { data } = await api.post<Product>(path, payload)
    return data
  },
  async update(id: number, payload: ProductInput): Promise<Product> {
    const { data } = await api.put<Product>(`${path}/${id}`, payload)
    return data
  },
  async remove(id: number): Promise<void> {
    await api.delete(`${path}/${id}`)
  },
}
