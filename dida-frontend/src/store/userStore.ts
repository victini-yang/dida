import { defineStore } from "pinia";
import { ref } from "vue";
import { getLoginUserUsingGet } from "@/api/userController";
import ACCESS_ENUM from "@/access/accessEnum";

/**
 * 登录用户信息全局状态
 */
export const useLoginUserStore = defineStore("loginUser", () => {
  const loginUser = ref<API.LoginUserVO>({
    // 每次刷新页面、进入页面尝试登录
    userName: "未登录",
  });

  function setLoginUser(newLoginUser: API.LoginUserVO) {
    loginUser.value = newLoginUser;
  }

  // 刷新页面时，尝试获取登录用户信息，
  // 没有尝试登录 userRole 就为空，
  // 尝试过但失败了 userRole 就是NOT_LOGIN
  async function fetchLoginUser() {
    const res = await getLoginUserUsingGet();
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data;
    } else {
      loginUser.value = { userRole: ACCESS_ENUM.NOT_LOGIN };
      // setTimeout(() => {
      //   loginUser.value = { userRole: ACCESS_ENUM.ADMIN, id: 1 };
      // }, 3000);
    }
  }

  return { loginUser, setLoginUser, fetchLoginUser };
});
