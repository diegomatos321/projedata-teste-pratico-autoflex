<template>
  <section>
    <div class="d-flex flex-column flex-sm-row ga-3 align-sm-end justify-space-between">
      <div>
        <h2 class="text-h5 font-weight-bold">{{ title }}</h2>
        <p class="text-medium-emphasis">Fill the product information.</p>
      </div>
      <v-btn variant="text" @click="goBack">Back</v-btn>
    </div>

    <v-alert v-if="productStore.error" class="mt-4" type="error" variant="tonal">
      {{ productStore.error }}
    </v-alert>

    <v-card class="mt-4" variant="outlined">
      <v-card-text>
        <form class="d-flex flex-column ga-3" @submit.prevent="submit">
          <v-text-field
            v-model="form.code"
            label="Code"
            variant="outlined"
            density="comfortable"
            :error-messages="errors.code ? [errors.code] : []"
          />
          <v-text-field
            v-model="form.name"
            label="Name"
            variant="outlined"
            density="comfortable"
            :error-messages="errors.name ? [errors.name] : []"
          />
          <v-text-field
            v-model="form.price"
            type="number"
            label="Price"
            variant="outlined"
            density="comfortable"
            :error-messages="errors.price ? [errors.price] : []"
          />
          <div class="d-flex justify-end ga-2">
            <v-btn variant="text" @click="goBack">Cancel</v-btn>
            <v-btn color="primary" :loading="productStore.loading" type="submit">Save</v-btn>
          </div>
        </form>
      </v-card-text>
    </v-card>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useToast } from '@/composables/useToast'
import { useProductStore } from '@/stores/productStore'

interface ProductForm {
  code: string
  name: string
  price: string
}

const productStore = useProductStore()
const route = useRoute()
const router = useRouter()
const { show } = useToast()

const form = reactive<ProductForm>({
  code: '',
  name: '',
  price: '',
})

const errors = reactive<ProductForm>({
  code: '',
  name: '',
  price: '',
})

const productId = computed(() => {
  const value = route.params.id
  return value ? Number(value) : null
})

const isEdit = computed(() => productId.value !== null && !Number.isNaN(productId.value))

const title = computed(() => (isEdit.value ? 'Edit Product' : 'New Product'))

onMounted(async () => {
  if (!isEdit.value) {
    return
  }

  if (!productStore.products.length) {
    await productStore.fetchProducts()
  }

  const product = productStore.products.find((item) => item.id === productId.value)
  if (!product) {
    show('Product not found', 'error')
    void router.push('/products')
    return
  }

  form.code = product.code
  form.name = product.name
  form.price = String(product.price)
})

function validate(): boolean {
  errors.code = form.code.trim() ? '' : 'Code is required'
  errors.name = form.name.trim() ? '' : 'Name is required'

  const price = Number(form.price)
  errors.price = Number.isFinite(price) && price >= 0 ? '' : 'Enter a valid price'

  return !errors.code && !errors.name && !errors.price
}

async function submit() {
  if (!validate()) {
    return
  }

  const payload = {
    code: form.code.trim(),
    name: form.name.trim(),
    price: Number(form.price),
  }

  try {
    if (isEdit.value && productId.value !== null) {
      await productStore.updateProduct(productId.value, payload)
      show('Product updated successfully', 'success')
    } else {
      await productStore.createProduct(payload)
      show('Product created successfully', 'success')
    }

    void router.push('/products')
  } catch {
    show('Unable to save product', 'error')
  }
}

function goBack() {
  void router.push('/products')
}
</script>
