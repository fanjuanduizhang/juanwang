# SurveyKing 部署到 Railway 完整指南（Dockerfile 方案）

## 前置条件

1. **Railway 账号**：注册 [railway.app](https://railway.app)
2. **GitHub/GitLab 仓库**：将代码推送到远程仓库（Railway 从仓库部署）
3. **MySQL 数据库**：Railway 提供 MySQL 插件（免费额度 500h/月）

---

## 第一步：文件清单

| 文件 | 用途 | 状态 |
|------|------|------|
| `Dockerfile` (根目录) | 多阶段构建：编译(JDK17) → 运行(JRE17) | ✅ 已创建 |
| `railway.json` | Railway 构建配置，指定 DOCKERFILE builder | ✅ 已创建 |
| `.gitignore` | Git 忽略规则 | ✅ 已创建 |
| `server/api/src/main/resources/application-pro.yml` | 生产配置(动态端口+环境变量) | ✅ 已修改 |
| `.env.railway` | 环境变量模板（仅供参考，不提交） | ✅ 已创建 |

---

## 第二步：推送到 GitHub

```bash
git init
git add .
git commit -m "feat: 添加 Railway Dockerfile 部署"
git remote add origin https://github.com/你的用户名/surveyking.git
git push -u origin main
```

---

## 第三步：Railway 部署步骤

### 从 GitHub 部署（推荐）

1. 登录 [railway.app](https://railway.app)
2. 点击 **New Project** → **Deploy from GitHub repo**
3. 授权 GitHub 并选择你的 surveyking 仓库
4. Railway 会自动检测到：
   - 根目录的 `Dockerfile`
   - `railway.json` 中指定了 `"builder": "DOCKERFILE"`
5. 点击 **Deploy** 开始构建

> **构建过程**：Docker 会在容器内执行 `gradlew bootJar -Ppro` 编译项目，
> 然后用 JRE 镜像运行。首次构建约需 3-8 分钟。

### 或通过 CLI 部署

```bash
npm install -g @railway/cli
railway login
railway init    # 在项目根目录执行
railway up      # 自动读取 Dockerfile 和 railway.json
```

---

## 第四步：添加 MySQL 服务（必须！⚠️）

SurveyKing **必须使用外部 MySQL**，不能用 H2。

### 在 Railway 控制台操作：

1. 进入你的 Project
2. 点击 **+ New** → **Database** → **MySQL Add-on**
3. Railway 自动注入环境变量：
   - `MYSQLHOST`
   - `MYSQLPORT`
   - `MYSQLUSER`
   - `MYSQLPASSWORD`
   - `MYSQLDATABASE`

4. 在服务的 **Variables** 中添加 Spring 数据源变量：

   变量名 | 值
   ---|---
   `SPRING_DATASOURCE_URL` | `jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true`
   `SPRING_DATASOURCE_USERNAME` | `${MYSQLUSER}`
   `SPRING_DATASOURCE_PASSWORD` | `${MYSQLPASSWORD}`
   `SPRING_PROFILES_ACTIVE` | `pro`

---

## 第五步：配置文件持久化 Volume

SurveyKing 上传文件存储在 `/app/files`。

1. 选择你的 SurveyKing 服务
2. 点击 **Volumes** 标签
3. **Create Volume**:
   - Mount path: `/app/files`
   - Size: 1 GB

---

## 第六步：验证部署

1. 部署完成后访问生成的 URL（如 `xxx.up.railway.app`）
2. 默认账号：`admin` / `123456`

### 常见问题排查

| 问题 | 解决方案 |
|------|----------|
| Nixpacks 构建失败 | ✅ 已改用 Dockerfile 构建，不会再出现 |
| 502 Bad Gateway | 检查 `$PORT` 是否正确注入（Railway 自动设置） |
| 数据库连接失败 | 确认 MySQL 服务已创建且变量正确填写 |
| 文件上传后丢失 | 配置 Volume 持久化 `/app/files` |
| 构建超时（>20min） | Railway 免费版有超时限制，可尝试预编译 JAR 后简化 Dockerfile |
| 内存不足(OOM) | 调整 `JAVA_OPTS` 为 `-Xmx384m -Xms256m` |

---

## 成本估算

| 资源 | 免费额度 | 实际需求 |
|------|----------|----------|
| 计算 | $5/月 (500h) | Java 服务 ≈ $7/月 |
| MySQL | 256MB RAM | 免费 ✅ |
| 存储 | 1GB | 免费 ✅ |
| 流量 | 100GB/月 | 一般够用 ✅ |

**预估成本**：约 **$7/月**（或利用免费额度 $0）

---

## 可选优化

- **自定义域名**：Settings → Networking → Custom Domains
- **自动扩缩容**：Deployments → Scaling（建议 min=1, max=1）
- **日志查看**：Dashboard → Logs 或 `railway logs`
