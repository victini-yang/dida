import router from "@/router";
import { useLoginUserStore } from "@/store/userStore";
import ACCESS_ENUM from "@/access/accessEnum";
import checkAccess from "@/access/checkAccess";

router.beforeEach(async (to, from, next) => {
  //   获取当前登录用户
  const longinUserStore = useLoginUserStore();
  // const 是常量无法修改。这里不能用 const
  let loginUser = longinUserStore.loginUser;

  // 如果之前没有尝试过获取登录用户信息，则尝试获取
  if (!loginUser || !loginUser.userRole) {
    // 加await 是因为需要等待登陆成功并获取用户信息完成后，在执行后续操作
    await longinUserStore.fetchLoginUser();
    loginUser = longinUserStore.loginUser;
  }

  //   当前页面需要的权限
  const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.NOT_LOGIN;
  // 要跳转的页面必须登录
  if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
    //   如果没有登陆
    if (
      !loginUser ||
      !loginUser.userRole ||
      loginUser.userRole === ACCESS_ENUM.NOT_LOGIN
    ) {
      // 跳转到登录页面并 redirect 标识携带从哪个地址跳转的
      next(`/user/login?redirect=${to.fullPath}`);
    }
    //如果已经登陆了，判断权限是否足够，如果不足，则跳转到无权限页面
    if (!checkAccess(loginUser, needAccess)) {
      next("/noAuth");
      return;
    }
  }
  next();
});
