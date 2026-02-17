<template>
  <v-app>
    <v-navigation-drawer v-model="sidebarOpen" :permanent="!isMobile" width="260">
      <v-list-item title="Stock & Production" subtitle="Management" />
      <v-divider class="my-2" />
      <v-list nav density="comfortable">
        <v-list-item
          v-for="item in navItems"
          :key="item.to"
          :to="item.to"
          :title="item.label"
          rounded="lg"
        />
      </v-list>
    </v-navigation-drawer>

    <v-app-bar elevation="1">
      <v-app-bar-nav-icon v-if="isMobile" @click="sidebarOpen = !sidebarOpen" />
      <v-app-bar-title>Inventory Management Panel</v-app-bar-title>
    </v-app-bar>

    <v-main>
      <v-container fluid class="py-6">
        <RouterView />
      </v-container>
    </v-main>

    <ToastList />
  </v-app>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue'
import { RouterView } from 'vue-router'
import ToastList from '@/components/ToastList.vue'

const sidebarOpen = ref(false)
const isMobile = ref(false)

const navItems = [
  { to: '/', label: 'Dashboard' },
  { to: '/products', label: 'Products' },
  { to: '/raw-materials', label: 'Raw Materials' },
  { to: '/production', label: 'Production' },
]

function syncLayoutState() {
  isMobile.value = window.innerWidth < 1024
  sidebarOpen.value = !isMobile.value
}

onMounted(() => {
  syncLayoutState()
  window.addEventListener('resize', syncLayoutState)
})

onUnmounted(() => {
  window.removeEventListener('resize', syncLayoutState)
})
</script>
