<template>
  <a-card class="appCard" hoverable @click="doCardClick">
    <template #actions>
      <!--      点赞图标<span class="icon-hover"> <IconThumbUp /> </span>-->
      <span class="icon-hover"> <IconShareInternal /> </span>
    </template>
    <template #cover>
      <div
        :style="{
          height: '204px',
          overflow: 'hidden',
        }"
      >
        <img
          :style="{ width: '100%', transform: 'translateY(-20px)' }"
          :alt="app.appName"
          :src="app.appIcon"
        />
      </div>
    </template>
    <a-card-meta :title="app.appName" :description="app.appDesc">
      <template #avatar>
        <div
          :style="{ display: 'flex', alignItems: 'center', color: '#1D2129' }"
        >
          <a-avatar
            :size="24"
            :image-url="app.user?.userAvatar"
            :style="{ marginRight: '8px' }"
          />
          <a-typography-text
            >{{ app.user?.userName ?? "无名" }}
          </a-typography-text>
        </div>
      </template>
    </a-card-meta>
  </a-card>
</template>

<script setup lang="ts">
import {
  IconThumbUp,
  IconShareInternal,
  IconMore,
} from "@arco-design/web-vue/es/icon";
import API from "@/api";
import { withDefaults, defineProps } from "vue";
import { useRouter } from "vue-router";

// 定义属性
interface Props {
  app: API.AppVO;
}

// 定义属性初始值，获取到外层传递的属性，
const props = withDefaults(defineProps<Props>(), {
  app: () => {
    return {};
  },
});

// 点击图片跳转,引入路由
const router = useRouter();
const doCardClick = () => {
  //   跳转各自的路由，属性用 props
  router.push(`/app/detail/${props.app.id}`);
};
</script>
<style scoped>
/**
 * 当鼠标移动到app图标上时，改变鼠标样式
 */
.appCard {
  cursor: pointer;
}

.icon-hover {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  transition: all 0.1s;
}

.icon-hover:hover {
  background-color: rgb(var(--gray-2));
}
</style>
