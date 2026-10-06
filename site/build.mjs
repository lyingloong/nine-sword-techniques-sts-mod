import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { execFileSync } from "node:child_process";

const here = path.dirname(fileURLToPath(import.meta.url));
const root = path.resolve(here, "..");
const output = path.join(root, "site-dist");
fs.rmSync(output, { recursive: true, force: true });
fs.mkdirSync(output, { recursive: true });
execFileSync(process.execPath, [path.join(here, "generate-data.mjs")], { stdio: "inherit" });
for (const file of ["index.html", "styles.css", "app.js", "data.js"]) fs.copyFileSync(path.join(here, file), path.join(output, file));
const imageSource = path.join(root, "src", "main", "resources", "NineSwordResources", "img");
const imageTarget = path.join(output, "assets");
fs.mkdirSync(imageTarget, { recursive: true });
for (const directory of ["cards", "muzixi/cards", "relics", "muzixi/relics", "events"]) {
  fs.cpSync(path.join(imageSource, directory), path.join(imageTarget, directory), { recursive: true });
}
for (const file of ["muzixi/character/muzixi_big.png", "muzixi/character/portrait.png", "muzixi/character/victory_bg.png", "muzixi/character/victory1.png"]) {
  const target = path.join(imageTarget, file);
  fs.mkdirSync(path.dirname(target), { recursive: true });
  fs.copyFileSync(path.join(imageSource, file), target);
}
const rename = [["muzixi", "muzixi-big.png", "muzixi/character/muzixi_big.png"], ["muzixi", "portrait.png", "muzixi/character/portrait.png"], ["", "hero-bg.png", "muzixi/character/victory_bg.png"], ["cards", "card-placeholder.png", "cards/HiddenSword.png"]];
for (const [, target, source] of rename) fs.copyFileSync(path.join(imageTarget, source), path.join(imageTarget, target));
