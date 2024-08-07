<!--内容-->
<template>
  <div class="homePage">
    <div class="searchBar">
      <a-input-search
        :style="{ width: '320px' }"
        placeholder="快速发现答题应用"
        button-text="搜索"
        size="large"
        search-button
      />
    </div>
    <div>
      <a-list
        class="list-demo-action-layout"
        :grid-props="{ gutter: [20, 20], sm: 24, md: 12, lg: 8, xl: 6 }"
        :bordered="false"
        :data="dataList"
        :pagination-props="{
          current: searchParams.current,
          pageSize: searchParams.pageSize,
          total,
        }"
        @page-change="onPageChange"
      >
        <template #item="{ item }">
          <app-card :app="item" />
        </template>
      </a-list>
    </div>
  </div>
</template>

<!--行为-->
<script setup lang="ts">
import { reactive, ref, watchEffect } from "vue";
import AppCard from "@/components/AppCard.vue";
import API from "@/api";
import { listAppVoByPageUsingPost } from "@/api/appController";
import message from "@arco-design/web-vue/es/message";
import { REVIEW_STATUS_ENUM } from "@/constant/app";
import { on } from "@arco-design/web-vue/es/_utils/dom";
import ShareModel from "@/components/ShareModal.vue";

// 初始化搜索条件（不应该被修改）
const initSearchParams = {
  current: 1,
  pageSize: 12,
};

// 定义存储数据列表、存储总数、搜索条件
const searchParams = ref<API.AppQueryRequest>({
  ...initSearchParams,
});
const dataList = ref<API.AppVO[]>([]);
const total = ref<number>(0);

/**
 * 加载数据
 */
const loadData = async () => {
  const params = {
    reviewStatus: REVIEW_STATUS_ENUM.PASS,
    ...searchParams.value,
  };
  // 获取appvo
  const res = await listAppVoByPageUsingPost(params);
  if (res.data.code === 0) {
    dataList.value = res.data.data?.records || [];
    total.value = res.data.data?.total || 0;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

/**
 * 当分页变化时，改变搜索条件，触发数据加载
 * @param page
 */
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

/**
 * 监听searchParams变量，改变时出发数据的重新加载
 */
watchEffect(() => {
  loadData();
});
</script>

<!--样式-->
<style scoped>
#homePage {
}

.searchBar {
  margin-bottom: 28px;
  text-align: center;
}

.list-demo-action-layout .image-area {
  width: 183px;
  height: 119px;
  overflow: hidden;
  border-radius: 2px;
}

.list-demo-action-layout .list-demo-item {
  padding: 20px 0;
  border-bottom: 1px solid var(--color-fill-3);
}

.list-demo-action-layout .image-area img {
  width: 100%;
}

.list-demo-action-layout .arco-list-item-action .arco-icon {
  margin: 0 4px;
}
</style>
