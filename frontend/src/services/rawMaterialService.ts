import { api } from './api'
import type { RawMaterial, RawMaterialInput } from '@/types/entities'

const path = '/api/raw-materials'

export const rawMaterialService = {
  async findAll(): Promise<RawMaterial[]> {
    const { data } = await api.get<RawMaterial[]>(path)
    return data
  },
  async create(payload: RawMaterialInput): Promise<RawMaterial> {
    const { data } = await api.post<RawMaterial>(path, payload)
    return data
  },
  async update(id: number, payload: RawMaterialInput): Promise<RawMaterial> {
    const { data } = await api.put<RawMaterial>(`${path}/${id}`, payload)
    return data
  },
  async remove(id: number): Promise<void> {
    await api.delete(`${path}/${id}`)
  },
}
