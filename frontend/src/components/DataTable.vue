<template>
  <v-card variant="outlined">
    <v-progress-linear v-if="loading" indeterminate color="primary" />
    <v-table>
      <thead>
        <tr>
          <th v-for="column in columns" :key="column.key">{{ column.label }}</th>
        </tr>
      </thead>
      <tbody v-if="rows.length">
        <tr v-for="row in rows" :key="String(row[rowKey])">
          <td v-for="column in columns" :key="column.key">
            <slot :name="`cell-${column.key}`" :row="row">
              {{ row[column.key] }}
            </slot>
          </td>
        </tr>
      </tbody>
    </v-table>
    <div v-if="!loading && !rows.length" class="pa-6 text-center text-medium-emphasis">
      {{ emptyText }}
    </div>
  </v-card>
</template>

<script setup lang="ts">
interface Column {
  key: string
  label: string
}

interface Props {
  columns: Column[]
  rows: Record<string, string | number | null>[]
  rowKey: string
  loading?: boolean
  emptyText?: string
}

withDefaults(defineProps<Props>(), {
  loading: false,
  emptyText: 'No data found',
})
</script>
