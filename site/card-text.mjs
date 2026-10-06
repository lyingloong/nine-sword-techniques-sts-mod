// These readers cover the literal assignments used by this mod, not arbitrary Java.
function methodBody(source, signature) {
  const match = signature.exec(source);
  if (!match) throw new Error(`Missing Java method: ${signature}`);
  const start = source.indexOf("{", match.index);
  let depth = 1;
  let end = start + 1;
  for (; depth && end < source.length; end++) {
    if (source[end] === "{") depth++;
    if (source[end] === "}") depth--;
  }
  return source.slice(start + 1, end - 1);
}

export function readCardValues(source, name) {
  const constructor = methodBody(source, new RegExp(`public\\s+${name}\\s*\\(\\s*\\)`));
  const upgrade = methodBody(source, /public\s+void\s+upgrade\s*\(\s*\)/);
  const constants = Object.fromEntries([...source.matchAll(/static\s+final\s+int\s+(\w+)\s*=\s*(-?\d+)\s*;/g)].map((m) => [m[1], Number(m[2])]));
  const argument = constructor.match(/super\(ID,[\s\S]*?,\s*(-?\d+)\s*,/);
  if (!argument) throw new Error(`Unrecognized card cost: ${name}`);
  const cost = Number(argument[1]);
  const values = {};
  const upgraded = {};
  for (const [token, field, call] of [["D", "baseDamage", "upgradeDamage"], ["B", "baseBlock", "upgradeBlock"], ["M", "baseMagicNumber", "upgradeMagicNumber"]]) {
    const assignment = constructor.match(new RegExp(`\\b${field}\\s*=\\s*(?:\\w+\\s*=\\s*)*(-?\\d+|[A-Z][A-Z_]+)\\s*;`));
    if (!assignment) continue;
    const value = /^-?\d+$/.test(assignment[1]) ? Number(assignment[1]) : constants[assignment[1]];
    if (value === undefined) throw new Error(`Unrecognized ${field}: ${name}`);
    values[token] = value;
    const change = upgrade.match(new RegExp(`\\b${call}\\(\\s*(-?\\d+)\\s*\\)`));
    upgraded[token] = value + (change ? Number(change[1]) : 0);
  }
  const newCost = upgrade.match(/upgradeBaseCost\(\s*(-?\d+)\s*\)/);
  return { values, upgraded, cost, upgradeCost: newCost ? cost + Number(newCost[1]) : cost };
}

export function cleanGameText(text = "") {
  return text.replace(/\s*\bNL\b\s*/g, "\n")
    .replace(/（当前生机\s*!NSTV!\s*）/g, "")
    .replace(/nineswordtechniques:/g, "")
    .replace(/#[rgbypw]/g, "")
    .replace(/\*/g, "")
    .replace(/(?:\[E\]\s*)+/g, (energy) => `${(energy.match(/\[E\]/g) || []).length} 点能量`)
    .replace(/[ \t]+/g, " ")
    .replace(/ +([。，；：）])/g, "$1")
    .replace(/([\u3400-\u9fff]) +(?=[\u3400-\u9fff])/g, "$1")
    .replace(/\n{2,}/g, "\n").trim();
}

export function renderCardText(text, values, name) {
  return cleanGameText(text).replace(/!([A-Z]+)!/g, (_, token) => {
    if (values[token] === undefined) throw new Error(`Unresolved !${token}! in ${name}`);
    return String(values[token]);
  });
}
