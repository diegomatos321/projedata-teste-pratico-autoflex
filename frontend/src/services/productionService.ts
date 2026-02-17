import { api } from './api'
import type { ProductionSuggestionResponse } from '@/types/entities'

const path = '/api/production-suggestions'

export const productionService = {
  async getSuggestions(): Promise<ProductionSuggestionResponse> {
    const { data } = await api.get<ProductionSuggestionResponse>(path)
    return data
  },
}
