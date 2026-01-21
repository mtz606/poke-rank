import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Vite config for GitHub Pages deployment under /poke-rank/
export default defineConfig({
  plugins: [react()],
  base: '/poke-rank/',
})


