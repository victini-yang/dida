import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import ArcoVue from "@arco-design/web-vue";
import { createPinia } from "pinia";
import "@arco-design/web-vue/dist/arco.css";
import "@/access";

// 全局页面入口
const pinia = createPinia();
createApp(App).use(pinia).use(ArcoVue).use(router).mount("#app");
