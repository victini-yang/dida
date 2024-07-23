<!--嵌套表单、响应式表单、动态增减表单项-->
<template>
  <div id="addQuestionPage">
    <h2 style="margin-bottom: 32px">设置题目</h2>
    <a-form
      :model="questionContent"
      :style="{ width: '480px' }"
      label-align="left"
      auto-label-width
      @submit="handleSubmit"
    >
      <a-form-item label="应用 id">
        {{ appId }}
      </a-form-item>
      <a-form-item label="题目列表" :content-flex="false" :merge-props="false">
        <a-button @click="addQuestion(questionContent.length)">
          底部添加题目
        </a-button>
        <!--        遍历每道题目-->
        <div v-for="(question, index) in questionContent" :key="index">
          <a-space size="large">
            <h3>题目 {{ index + 1 }}</h3>
            <a-button size="small" @click="addQuestion(index + 1)"
              >添加题目
            </a-button>
            <a-button
              size="small"
              status="danger"
              @click="deleteQuestion(index)"
              >删除题目
            </a-button>
          </a-space>
          <a-form-item field="posts.post1" :label="`题目 ${index + 1} 标题`">
            <!--question.title：用来接收用户输入的变量名-->
            <a-input v-model="question.title" placeholder="请输入标题" />
          </a-form-item>
          <!--          题目选项-->
          <a-space size="large">
            <h4>题目 {{ index + 1 }} 选项列表</h4>

            <a-button
              size="small"
              @click="addQuestionOption(question, question.options.length)"
              >底部添加选项
            </a-button>
          </a-space>
          <a-form-item
            v-for="(option, optionIndex) in question.options"
            :key="optionIndex"
            :label="`选项 ${optionIndex + 1}`"
            :content-flex="false"
            :merge-props="false"
          >
            <a-form-item label="选项key">
              <a-input v-model="option.key" placeholder="请输入选项key" />
            </a-form-item>
            <a-form-item label="选项值">
              <a-input v-model="option.value" placeholder="请输入选项值" />
            </a-form-item>
            <a-form-item label="选项结果">
              <a-input v-model="option.result" placeholder="请输入选项结果" />
            </a-form-item>
            <a-form-item label="选项得分">
              <a-input-number
                v-model="option.score"
                placeholder="请输入选项得分"
              />
            </a-form-item>
            <a-space size="large">
              <a-button
                size="mini"
                @click="addQuestionOption(question, optionIndex + 1)"
                >添加选项
              </a-button>
              <a-button
                size="small"
                status="danger"
                @click="deleteQuestionOption(question, optionIndex as any)"
                >删除选项
              </a-button>
            </a-space>
          </a-form-item>
          <!--          题目选项结尾-->
        </div>
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
        </div>
      </a-form-item>
    </a-form>
  </div>
</template>

<!--行为-->
<script setup lang="ts">
import { reactive, ref, watchEffect } from "vue";
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
  // 获取 QuestionVo
  const res = await listQuestionVoByPageUsingPost({
    appId: props.appId as any,
    current: 1,
    pageSize: 1,
    sortField: "createTime",
    sortOrder: "descend",
  });
  if (res.data.code === 0 && res.data.data?.records) {
    oldQuestion.value = res.data.data?.records[0];
    // 获取登录用户信息，提示添加成功
    if (oldQuestion.value) {
      questionContent.value = oldQuestion.value.questionContent ?? [];
    }
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};
// 获取旧数据
watchEffect(() => loadData());

/**
 * 提交创建
 */
const handleSubmit = async () => {
  if (!props.appId || !questionContent.value) {
    return;
  }
  let res: any;
  //   如果是修改
  if (oldQuestion.value?.id) {
    res = await editQuestionUsingPost({
      // 传入id、内容
      id: oldQuestion.value.id,
      questionContent: questionContent.value,
    });
  }
  //   获取创建信息，提示创建成功
  if (res.data.code === 0) {
    message.success("操作成功，即将跳转到应用详情页");
    setTimeout(() => {
      router.push(`/app/detail/${props.appId}`);
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
