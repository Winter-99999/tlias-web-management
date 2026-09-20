# tlias-web-management

Java Web 课程练习项目 —— 基于 Spring Boot + MyBatis 的「tlias」后台管理系统（学习进行中，功能持续补充）。

## 技术栈

| 分层 | 技术 |
| --- | --- |
| 框架 | Spring Boot 4.0.8 |
| 持久层 | MyBatis 4.0.1 + PageHelper 分页 |
| 数据库 | MySQL 8 + Druid 连接池 |
| 文件存储 | 阿里云 OSS |
| 日志 | Logback |
| 构建 | Maven（JDK 17） |

## 目录结构

```
web-ai-project02/
├── tlias-web-management/            # 主模块
│   ├── src/main/java/com/winter/
│   │   ├── TliasWebManagementApplication.java   # 启动类
│   │   ├── controller/              # 控制层：DeptController、EmpController、UpLoadController
│   │   ├── service/                 # 业务层接口与实现
│   │   ├── mapper/                  # 数据访问层接口
│   │   ├── pojo/                    # 实体类：Dept、Emp、EmpExpr、Result、PageResult 等
│   │   └── utils/                   # OssUtil 等工具类
│   └── src/main/resources/
│       ├── application.yaml.example # 配置模板（真实配置不提交）
│       ├── logback.xml
│       └── com/winter/mapper/       # MyBatis XML 映射文件
└── .gitignore
```

## 快速开始

1. 在 MySQL 里建好 `tlias` 数据库。
2. 复制配置模板并填入自己的信息：

   ```bash
   cd tlias-web-management/src/main/resources
   cp application.yaml.example application.yaml
   ```

   然后编辑 `application.yaml`，修改 `password`、`bucket-name` 等字段。

3. 设置 OSS 密钥环境变量（代码中通过环境变量读取，不写进配置文件）：

   ```powershell
   $env:OSS_ACCESS_KEY_ID="你的AccessKeyId"
   $env:OSS_ACCESS_KEY_SECRET="你的AccessKeySecret"
   ```

4. 启动 `TliasWebManagementApplication`，默认端口 8080。

## 已实现功能

- [x] 部门管理：列表查询、新增、修改、删除
- [x] 员工管理：分页条件查询、新增（含工作经历）、修改、删除
- [x] 文件上传：本地保存 + 阿里云 OSS 上传
- [ ] 登录认证与 JWT 令牌校验（待完成）
- [ ] 全局异常处理（待完成）

## 说明

本仓库不包含数据库密码等敏感信息。`application.yaml`、`target/`、`logs/`、`.idea/` 均已在 `.gitignore` 中排除。
