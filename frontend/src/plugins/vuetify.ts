import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'

import { createVuetify } from 'vuetify'

const vuetify = createVuetify({
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          primary: '#1e293b',
          secondary: '#475569',
          error: '#dc2626',
          success: '#16a34a',
          background: '#f8fafc',
          surface: '#ffffff',
        },
      },
    },
  },
})

export default vuetify
