import { api } from './api'
import type { ProductMaterial, ProductMaterialInput } from '@/types/entities'

const path = '/api/product-materials'

export const bomService = {
  async findAll(): Promise<ProductMaterial[]> {
    const { data } = await api.get<ProductMaterial[]>(path)
    return data
  },
  async findByProduct(productId: number): Promise<ProductMaterial[]> {
    const { data } = await api.get<ProductMaterial[]>(`${path}/product/${productId}`)
    return data
  },
  async create(payload: ProductMaterialInput): Promise<void> {
    await api.post<void>(path, payload)
  },
  async update(id: number, payload: ProductMaterialInput): Promise<ProductMaterial> {
    const { data } = await api.put<ProductMaterial>(`${path}/${id}`, payload)
    return data
  },
  async remove(id: number): Promise<void> {
    await api.delete(`${path}/${id}`)
  },
}
