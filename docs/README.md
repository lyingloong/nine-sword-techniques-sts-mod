# 文档导航

玩家查询卡牌、遗物、事件和机制，请前往[在线 Wiki](https://lyingloong.github.io/nine-sword-techniques-sts-mod/)。这里保存开发与维护文档。

| 文档 | 内容 |
| --- | --- |
| [项目首页](../README.md) | 项目介绍、安装与常用入口 |
| [开发指南](development.md) | Java 与 Maven 环境、依赖、构建、IDE 和调试 |
| [贡献指南](../CONTRIBUTING.md) | 问题反馈、功能建议、修改与验证要求 |
| [九大剑术设计](design/nine-swords.md) | 剑术、顿悟、共享遗物和事件的规格 |
| [木子汐设计](design/muzixi.md) | 角色、卡池、生机、麻痹与双魂机制 |
| [Wiki 维护指南](../site/README.md) | 数据生成、本地预览与 GitHub Pages 部署 |
| [背景设定](lore.md) | 原 README 中的《剑经》引文 |
| [来源与授权](credits.md) | 代码许可、作品背景、依赖与素材范围 |

## 文档与实现的关系

- `src/main/java/` 实现游戏行为，`src/main/resources/NineSwordResources/localization/` 提供游戏内文本。
- `docs/design/` 记录设计规格；其中明确标注的概念或未实装内容不代表当前功能。
- Wiki 的卡牌数值和描述由代码、本地化文件生成；事件选项和机制说明还包含手工维护的内容。

调整玩法时，同时检查实现、中英文文本、设计规格与 Wiki。发现不一致时，请在 Issue 或 PR 中指出差异，不要只根据旧文档推断游戏效果。
