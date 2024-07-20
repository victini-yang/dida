import ACCESS_ENUM from "@/access/accessEnum";

/**
 * 检查用户权限
 * @param loginUser
 * @param needAccess
 */
const checkAccess = (
  loginUser: API.LoginUserVO,
  needAccess = ACCESS_ENUM.NOT_LOGIN
) => {
  // 如果用户要登录才能访问
  // 如果用户没有登陆，那么表示无权限。
  const loginUserAccess = loginUser?.userRole ?? ACCESS_ENUM.NOT_LOGIN;
  // 获取当前登录用户具有的权限，如果没有loginUser，则表示未登录。
  if (needAccess === ACCESS_ENUM.NOT_LOGIN) {
    return true;
  }
  //   如果用户要登录才能访问
  if (needAccess === ACCESS_ENUM.USER) {
    //   如果用户没有登陆，那么表示无权限。
    if (loginUserAccess === ACCESS_ENUM.NOT_LOGIN) {
      return false;
    }
  }
  //   如果管理员才能访问
  if (needAccess === ACCESS_ENUM.ADMIN) {
    // 如果不是管理员，表示无权限
    if (loginUserAccess !== ACCESS_ENUM.ADMIN) {
      return false;
    }
  }
  return true;
};

export default checkAccess;
