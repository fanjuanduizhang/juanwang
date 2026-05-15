# SurveyKing 部署到 Railway 完整指南

## 前置条件

1. **Railway 账号**：注册 [railway.app](https://railway.app)
2. **GitHub/GitLab 仓库**：将代码推送到远程仓库（Railway 从仓库部署）
3. **MySQL 数据库**：Railway 提供 MySQL 插件（免费额度 500h/月）

---

## 第一步：修改清单（已完成 ✅）

| 文件 | 修改内容 |
|------|----------|
| `server/api/Dockerfile` | 更新为 JDK 17 + 动态端口支持 |
| `server/api/src/main/resources/application-pro.yml` | 端口改为 `${PORT:1991}`，数据库改用环境变量 |
| `railway.json` (新增) | Railway 构建配置 |
| `.env.railway` (新增) | 环境变量模板 |

---

## 第二步：推送到 GitHub

```bash
# 初始化 Git 仓库（如果还没有）
git init
git add .
git commit -m "feat: 添加 Railway 部署配置"

# 推送到 GitHub
git remote add origin https://github.com/你的用户名/surveyking.git
git push -u origin main
```

> ⚠️ **注意**：确保 `.gitignore` 包含以下内容：
> ```
> # 不要上传的文件
> .env.railway
> *.db
> files/
> logs/
> build/
> .gradle/
> ```

---

## 第三步：Railway 部署步骤

### 方式 A：从 GitHub 部署（推荐）

1. 登录 [railway.app](https://railway.app)
2. 点击 **New Project**
3. 选择 **Deploy from GitHub repo**
4. 授权并选择你的 surveyking 仓库
5. Railway 会自动检测到 `railway.json` 和 `Dockerfile`
6. 点击 **Deploy**

### 方式 B：通过 CLI 部署

```bash
# 安装 Railway CLI
npm install -g @railway/cli

# 登录
railway login

# 初始化项目（在项目根目录执行）
railway init

# 部署
railway up
```

---

## 第四步：添加 MySQL 服务（重要！）

SurveyKing **必须使用 MySQL**，不能只靠 H2。

### 在 Railway 控制台添加：

1. 进入你的 Project
2. 点击 **+ New** → **Database** → **MySQL Add-on**
3. Railway 会自动创建 MySQL 并注入环境变量：
   - `MYSQLHOST`
   - `MYSQLPORT`
   - `MYSQLUSER`
   - `MYSQLPASSWORD`
   - `MYSQLDATABASE`

4. **手动添加 Spring 数据源变量**（在 Variables 中）：

   变量名 | 值 | 说明
   ---|---|---
   `SPRING_DATASOURCE_URL` | `jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true` | 数据库连接
   `SPRING_DATASOURCE_USERNAME` | `${MYSQLUSER}` | 数据库用户
   `SPRING_DATASOURCE_PASSWORD` | `${MYSQLPASSWORD}` | 数据库密码
   `SPRING_PROFILES_ACTIVE` | `pro` | 使用生产配置

---

## 第五步：配置文件持久化（Volume）

SurveyKing 上传的文件存储在 `./files` 目录。

### 在 Railway 控制台：

1. 选择你的服务
2. 点击 **Volumes** 标签
3. 点击 **Create Volume**：
   - Mount path: `/app/files`
   - Size: 1 GB（根据需要调整）

这样重启服务后文件不会丢失。

---

## 第六步：部署验证

1. 部署完成后，Railway 会生成一个 URL（如 `xxx.up.railway.app`）
2. 访问该 URL，应该看到 SurveyKing 首页
3. 默认账号：`admin` / `123456`

### 常见问题排查

| 问题 | 解决方案 |
|------|----------|
| 502 Bad Gateway | 检查 `$PORT` 变量是否正确注入 |
| 数据库连接失败 | 确认 MySQL 服务已创建且变量正确 |
| 文件上传后丢失 | 配置 Volume 持久化 |
| 启动超时 | 增加 JVM 内存或优化启动参数 |

---

## 成本估算（Railway 免费额度）

| 资源 | 免费额度 | SurveyKing 需求 |
|------|----------|----------------|
| 计算 | $5/月 (500h) | 1 个 Java 服务 ≈ $5-10/月 |
| MySQL | 256MB RAM | 免费额度内够用 |
| 存储 | 1GB | 文件上传够用 |
| 流量 | 100GB/月 | 一般够用 |

**最低成本**：$0-10/月（免费额度内可运行）

---

## 可选优化

### 1. 自定义域名
- Railway 支持绑定自定义域名
- 在 Settings → Networking → Custom Domains 添加

### 2. 自动扩缩容
- 在 Deployments → Scaling 设置最小/最大实例数
- 建议开发阶段设为 min=1, max=1

### 3. 日志查看
- Railway Dashboard → Logs 可以实时查看运行日志
- 或使用 CLI: `railway logs`

---

## 一键重新部署命令

当代码更新后：
```bash
git add .
git commit -m "update"
git push
# Railway 会自动触发重新部署
```

或在 Railway 手动点击 **Redeploy**。
