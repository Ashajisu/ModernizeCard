// vite.config.ts
import { fileURLToPath, URL } from "node:url";
import { defineConfig } from "file:///D:/Works/arisys/zoom%EA%B4%80%EB%A0%A8/%EC%A4%8C%ED%86%B5%ED%95%A9%ED%86%B5%EA%B3%84_%EA%B4%80%EB%A0%A8/zoom/modernize/node_modules/vite/dist/node/index.js";
import vue from "file:///D:/Works/arisys/zoom%EA%B4%80%EB%A0%A8/%EC%A4%8C%ED%86%B5%ED%95%A9%ED%86%B5%EA%B3%84_%EA%B4%80%EB%A0%A8/zoom/modernize/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import vuetify from "file:///D:/Works/arisys/zoom%EA%B4%80%EB%A0%A8/%EC%A4%8C%ED%86%B5%ED%95%A9%ED%86%B5%EA%B3%84_%EA%B4%80%EB%A0%A8/zoom/modernize/node_modules/vite-plugin-vuetify/dist/index.mjs";
var __vite_injected_original_import_meta_url = "file:///D:/Works/arisys/zoom%EA%B4%80%EB%A0%A8/%EC%A4%8C%ED%86%B5%ED%95%A9%ED%86%B5%EA%B3%84_%EA%B4%80%EB%A0%A8/zoom/modernize/vite.config.ts";
var vite_config_default = defineConfig({
  plugins: [
    vue(),
    vuetify({
      autoImport: true
    })
  ],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", __vite_injected_original_import_meta_url))
    }
  },
  server: {
    proxy: {
      // "/api"로 시작하는 요청을 프록시 처리
      "/api": {
        // 백엔드 서버 주소
        target: "http://localhost:8080",
        // CORS 문제 해결을 위해 필요
        changeOrigin: true,
        // 경로에서 '/api' 제거
        rewrite: (path) => path.replace(/^\/api/, "")
      }
    }
  },
  build: {
    outDir: "../src/main/resources/static",
    chunkSizeWarningLimit: 5e3,
    rollupOptions: {
      output: {
        entryFileNames: "assets/[name].js",
        chunkFileNames: "assets/[name].js",
        assetFileNames: "assets/[name].[ext]"
      }
    }
  },
  css: {
    preprocessorOptions: {
      scss: {}
    }
  },
  optimizeDeps: {
    exclude: ["vuetify"],
    entries: [
      "./src/**/*.vue"
    ]
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcudHMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJEOlxcXFxXb3Jrc1xcXFxhcmlzeXNcXFxcem9vbVx1QUQwMFx1QjgyOFxcXFxcdUM5MENcdUQxQjVcdUQ1NjlcdUQxQjVcdUFDQzRfXHVBRDAwXHVCODI4XFxcXHpvb21cXFxcbW9kZXJuaXplXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJEOlxcXFxXb3Jrc1xcXFxhcmlzeXNcXFxcem9vbVx1QUQwMFx1QjgyOFxcXFxcdUM5MENcdUQxQjVcdUQ1NjlcdUQxQjVcdUFDQzRfXHVBRDAwXHVCODI4XFxcXHpvb21cXFxcbW9kZXJuaXplXFxcXHZpdGUuY29uZmlnLnRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9EOi9Xb3Jrcy9hcmlzeXMvem9vbSVFQSVCNCU4MCVFQiVBMCVBOC8lRUMlQTQlOEMlRUQlODYlQjUlRUQlOTUlQTklRUQlODYlQjUlRUElQjMlODRfJUVBJUI0JTgwJUVCJUEwJUE4L3pvb20vbW9kZXJuaXplL3ZpdGUuY29uZmlnLnRzXCI7XHJcblxyXG5pbXBvcnQgeyBmaWxlVVJMVG9QYXRoLCBVUkwgfSBmcm9tICdub2RlOnVybCdcclxuXHJcbmltcG9ydCB7IGRlZmluZUNvbmZpZyB9IGZyb20gJ3ZpdGUnXHJcbmltcG9ydCB2dWUgZnJvbSAnQHZpdGVqcy9wbHVnaW4tdnVlJ1xyXG5pbXBvcnQgdnVldGlmeSBmcm9tIFwidml0ZS1wbHVnaW4tdnVldGlmeVwiO1xyXG5cclxuLy8gaHR0cHM6Ly92aXRlLmRldi9jb25maWcvXHJcbmV4cG9ydCBkZWZhdWx0IGRlZmluZUNvbmZpZyh7XHJcbiAgcGx1Z2luczogW1xyXG4gICAgdnVlKCksXHJcbiAgICB2dWV0aWZ5KHtcclxuICAgICAgYXV0b0ltcG9ydDogdHJ1ZSxcclxuICAgIH0pLFxyXG4gIF0sXHJcbiAgcmVzb2x2ZToge1xyXG4gICAgYWxpYXM6IHtcclxuICAgICAgJ0AnOiBmaWxlVVJMVG9QYXRoKG5ldyBVUkwoJy4vc3JjJywgaW1wb3J0Lm1ldGEudXJsKSlcclxuICAgIH1cclxuICB9LFxyXG4gIHNlcnZlcjoge1xyXG4gICAgcHJveHk6IHtcclxuICAgICAgLy8gXCIvYXBpXCJcdUI4NUMgXHVDMkRDXHVDNzkxXHVENTU4XHVCMjk0IFx1QzY5NFx1Q0NBRFx1Qzc0NCBcdUQ1MDRcdUI4NURcdUMyREMgXHVDQzk4XHVCOUFDXHJcbiAgICAgICcvYXBpJzoge1xyXG4gICAgICAgIC8vIFx1QkMzMVx1QzVENFx1QjREQyBcdUMxMUNcdUJDODQgXHVDOEZDXHVDMThDXHJcbiAgICAgICAgdGFyZ2V0OiAnaHR0cDovL2xvY2FsaG9zdDo4MDgwJyxcclxuICAgICAgICAvLyBDT1JTIFx1QkIzOFx1QzgxQyBcdUQ1NzRcdUFDQjBcdUM3NDQgXHVDNzA0XHVENTc0IFx1RDU0NFx1QzY5NFxyXG4gICAgICAgIGNoYW5nZU9yaWdpbjogdHJ1ZSxcclxuICAgICAgICAvLyBcdUFDQkRcdUI4NUNcdUM1RDBcdUMxMUMgJy9hcGknIFx1QzgxQ1x1QUM3MFxyXG4gICAgICAgIHJld3JpdGU6IChwYXRoKSA9PiBwYXRoLnJlcGxhY2UoL15cXC9hcGkvLCAnJylcclxuICAgICAgfVxyXG4gICAgfVxyXG4gIH0sXHJcbiAgYnVpbGQ6IHtcclxuICAgIG91dERpciA6ICcuLi9zcmMvbWFpbi9yZXNvdXJjZXMvc3RhdGljJyxcclxuICAgIGNodW5rU2l6ZVdhcm5pbmdMaW1pdDogNTAwMCxcclxuICAgIHJvbGx1cE9wdGlvbnM6IHtcclxuICAgICAgb3V0cHV0OiB7XHJcbiAgICAgICAgZW50cnlGaWxlTmFtZXM6IFwiYXNzZXRzL1tuYW1lXS5qc1wiLFxyXG4gICAgICAgIGNodW5rRmlsZU5hbWVzOiBcImFzc2V0cy9bbmFtZV0uanNcIixcclxuICAgICAgICBhc3NldEZpbGVOYW1lczogXCJhc3NldHMvW25hbWVdLltleHRdXCIsXHJcbiAgICAgIH0sXHJcbiAgICB9LFxyXG4gIH0sXHJcbiAgY3NzOiB7XHJcbiAgICBwcmVwcm9jZXNzb3JPcHRpb25zOiB7XHJcbiAgICAgIHNjc3M6IHt9LFxyXG4gICAgfSxcclxuICB9LFxyXG4gIG9wdGltaXplRGVwczoge1xyXG4gICAgZXhjbHVkZTogWyd2dWV0aWZ5J10sXHJcbiAgICBlbnRyaWVzOiBbXHJcbiAgICAgICcuL3NyYy8qKi8qLnZ1ZScsXHJcbiAgICBdLFxyXG4gIH0sXHJcbn0pXHJcbiJdLAogICJtYXBwaW5ncyI6ICI7QUFFQSxTQUFTLGVBQWUsV0FBVztBQUVuQyxTQUFTLG9CQUFvQjtBQUM3QixPQUFPLFNBQVM7QUFDaEIsT0FBTyxhQUFhO0FBTjZMLElBQU0sMkNBQTJDO0FBU2xRLElBQU8sc0JBQVEsYUFBYTtBQUFBLEVBQzFCLFNBQVM7QUFBQSxJQUNQLElBQUk7QUFBQSxJQUNKLFFBQVE7QUFBQSxNQUNOLFlBQVk7QUFBQSxJQUNkLENBQUM7QUFBQSxFQUNIO0FBQUEsRUFDQSxTQUFTO0FBQUEsSUFDUCxPQUFPO0FBQUEsTUFDTCxLQUFLLGNBQWMsSUFBSSxJQUFJLFNBQVMsd0NBQWUsQ0FBQztBQUFBLElBQ3REO0FBQUEsRUFDRjtBQUFBLEVBQ0EsUUFBUTtBQUFBLElBQ04sT0FBTztBQUFBO0FBQUEsTUFFTCxRQUFRO0FBQUE7QUFBQSxRQUVOLFFBQVE7QUFBQTtBQUFBLFFBRVIsY0FBYztBQUFBO0FBQUEsUUFFZCxTQUFTLENBQUMsU0FBUyxLQUFLLFFBQVEsVUFBVSxFQUFFO0FBQUEsTUFDOUM7QUFBQSxJQUNGO0FBQUEsRUFDRjtBQUFBLEVBQ0EsT0FBTztBQUFBLElBQ0wsUUFBUztBQUFBLElBQ1QsdUJBQXVCO0FBQUEsSUFDdkIsZUFBZTtBQUFBLE1BQ2IsUUFBUTtBQUFBLFFBQ04sZ0JBQWdCO0FBQUEsUUFDaEIsZ0JBQWdCO0FBQUEsUUFDaEIsZ0JBQWdCO0FBQUEsTUFDbEI7QUFBQSxJQUNGO0FBQUEsRUFDRjtBQUFBLEVBQ0EsS0FBSztBQUFBLElBQ0gscUJBQXFCO0FBQUEsTUFDbkIsTUFBTSxDQUFDO0FBQUEsSUFDVDtBQUFBLEVBQ0Y7QUFBQSxFQUNBLGNBQWM7QUFBQSxJQUNaLFNBQVMsQ0FBQyxTQUFTO0FBQUEsSUFDbkIsU0FBUztBQUFBLE1BQ1A7QUFBQSxJQUNGO0FBQUEsRUFDRjtBQUNGLENBQUM7IiwKICAibmFtZXMiOiBbXQp9Cg==
