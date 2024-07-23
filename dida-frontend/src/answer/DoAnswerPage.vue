<!--嵌套表单、响应式表单、动态增减表单项-->
<template>
  <div id="doAnswerPage">
    <a-card>
      <h1>{{ app.appName }}</h1>
      <p>{{ app.appDesc }}</p>
      <h2 style="margin-bottom: 32px">
        {{ current }}、{{ currentQuestion?.title }}
      </h2>
      <!--a-radio-group不会记录历史选项值，需要@change="doRadioChange"-->
      <div>
        <a-radio-group
          direction="vertical"
          v-model="currentAnswer"
          :options="questionOptions"
          @change="doRadioChange"
        />
        <div style="margin-top: 24px">
          <a-space size="large">
            <a-button
              type="primary"
              circle
              v-if="current < questionContent.length"
              :disabled="!currentAnswer"
              @click="current += 1"
            >
              下一题
            </a-button>
            <a-button
              type="primary"
              v-if="current === questionContent.length"
              circle
              :disabled="!currentAnswer"
              @click="doSubmit"
            >
              查看结果
            </a-button>
            <a-button v-if="current > 1" circle @click="current -= 1">
              上一题
            </a-button>
          </a-space>
        </div>
      </div>
    </a-card>
  </div>
</template>

<!--行为-->
<script setup lang="ts">
import { computed, reactive, ref, watchEffect } from "vue";
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
import {
  editQuestionUsingPost,
  listQuestionVoByPageUsingPost,
} from "@/api/questionController";
import { addUserAnswerUsingPost } from "@/api/userAnswerController";

// 定义属性
interface Props {
  appId: string;
}

// 定义属性初始值，获取到外层传递的属性，
const props = withDefaults(defineProps<Props>(), {
  appId: () => {
    return "";
  },
});

const router = useRouter();

// 定义题目结构（理解为题目列表）
const questionContent = ref<API.QuestionContentDTO[]>([]);
// 获取app
const app = ref<API.AppVO>({});

// 当前题目的序号（从1开始）
const current = ref(1);
// 当前题目
const currentQuestion = ref<API.QuestionContentDTO>({});
// 当前题目选项 如果存在就遍历成为 前端展示选项的radio组件可接受的 选项数组
const questionOptions = computed(() => {
  return currentQuestion.value?.options
    ? currentQuestion.value.options.map((option) => {
        return {
          label: `${option.key}. ${option.value}`,
          value: option.key,
        };
      })
    : [];
});
// 当前答案
const currentAnswer = ref<string>();
// 回答列表
const answerList = reactive<string[]>([]);
/**
 *  * 添加题目，0：不删除即添加
 * @param index 可以指定第几题
 */
const addQuestion = (index: number) => {
  questionContent.value.splice(index, 0, {
    title: "",
    options: [],
  });
};

/**
 * 删除题目，1：删除数量
 * @param index 可以指定第几题
 */
const deleteQuestion = (index: number) => {
  questionContent.value.splice(index, 1);
};

/**
 * 添加题目选项，0：不删除即添加
 * @param index 可以指定第几个选项
 * @param question 获取当前题目
 */
const addQuestionOption = (question: API.QuestionConentDTO, index: number) => {
  if (!question.options) {
    question.options = [];
  }
  question.options.splice(index, 0, {
    key: "",
    value: "",
  });
};

/**
 * 删除题目选项，1：删除数量
 * @param index 可以指定第几个选项
 * @param question 获取当前题目
 */
const deleteQuestionOption = (
  question: API.QuestionConentDTO,
  index: number
) => {
  if (!question.options) {
    question.options = [];
  }
  question.options.splice(index, 1);
};

const oldQuestion = ref<API.QuestionVO>();

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
  // 获取 QuestionVo
  res = await listQuestionVoByPageUsingPost({
    appId: props.appId as any,
    current: 1,
    pageSize: 1,
    sortField: "createTime",
    sortOrder: "descend",
  });

  if (res.data.code === 0 && res.data.data?.records) {
    // 获取登录用户信息，提示添加成功
    questionContent.value = res.data.data?.records[0].questionContent;
  } else {
    message.error("获取题目失败，" + res.data.message);
  }
};
// 获取旧数据
watchEffect(() => loadData());

// 监听current的变化，改变current会自动更新当前题目和答案
// answerList是reactive属性，不用.value
watchEffect(() => {
  currentQuestion.value = questionContent.value[current.value - 1];
  currentAnswer.value = answerList[current.value - 1];
});

/**
 * 选中选项后，保存选择记录
 * @param value
 */
const doRadioChange = (value: string) => {
  answerList[current.value - 1] = value;
};

/**
 * 提交创建
 */
const doSubmit = async () => {
  if (!props.appId || !answerList) {
    return;
  }
  const res = await addUserAnswerUsingPost({
    // 传入id、内容
    appId: props.appId as any,
    choices: answerList,
  });
  //   获取创建信息，提示创建成功
  if (res.data.code === 0 && res.data.data) {
    router.push(`/answer/result/${res.data.data}`);
  } else {
    message.error("提交答案失败，" + res.data.message);
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
