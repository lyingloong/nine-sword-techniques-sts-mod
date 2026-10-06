import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { cleanGameText, readCardValues, renderCardText } from "./card-text.mjs";
import { eventDetails, mechanics } from "./wiki-content.mjs";

const here = path.dirname(fileURLToPath(import.meta.url));
const root = path.resolve(here, "..");
const sourceRoot = path.join(root, "src", "main");
const zhPath = path.join(sourceRoot, "resources", "NineSwordResources", "localization", "ZHS", "cards.json");
const zh = JSON.parse(fs.readFileSync(zhPath, "utf8"));
const relicZh = JSON.parse(fs.readFileSync(path.join(sourceRoot, "resources", "NineSwordResources", "localization", "ZHS", "relics.json"), "utf8"));
const eventZh = JSON.parse(fs.readFileSync(path.join(sourceRoot, "resources", "NineSwordResources", "localization", "ZHS", "events.json"), "utf8"));
const swordBase = new Set(["HiddenSword", "IllusorySword", "NonSword", "MyriadSword", "EmotionalSword", "TrueSword", "GhostlySword", "MindSword", "NinefoldSword"]);
const swordEvolved = new Map([["UnsheathedBlade", "出鞘剑"], ["SpacetimeLeap", "时空跃迁"], ["VoidBladeStyle", "无有剑流"], ["AbsoluteImperialRule", "绝对帝制"], ["MortalBladeAllLivingFaces", "红尘剑·众生相"], ["AzureRiverSwordDomain", "青河剑界"], ["SoulControlTrickery", "御魂诡术"], ["UnderOnesGaze", "目下神佛"], ["InfiniteNumeration", "无限穷数"]]);
const evolutionPairs = { HiddenSword: "UnsheathedBlade", IllusorySword: "SpacetimeLeap", NonSword: "VoidBladeStyle", MyriadSword: "AbsoluteImperialRule", EmotionalSword: "MortalBladeAllLivingFaces", TrueSword: "AzureRiverSwordDomain", GhostlySword: "SoulControlTrickery", MindSword: "UnderOnesGaze", NinefoldSword: "InfiniteNumeration" };
const fileNames = new Map();
function walk(dir) { for (const entry of fs.readdirSync(dir, { withFileTypes: true })) { const full = path.join(dir, entry.name); if (entry.isDirectory()) walk(full); else if (entry.name.endsWith(".java")) fileNames.set(entry.name.slice(0, -5), full); } }
walk(path.join(sourceRoot, "java"));
function sourceFor(name) { return fileNames.get(name) || ""; }
function parseClass(name) {
  const file = sourceFor(name);
  if (!file) throw new Error(`Missing card source: ${name}`);
  const text = fs.readFileSync(file, "utf8");
  const type = text.match(/CardType\.(\w+)/)?.[1];
  const rarity = text.match(/CardRarity\.(\w+)/)?.[1];
  const image = text.match(/IMG_PATH\s*=\s*"([^"]+)"/);
  return { ...readCardValues(text, name), type, rarity, image: image?.[1].replace("NineSwordResources/img/", "") };
}
function imageFor(name, group) { const parsed = parseClass(name); if (parsed.image) return parsed.image; const dir = group === "九大剑术" ? "cards" : "muzixi/cards"; const direct = path.join(sourceRoot, "resources", "NineSwordResources", "img", dir, `${name}.png`); return fs.existsSync(direct) ? `${dir}/${name}.png` : ""; }
const cards = [];
for (const [id, strings] of Object.entries(zh)) {
  if (!id.startsWith("NineSwordTechniques:")) continue;
  const name = id.split(":")[1];
  if (name === "SacrificeLegacyOption" || name === "AbandonLegacyOption") continue;
  const group = swordBase.has(name) || swordEvolved.has(name) ? "九大剑术" : "木子汐卡牌";
  const parsed = parseClass(name);
  cards.push({ id, name: strings.NAME, group, kind: swordBase.has(name) ? "基础剑术" : swordEvolved.has(name) ? "顿悟形态" : parsed.rarity, rarity: parsed.rarity, cost: parsed.cost, upgradeCost: parsed.upgradeCost, type: parsed.type,
    description: renderCardText(strings.DESCRIPTION, parsed.values, name),
    upgradeDescription: renderCardText(strings.UPGRADE_DESCRIPTION || strings.DESCRIPTION, parsed.upgraded, name),
    image: imageFor(name, group), evolution: evolutionPairs[name] ? swordEvolved.get(evolutionPairs[name]) : undefined });
}
const relicImageNames = { SwordAncestorsLegacy: "relics/SwordAncestorsLegacy_128.png", TheCanonOfSwordObservation: "relics/TheCanonOfSwordObservation_128.png", TheYousiSword: "relics/TheYousiSword_128.png", FlameSwordYanmang: "relics/FlameSwordYanmang_128.png", EmberSeed: "relics/EmberSeed_128.png", EmberWhiteFlame: "relics/EmberWhiteFlame_128.png", VoidCrystal: "relics/VoidCrystal_128.png", DivineDemonicEyes: "muzixi/relics/DivineDemonicEyes_128.png" };
const relics = Object.entries(relicZh).map(([id, strings]) => { const name = id.split(":")[1]; return { id, name: strings.NAME, group: name === "DivineDemonicEyes" ? "木子汐" : "九大剑术", description: cleanGameText(strings.DESCRIPTIONS?.[0]), image: relicImageNames[name] || "" }; });
const events = eventDetails.map((event) => {
  const strings = eventZh[`NineSwordTechniques:${event.id}`];
  return { ...event, name: strings.NAME, image: `events/${event.id}.png`, story: cleanGameText(strings.DESCRIPTIONS[0]),
    options: event.options.map((option) => ({ ...option, result: cleanGameText(option.result) })) };
});
const data = { cards, events, relics, mechanics };
const output = `window.NINE_SWORD_DATA = ${JSON.stringify(data, null, 2)};\n`;
fs.writeFileSync(path.join(here, "data.js"), output);
