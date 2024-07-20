const { generateService } = require("@umijs/openapi");

generateService({
  // 引入全局响应拦截器
  requestLibPath: "import request from '@/request'",
  schemaPath: "http://localhost:8101/api/v2/api-docs",
  serversPath: "./src",
});
