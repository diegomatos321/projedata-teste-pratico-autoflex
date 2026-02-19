export interface Product {
  id: number
  code: string
  name: string
  price: number
}

export interface ProductInput {
  code: string
  name: string
  price: number
}

export interface RawMaterial {
  id: number
  code: string
  name: string
  stockQuantity: number
}

export interface RawMaterialInput {
  code: string
  name: string
  stockQuantity: number
}

export interface ProductMaterial {
  id: number
  product: Product
  rawMaterial: RawMaterial
  quantityRequired: number
}

export interface ProductMaterialInput {
  productId: number
  rawMaterialId: number
  quantityRequired: number
}

export interface ProductionSuggestionItem {
  productId: number
  productCode: string
  productName: string
  productPrice: number
  quantityToProduce: number
  totalValue: number
}

export interface ProductionSuggestionResponse {
  items: ProductionSuggestionItem[]
  totalValue: number
}
