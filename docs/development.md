# 开发指南

本项目使用 Java 8 和 Maven 开发模组，使用 Node.js 构建独立的静态 Wiki。以下命令从仓库根目录执行。

## 准备环境

| 工具或依赖 | 用途 |
| --- | --- |
| JDK 8 | 编译模组，并通过 ModTheSpire 启动游戏 |
| Maven 3.x | 解析本机依赖、编译和打包；仓库目前没有 Maven Wrapper |
| 本机安装的《杀戮尖塔》 | 提供游戏运行环境和 `desktop-1.0.jar` |
| ModTheSpire、BaseMod | 提供模组加载器和开发 API，可从 Steam 创意工坊订阅 |
| Node.js 20 | 仅修改或构建 Wiki 时需要，与 Pages 工作流保持一致 |
| Python 3 | 可用于本地预览 Wiki；重新生成角色骨骼资源时还需要 Pillow |

先检查命令行使用的 Java 和 Maven：

```powershell
java -version
mvn -version
```

两者应使用同一套 JDK 8。IDE 使用的 Java 也需要单独核对。

## 克隆与依赖路径

```powershell
git clone https://github.com/lyingloong/nine-sword-techniques-sts-mod.git
cd nine-sword-techniques-sts-mod
```

游戏和模组依赖通过 `pom.xml` 的 `systemPath` 从本机读取，不会从 Maven Central 下载，也不包含在本仓库中。`Steam.path` 指向 Steam 库的 **`steamapps` 目录**，其下需要能找到：

| 相对路径 | 依赖 |
| --- | --- |
| `common/SlayTheSpire/desktop-1.0.jar` | 游戏 |
| `workshop/content/646570/1605833019/BaseMod.jar` | BaseMod |
| `workshop/content/646570/1605060445/ModTheSpire.jar` | ModTheSpire |

下面的 `C:/SteamLibrary/steamapps` 是示例，请替换成自己的安装位置。命令行可以覆盖路径，无需修改已提交的 POM：

```powershell
mvn "-DSteam.path=C:/SteamLibrary/steamapps" clean compile
```

`compile` 用于检查 Java 编译和复制构建资源，不会把 JAR 部署到游戏目录。首次执行仍需要 Maven 下载构建插件。

如果游戏和创意工坊位于不同的 Steam 库，需要在本地调整 POM 中对应依赖的 `systemPath`。检查三个 JAR 是否存在，比单看 Maven 依赖的版本字段更可靠；实际使用的是这些路径下的本地 JAR。

## 打包与游戏内验证

**打包前先完全退出游戏。** 当前 POM 在 `package` 阶段会把生成的 JAR 自动复制到游戏的 `mods` 目录，覆盖同名文件。运行中的游戏可能仍持有旧 JAR 的读取状态，覆盖后再加载资源会导致崩溃。

```powershell
mvn "-DSteam.path=C:/SteamLibrary/steamapps" clean package
```

构建产物和自动部署目标分别为：

```text
target/nine-sword-techniques.jar
<Steam.path>/common/SlayTheSpire/mods/nine-sword-techniques.jar
```

通过 ModTheSpire 启动游戏，启用 BaseMod 和 NineSwordTechniques。避免同时加载创意工坊版与本地开发版的同一模组。

涉及卡牌、Power 或事件结算时，至少验证基础与升级状态、相关边界条件，以及一次完整战斗或事件流程。文本或图片调整也应在游戏中检查实际换行、尺寸与透明区域。提交问题所需的信息见[贡献指南](../CONTRIBUTING.md)。

## IDE 配置

### IntelliJ IDEA

打开根目录的 `pom.xml`，按 Maven 项目导入，项目 SDK 和 Maven 使用的 JDK 都设为 8。按本机安装位置配置 `Steam.path`；如果 Maven 导入器没有应用命令行覆盖值，可以本地调整 POM 后重新加载，提交前去掉机器专属路径的改动。

IDEA 可以从 Maven 项目重新生成 `.idea`、模块和依赖索引。个人窗口布局、JDK 路径和运行配置不需要提交到仓库。

### Visual Studio Code

安装 Java 扩展和 Maven for Java，打开仓库目录并导入 Maven 项目。配置 Java 运行时为 JDK 8，再检查 `Steam.path` 是否适合本机。`.vscode` 是本地配置，克隆仓库后需要自行建立。

Java 扩展的语言服务器可能需要更高版本的 JDK；它与项目的编译、游戏运行时可以分别配置。本项目与 ModTheSpire 仍使用 JDK 8。

如需 F5 启动，可创建 Java 启动配置，使用以下字段：

| 字段 | 设置 |
| --- | --- |
| `mainClass` | `com.evacipated.cardcrawl.modthespire.Loader` |
| `cwd` | 本机的 `common/SlayTheSpire` 游戏目录 |
| `javaExec` | 本机 JDK 8 的 Java 可执行文件 |
| `classPaths` | 本机 `ModTheSpire.jar` 的完整路径 |
| `args` | `["--mods", "basemod,NineSwordTechniques"]` |
| `preLaunchTask` | 与本地 `tasks.json` 中的打包任务 `label` 一致，例如 `maven-package` |

对应任务调用 `mvn`，传入自己的 `-DSteam.path=...` 和 `package` 参数，并使用 JDK 8。使用 F5 前同样需要退出上次启动的游戏。ModTheSpire 的其他启动参数见[官方参数说明](https://github.com/kiooeht/ModTheSpire/wiki/Command-Line-Arguments)。

## 修改内容时同步哪些文件

| 修改内容 | 需要同步检查 |
| --- | --- |
| 剑术、顿悟、共享遗物与事件 | Java 实现、中英本地化、[剑术设计](design/nine-swords.md)、Wiki 手写规则 |
| 木子汐卡池与专属机制 | Java 实现、中英本地化、[角色设计](design/muzixi.md)、Wiki 手写规则与角色概览 |
| 图片资源 | Java 资源路径、大小图、透明通道、Wiki 中的资源映射 |
| Wiki 页面或资料生成 | [站点开发说明](../site/README.md)、数据生成测试与浏览器预览 |

本地化位于 `src/main/resources/NineSwordResources/localization/ZHS/` 和 `ENG/`。使用游戏与 BaseMod 的关键词、能量图标和动态变量格式，避免把仅适用于某种文本界面的控制符混用。

## 角色骨骼资源工具

`tools/generate_muzixi_spine.py` 根据角色图片生成游戏内 Spine 3.4 资源。仅在修改木子汐战斗立绘或动画时运行：

```powershell
python -m pip install Pillow
python tools/generate_muzixi_spine.py
```

输入为 `src/main/resources/NineSwordResources/img/muzixi/character/muzixi.png`，输出位于同目录的 `spine/`。脚本会重写骨骼、图集和纹理；保留源图的透明背景，生成后检查资源差异，并在游戏内验证待机、攻击和受击动画。
