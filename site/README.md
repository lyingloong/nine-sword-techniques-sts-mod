# 模组 Wiki

[线上图鉴](https://lyingloong.github.io/nine-sword-techniques-sts-mod/)收录九大剑术、木子汐、遗物、事件与机制。站点使用原生 HTML、CSS 和 JavaScript，通过 hash 路由支持搜索、筛选和条目链接。

## 本地构建与预览

需要 Node.js 20。脚本只使用 Node.js 内置模块，无需安装 npm 依赖，也不需要 Java、Maven 或游戏 JAR。以下命令从仓库根目录执行：

```powershell
node --test site/card-text.test.mjs
node site/build.mjs
python -m http.server 8000 --bind 127.0.0.1 --directory site-dist
```

预览使用 Python 3。打开 <http://127.0.0.1:8000/>，按 `Ctrl+C` 停止服务。修改页面或数据后重新构建并刷新浏览器。

构建会重新创建 `site-dist/`，生成 `site/data.js`，并复制页面和游戏美术资源。`site-dist/` 是被 Git 忽略的发布产物；`site/data.js` 当前纳入版本控制，重新生成后应检查其差异。不要直接编辑这两个生成结果来修改规则。

## 文件与数据来源

| 文件 | 职责 |
| --- | --- |
| `index.html` | 站点外壳、侧栏、全站搜索和卡牌对话框 |
| `styles.css` | 页面样式与移动端布局 |
| `app.js` | 路由、搜索、筛选、详情和各资料页渲染 |
| `build.mjs` | 生成资料并把站点与图片复制到 `site-dist/` |
| `generate-data.mjs` | 读取模组源码与中文本地化，生成 `data.js` |
| `card-text.mjs` | 解析卡牌数值和升级费用，转换游戏文本格式 |
| `card-text.test.mjs` | 用真实卡牌源码检查费用解析，覆盖普通、升级到零费和 X 费 |
| `wiki-content.mjs` | 人工整理的机制规则、事件选项、条件、代价、收益和结算示例 |
| `data.js` | 页面使用的生成数据 |

生成器从以下目录读取内容：

- `src/main/java/`：卡牌类型、稀有度、费用、基础和升级数值，以及部分图片路径。
- `src/main/resources/NineSwordResources/localization/ZHS/`：卡牌与遗物文本、事件名称和开场故事。
- `src/main/resources/NineSwordResources/img/`：卡图、遗物图、事件图和角色插画。

部分内容仍需人工维护：`generate-data.mjs` 中的顿悟对应关系和遗物图片映射、`wiki-content.mjs` 中的机制和事件资料，以及 `app.js` 中的角色初始配置。修改游戏逻辑后，需要同步检查这些内容；重新构建不会自动推断任意 Java 结算逻辑。

## 验证改动

修改生成逻辑时运行 `node --test site/card-text.test.mjs`，再执行完整构建。修改页面时检查首页、搜索结果、卡牌基础与升级效果、顿悟双向跳转，以及事件和机制链接。

浏览器中同时检查桌面和窄屏布局。卡牌详情关闭后应保留筛选和阅读位置，直接打开条目链接或刷新页面应能找到对应内容。若游戏规则有变化，最终以游戏内验证和对应源码为准。

## GitHub Pages 发布

仓库使用 [Pages 工作流](../.github/workflows/pages.yml)。推送到 `master` 或 `main`，或手动触发工作流后，GitHub Actions 使用 Node.js 20 构建，上传 `site-dist/`，再部署到 GitHub Pages。

仓库的 **Settings → Pages → Source** 应选择 **GitHub Actions**。此流程不需要单独的 `gh-pages` 分支，也不需要把构建产物提交到分支。构建与发布状态可在仓库的 Actions 页面查看。

模组 Java 开发、IDE 配置和打包副作用见[开发指南](../docs/development.md)。
