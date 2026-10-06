# 九大剑术模组图鉴

这是仓库内的 GitHub Pages 静态站点。页面不依赖前端框架，内容数据由 `generate-data.mjs` 从模组本地化文件和 Java 卡牌定义生成。

本地预览：

```powershell
node site/build.mjs
python -m http.server 8000 -d site-dist
```

然后打开 `http://localhost:8000`。GitHub Actions 会在推送到 `main` 或 `master` 后自动构建并部署 `site-dist`。
