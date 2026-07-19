import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vuetify from "vite-plugin-vuetify";

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vuetify({
      autoImport: true,
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {
      // "/api"로 시작하는 요청을 프록시 처리 - 개발환경에서만 가능.
      '/api': {
        // 백엔드 서버 주소
        target: 'http://localhost:9090',
        // CORS 문제 해결을 위해 필요
        changeOrigin: true,
        // 경로에서 '/api' 제거
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    },
      allowedHosts: ['localhost','ashajisu.iptime.org','ashajisu.github.io','131.186.39.198']
  },
  base: '/ModernizeCard/',
  build: {
    outDir : '../dist',
    emptyOutDir: true,            // 해당 경로 이전 build 파일 삭제
    chunkSizeWarningLimit: 5000,
    rollupOptions: {
      output: {
          // 해시 활성화(권장)
          entryFileNames: 'assets/[name]-[hash].js',
          chunkFileNames: 'assets/[name]-[hash].js',
          assetFileNames: 'assets/[name]-[hash][extname]',
      },
    },
  },
  css: {
    preprocessorOptions: {
      scss: {},
    },
  },
  optimizeDeps: {
    exclude: ['vuetify'],
    entries: [
      './src/**/*.vue',
    ],
  },
})
