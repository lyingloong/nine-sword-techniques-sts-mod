import assert from "node:assert/strict";
import fs from "node:fs";
import test from "node:test";
import { readCardValues } from "./card-text.mjs";

const cases = [
  { name: "TrueSword", folder: "cards", cost: 3, upgraded: 2, label: "upgradeBaseCost sets an absolute cost" },
  { name: "EmotionalSword", folder: "cards", cost: 1, upgraded: 0, label: "upgrading to zero produces a free card" },
  { name: "HiddenSword", folder: "cards", cost: 3, upgraded: 3, label: "an upgrade without a cost change preserves the cost" },
  { name: "Flourish", folder: "muzixi/cards", cost: -1, upgraded: -1, label: "X cost remains X after upgrading" }
];

for (const item of cases) {
  test(item.label, () => {
    const source = fs.readFileSync(new URL(`../src/main/java/ninesword/${item.folder}/${item.name}.java`, import.meta.url), "utf8");
    const values = readCardValues(source, item.name);
    assert.equal(values.cost, item.cost);
    assert.equal(values.upgradeCost, item.upgraded);
  });
}
