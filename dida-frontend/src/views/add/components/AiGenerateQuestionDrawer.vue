<template>
  <a-button type="outline" @click="handleClick">AI 生成题目</a-button>
  <a-drawer
    :width="340"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    unmountOnClose
  >
    <template #title>AI 生成题目</template>
    <div>
      <a-form
        :model="form"
        label-align="left"
        auto-label-width
        @submit="handleSubmit"
      >
        <a-form-item label="应用 id">
          {{ appId }}
        </a-form-item>
        <a-form-item label="应用名称"> {{ app.appName }}</a-form-item>
        <a-form-item field="questionNumber" label="题目数量">
          <a-input-number
            :min="0"
            :max="20"
            :v-model="app.appName"
            placeholder="请输入你的题目数量"
          />
        </a-form-item>
        <a-form-item field="optionNumber" label="选项数量">
          <a-input-number
            :min="0"
            :max="6"
            :v-model="app.appDesc"
            placeholder="请输入你的选项数量"
          />
        </a-form-item>
        <a-form-item>
          <a-button
            :loading="submitting"
            type="primary"
            html-type="submit"
            style="width: 120px"
          >
            {{ submitting ? "生成中" : "一键生成" }}
          </a-button>
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { reactive, ref, watchEffect } from "vue";
import API from "@/api";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { getAppVoByIdUsingGet } from "@/api/appController";

import { withDefaults, defineProps } from "vue";
import { aiGenerateQuestionUsingPost } from "@/api/questionController";

// 定义属性
interface Props {
  appId: string;
  onSuccess?: (result: API.QuestionContentDTO[]) => void;
}

// 定义属性初始值，获取到外层传递的属性，
const props = withDefaults(defineProps<Props>(), {
  appId: () => {
    return "";
  },
});

// 获取app
const app = ref<API.AppVO>({});

const form = reactive({
  optionNumber: 2,
  questionNumber: 10,
} as API.AiGenerateQuestionRequest);

const visible = ref(false);
const submitting = ref(false);

const handleClick = () => {
  visible.value = true;
};
const handleOk = () => {
  visible.value = false;
};
const handleCancel = () => {
  visible.value = false;
};

/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.appId) {
    return;
  }
  // 获取 appvo
  let res: any = await getAppVoByIdUsingGet({
    id: props.appId as any,
  });
  if (res.data.code === 0) {
    app.value = res.data.data as any;
  } else {
    message.error("获取应用失败，" + res.data.message);
  }
};
// 获取旧数据
watchEffect(() => loadData());

/**
 * 提交创建
 */
const handleSubmit = async () => {
  if (!props.appId) {
    return;
  }
  submitting.value = true;
  //   如果是修改
  const res = await aiGenerateQuestionUsingPost({
    // 传入id、表单项
    appId: props.appId as any,
    ...form,
  });
  //   获取创建信息，提示创建成功
  if (res.data.code === 0 && res.data.data.length > 0) {
    if (props.onSuccess) {
      props.onSuccess(res.data.data);
    } else {
      message.success("生成成功");
    }
    handleCancel();
  } else {
    message.error("操作失败，" + res.data.message);
  }
  submitting.value = false;
};
</script>
