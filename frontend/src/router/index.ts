import { createRouter, createWebHistory } from 'vue-router'
import AppLayout from '@/layouts/AppLayout.vue'
import DashboardPage from '@/views/DashboardPage.vue'
import ProductsListPage from '@/views/products/ProductsListPage.vue'
import ProductFormPage from '@/views/products/ProductFormPage.vue'
import ProductBomPage from '@/views/products/ProductBomPage.vue'
import RawMaterialsListPage from '@/views/raw-materials/RawMaterialsListPage.vue'
import RawMaterialFormPage from '@/views/raw-materials/RawMaterialFormPage.vue'
import ProductionPage from '@/views/ProductionPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: AppLayout,
      children: [
        {
          path: '',
          name: 'dashboard',
          component: DashboardPage,
        },
        {
          path: 'products',
          name: 'products',
          component: ProductsListPage,
        },
        {
          path: 'products/new',
          name: 'products-new',
          component: ProductFormPage,
        },
        {
          path: 'products/:id/edit',
          name: 'products-edit',
          component: ProductFormPage,
        },
        {
          path: 'products/:id/bom',
          name: 'products-bom',
          component: ProductBomPage,
        },
        {
          path: 'raw-materials',
          name: 'raw-materials',
          component: RawMaterialsListPage,
        },
        {
          path: 'raw-materials/new',
          name: 'raw-materials-new',
          component: RawMaterialFormPage,
        },
        {
          path: 'raw-materials/:id/edit',
          name: 'raw-materials-edit',
          component: RawMaterialFormPage,
        },
        {
          path: 'production',
          name: 'production',
          component: ProductionPage,
        },
      ],
    },
  ],
})

export default router
