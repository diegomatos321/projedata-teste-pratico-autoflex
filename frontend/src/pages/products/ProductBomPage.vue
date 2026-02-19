<template>
  <section>
    <div class="d-flex flex-column flex-sm-row ga-3 align-sm-end justify-space-between">
      <div>
        <h2 class="text-h5 font-weight-bold">Bill of Materials</h2>
        <p class="text-medium-emphasis">
          {{ productLabel || 'Select a product to manage its materials.' }}
        </p>
      </div>
      <v-btn variant="text" @click="goBack">Back</v-btn>
    </div>

    <v-alert v-if="errorMessage" class="mt-4" type="error" variant="tonal">
      {{ errorMessage }}
    </v-alert>

    <v-card class="mt-4" variant="outlined">
      <v-card-text>
        <form
          class="d-grid"
          style="gap: 12px; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr))"
          @submit.prevent="saveBom"
        >
          <v-select
            v-model="bomForm.rawMaterialId"
            label="Raw Material"
            variant="outlined"
            density="comfortable"
            :items="rawMaterialOptions"
            item-title="label"
            item-value="value"
            :error-messages="bomErrors.rawMaterialId ? [bomErrors.rawMaterialId] : []"
          />

          <v-text-field
            v-model="bomForm.quantityRequired"
            type="number"
            label="Quantity Required"
            variant="outlined"
            density="comfortable"
            :error-messages="bomErrors.quantityRequired ? [bomErrors.quantityRequired] : []"
          />

          <div class="d-flex align-end">
            <v-btn color="primary" type="submit" :loading="bomStore.loading">
              {{ bomEditingId ? 'Update Association' : 'Add Association' }}
            </v-btn>
          </div>
        </form>
      </v-card-text>
    </v-card>

    <v-alert v-if="bomStore.error" class="mt-4" type="error" variant="tonal">
      {{ bomStore.error }}
    </v-alert>

    <div class="mt-4">
      <DataTable
        :columns="bomColumns"
        :rows="bomRows"
        row-key="id"
        :loading="bomStore.loading"
        empty-text="No BOM associations"
      >
        <template #cell-actions="{ row }">
          <div class="d-flex ga-2">
            <v-btn variant="text" size="small" @click="openEditBom(Number(row.id))">Edit</v-btn>
            <v-btn variant="text" size="small" color="error" @click="askDeleteBom(Number(row.id))">
              Delete
            </v-btn>
          </div>
        </template>
      </DataTable>
    </div>

    <v-dialog
      :model-value="confirmDeleteOpen"
      max-width="460"
      @update:model-value="onConfirmChange"
    >
      <v-card>
        <v-card-title>Confirmation</v-card-title>
        <v-card-text>This action will remove the BOM association. Continue?</v-card-text>
        <v-card-actions class="justify-end">
          <v-btn variant="text" @click="confirmDeleteOpen = false">Cancel</v-btn>
          <v-btn color="error" variant="flat" @click="confirmDelete">Confirm</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import DataTable from '@/components/DataTable.vue'
import { useToast } from '@/composables/useToast'
import { useBomStore } from '@/stores/bomStore'
import { useProductStore } from '@/stores/productStore'
import { useRawMaterialStore } from '@/stores/rawMaterialStore'

interface BomForm {
  rawMaterialId: string
  quantityRequired: string
}

const route = useRoute()
const router = useRouter()
const productStore = useProductStore()
const rawMaterialStore = useRawMaterialStore()
const bomStore = useBomStore()
const { show } = useToast()

const productId = computed(() => Number(route.params.id))
const errorMessage = ref('')

const bomForm = reactive<BomForm>({
  rawMaterialId: '',
  quantityRequired: '',
})

const bomErrors = reactive<BomForm>({
  rawMaterialId: '',
  quantityRequired: '',
})

const bomEditingId = ref<number | null>(null)
const confirmDeleteOpen = ref(false)
const deleteBomId = ref<number | null>(null)

const bomColumns = [
  { key: 'rawMaterialCode', label: 'Raw Material Code' },
  { key: 'rawMaterialName', label: 'Raw Material Name' },
  { key: 'quantityRequired', label: 'Quantity Required' },
  { key: 'actions', label: 'Actions' },
]

const productLabel = computed(() => {
  const product = productStore.products.find((item) => item.id === productId.value)
  return product ? `${product.code} - ${product.name}` : ''
})

const rawMaterialOptions = computed(() => {
  return rawMaterialStore.rawMaterials.map((item) => ({
    label: `${item.code} - ${item.name}`,
    value: String(item.id),
  }))
})

const bomRows = computed(() => {
  return bomStore.productMaterials.map((association) => {
    console.dir(association)
    return {
      id: association.id,
      rawMaterialCode: association.rawMaterial?.code ?? 'Unknown code',
      rawMaterialName: association.rawMaterial?.name ?? 'Unknown material',
      quantityRequired: association.quantityRequired,
      actions: '',
    }
  })
})

onMounted(async () => {
  if (!productStore.products.length) {
    await productStore.fetchProducts()
  }
  if (!rawMaterialStore.rawMaterials.length) {
    await rawMaterialStore.fetchRawMaterials()
  }

  const product = productStore.products.find((item) => item.id === productId.value)
  if (!product) {
    errorMessage.value = 'Product not found.'
    return
  }

  await bomStore.fetchByProduct(productId.value)
})

function resetForm() {
  bomForm.rawMaterialId = ''
  bomForm.quantityRequired = ''
  bomErrors.rawMaterialId = ''
  bomErrors.quantityRequired = ''
}

function validateBom(): boolean {
  bomErrors.rawMaterialId = bomForm.rawMaterialId ? '' : 'Raw material is required'

  const quantity = Number(bomForm.quantityRequired)
  bomErrors.quantityRequired =
    Number.isFinite(quantity) && quantity > 0 ? '' : 'Quantity must be greater than zero'

  return !bomErrors.rawMaterialId && !bomErrors.quantityRequired
}

async function saveBom() {
  if (!validateBom()) {
    return
  }

  const payload = {
    productId: productId.value,
    rawMaterialId: Number(bomForm.rawMaterialId),
    quantityRequired: Number(bomForm.quantityRequired),
  }

  try {
    if (bomEditingId.value) {
      await bomStore.updateAssociation(bomEditingId.value, payload)
      show('BOM association updated', 'success')
    } else {
      await bomStore.createAssociation(payload)
      show('BOM association added', 'success')
    }

    bomEditingId.value = null
    resetForm()
  } catch {
    show('Unable to save BOM association', 'error')
  }
}

function openEditBom(associationId: number) {
  const association = bomStore.productMaterials.find((item) => item.id === associationId)
  if (!association) {
    return
  }

  bomEditingId.value = associationId
  bomForm.rawMaterialId = String(association.rawMaterial.id)
  bomForm.quantityRequired = String(association.quantityRequired)
  bomErrors.rawMaterialId = ''
  bomErrors.quantityRequired = ''
}

function askDeleteBom(associationId: number) {
  deleteBomId.value = associationId
  confirmDeleteOpen.value = true
}

function onConfirmChange(value: boolean) {
  if (!value) {
    confirmDeleteOpen.value = false
  }
}

async function confirmDelete() {
  if (!deleteBomId.value) {
    return
  }

  try {
    await bomStore.deleteAssociation(deleteBomId.value)
    show('BOM association deleted', 'success')
  } catch {
    show('Unable to delete BOM association', 'error')
  } finally {
    deleteBomId.value = null
    confirmDeleteOpen.value = false
  }
}

function goBack() {
  void router.push('/products')
}
</script>
