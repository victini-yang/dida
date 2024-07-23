<!--内容-->
<template>
  <div class="userLoginPage">
    <h2 style="margin-bottom: 32px">创建应用</h2>
    <a-form
      :model="form"
      :style="{ width: '480px' }"
      label-align="left"
      auto-label-width
      @submit="handleSubmit"
    >
      <a-form-item field="appName" label="应用名称">
        <a-input v-model="form.appName" placeholder="请输入你的应用名称" />
      </a-form-item>
      <a-form-item field="appDesc" label="应用描述">
        <a-input v-model="form.appDesc" placeholder="请输入你的应用描述" />
      </a-form-item>
      <!--      <a-form-item field="appIcon" label="应用图标">-->
      <!--        <PictureUploader-->
      <!--          :value="form.appIcon"-->
      <!--          :on-change="(value) => (form.appIcon = value)"-->
      <!--        />-->
      <!--      </a-form-item>-->
      <a-form-item field="appType" label="应用类型">
        <a-select
          v-model="form.appType"
          :style="{ width: '320px' }"
          placeholder="请选择应用类型"
        >
          <a-option
            v-for="(value, key) of APP_TYPE_MAP"
            :value="Number(key)"
            :label="value"
          />
        </a-select>
      </a-form-item>
      <a-form-item field="scoringStrategy" label="评分策略">
        <a-select
          v-model="form.scoringStrategy"
          :style="{ width: '320px' }"
          placeholder="请选择评分策略"
        >
          <a-option
            v-for="(value, key) of APP_SCORING_STRATEGY_MAP"
            :value="Number(key)"
            :label="value"
          />
        </a-select>
      </a-form-item>

      <a-form-item>
        <div
          style="
            display: flex;
            width: 100%;
            align-items: center;
            justify-content: space-between;
          "
        >
          <a-button type="primary" html-type="submit" style="width: 120px">
            提交
          </a-button>
          <a-link href="/user/register">新用户注册</a-link>
        </div>
      </a-form-item>
    </a-form>
  </div>
</template>

<!--行为-->
<script setup lang="ts">
import { ref, watchEffect } from "vue";
import API from "@/api";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import {
  addAppUsingPost,
  editAppUsingPost,
  getAppVoByIdUsingGet,
} from "@/api/appController";
import { APP_SCORING_STRATEGY_MAP, APP_TYPE_MAP } from "@/constant/app";

import { withDefaults, defineProps } from "vue";

// 定义属性
interface Props {
  id: string;
}

// 定义属性初始值，获取到外层传递的属性，
const props = withDefaults(defineProps<Props>(), {
  id: () => {
    return "";
  },
});

const router = useRouter();

const form = ref({
  appDesc: "",
  appIcon: "",
  appName: "",
  appType: 0,
  scoringStrategy: 0,
} as API.AppAddRequest);

const oldApp = ref<API.AppVO>();

/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  // 获取 appvo
  const res = await getAppVoByIdUsingGet({
    id: props.id as any,
  });
  if (res.data.code === 0 && res.data.data) {
    // 获取登录用户信息，提示添加成功
    oldApp.value = res.data.data as any;
    form.value = res.data.data;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

watchEffect(() => loadData());

/**
 * 提交创建
 */
const handleSubmit = async () => {
  let res: any;
  if (props.id) {
    //   如果是修改
    res = await editAppUsingPost({
      // 传入id、表单项
      id: props.id as any,
      ...form.value,
    });
  } else {
    //   调用创建
    res = await addAppUsingPost(form.value);
  }
  //   获取创建信息，提示创建成功
  if (res.data.code === 0) {
    message.success("操作成功，即将跳转到应用详情页");
    setTimeout(() => {
      router.push(`/app/detail/${props.id ?? res.data.data}`);
    }, 3000);
  } else {
    message.error("操作失败，" + res.data.message);
  }
};
</script>

<!--样式-->
<style scoped>
#home {
  padding: 20px;
  background-color: #f0f0f0;
}
</style>
