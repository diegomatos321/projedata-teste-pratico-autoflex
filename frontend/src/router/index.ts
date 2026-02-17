import { createRouter, createWebHistory } from 'vue-router'
import AppLayout from '@/layouts/AppLayout.vue'
import DashboardPage from '@/pages/DashboardPage.vue'
import ProductsListPage from '@/pages/products/ProductsListPage.vue'
import ProductFormPage from '@/pages/products/ProductFormPage.vue'
import ProductBomPage from '@/pages/products/ProductBomPage.vue'
import RawMaterialsListPage from '@/pages/raw-materials/RawMaterialsListPage.vue'
import RawMaterialFormPage from '@/pages/raw-materials/RawMaterialFormPage.vue'
import ProductionPage from '@/pages/ProductionPage.vue'

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
