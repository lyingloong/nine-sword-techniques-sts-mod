# 九大剑术 · Nine Sword Techniques

[![Wiki 部署](https://github.com/lyingloong/nine-sword-techniques-sts-mod/actions/workflows/pages.yml/badge.svg)](https://github.com/lyingloong/nine-sword-techniques-sts-mod/actions/workflows/pages.yml)
[![代码许可：MIT](https://img.shields.io/badge/code_license-MIT-3e705c)](LICENSE)

> 九大剑术，十八剑流，三千剑道，皆自剑楼收录剑祖出。——《我有一身被动技》  
> The Nine Great Swordsmanship, the Eighteen Sword Styles, and the Three Thousand Swordsmanship Paths—all originated from the Sword Ancestor recorded in the Sword Tower. — I Have a Body of Passive Skills

以《我有一身被动技》为灵感的《杀戮尖塔》社区模组：为各角色加入九大剑术与顿悟体系，并提供拥有独立卡池的可选角色木子汐。

**[在线 Wiki](https://lyingloong.github.io/nine-sword-techniques-sts-mod/)** · **[Steam 创意工坊](https://steamcommunity.com/sharedfiles/filedetails/?id=3554467601)** · [开发指南](docs/development.md) · [参与贡献](CONTRIBUTING.md)

## 模组内容

| 内容 | 概览 |
| --- | --- |
| 九大剑术 | 9 张稀有无色基础剑术牌，分别对应 9 张顿悟形态 |
| 木子汐 | 67 张专属卡牌，围绕生机、麻痹及木子汐／泪汐儿双魂体系构筑 |
| 遗物 | 7 件共享主题遗物与 1 件木子汐专属初始遗物 |
| 事件 | 白窟、虚空岛、八尊谙 |
| 涅奥奖励 | 普通奖励池加入“选择一张剑术牌获得” |
| 语言 | 简体中文与英文游戏文本 |

**顿悟**可以将基础剑术替换为对应的进化牌，并继承升级状态。没有可进化剑术时，改为随机升级一张可升级的牌并获得 5 点最大生命。

**木子汐**使用独立的资源与人格机制：获得生机增加战斗内的临时最大生命，麻痹暂时削弱敌人，灵魂共鸣支持人格切换时的抽牌与能量爆发。

完整卡牌效果、升级变化、遗物和事件选项请查阅 [Wiki](https://lyingloong.github.io/nine-sword-techniques-sts-mod/)。维护者规格保存在[九大剑术设计](docs/design/nine-swords.md)与[木子汐设计](docs/design/muzixi.md)中；原 README 的《剑经》全文保留在[背景设定](docs/lore.md)。

## 安装与游玩

需要已安装的《杀戮尖塔》及两个前置模组：

- [ModTheSpire](https://steamcommunity.com/sharedfiles/filedetails/?id=1605060445)
- [BaseMod](https://steamcommunity.com/sharedfiles/filedetails/?id=1605833019)

1. 在 Steam 创意工坊订阅上述前置模组和[九大剑术](https://steamcommunity.com/sharedfiles/filedetails/?id=3554467601)。
2. 通过游戏的模组启动方式打开 ModTheSpire，并启用 BaseMod 和九大剑术。
3. 启动游戏；可使用原有角色体验共享剑术，或选择木子汐体验她的专属卡池。

手动构建和安装见[开发指南](docs/development.md)。遇到崩溃或效果异常，可通过[问题反馈](https://github.com/lyingloong/nine-sword-techniques-sts-mod/issues/new/choose)提交复现步骤与日志。

## 开发与维护

游戏模组使用 **Java 8 + Maven**。游戏本体、BaseMod 与 ModTheSpire 的 JAR 来自本机安装，不能仅靠 Maven 下载；先按[开发指南](docs/development.md)设置依赖路径，再编译或打包。

Wiki 使用 **Node.js 20** 构建，无需安装 npm 依赖。在仓库根目录执行：

```sh
node --test site/card-text.test.mjs
node site/build.mjs
```

静态站点输出到 `site-dist/`。本地预览、数据来源和 GitHub Pages 发布方式见 [Wiki 维护指南](site/README.md)。

> 当前 `mvn package` 会自动将 JAR 复制到游戏的 `mods` 目录。打包前请退出游戏；仅检查编译时使用 `mvn compile`。

## 仓库结构

```text
src/main/java/                    游戏逻辑
src/main/resources/              本地化、图片与模组元数据
docs/                            开发指南、设计规格与背景资料
site/                            Wiki 页面、数据生成器及测试
tools/                           美术与动画辅助工具
.github/                         Pages 工作流、Issue 与 PR 模板
pom.xml                          Maven 构建与本机依赖配置
```

`target/`、`site-dist/`、临时素材及 IDE 本机配置不应提交到版本控制。所有文档入口见[文档导航](docs/README.md)。

## 参与贡献

欢迎反馈问题、讨论玩法、完善文本或提交代码。请阅读 [CONTRIBUTING.md](CONTRIBUTING.md)，其中列出了不同改动需要同步的文档、文本与验证方式。

## 许可与来源

本项目作者提供的程序代码按 [MIT License](LICENSE) 发布。游戏、小说、角色设定、引文及图片等素材不由代码许可证统一授权，具体范围见[来源与授权说明](docs/credits.md)。

本项目由 [lyingloong](https://github.com/lyingloong) 维护，是社区模组，与原游戏及原作品的官方发布无关。
