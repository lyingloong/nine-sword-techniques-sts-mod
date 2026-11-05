(function () {
  const app = document.getElementById("app");
  const breadcrumbs = document.getElementById("breadcrumbs");
  const modal = document.getElementById("modal");
  const modalBody = document.getElementById("modal-body");
  const shell = document.querySelector(".site-shell");
  const data = window.NINE_SWORD_DATA || {};
  const cards = data.cards || [];
  const relics = data.relics || [];
  const eventList = data.events || [];
  const mechanicList = data.mechanics || [];
  const routes = { home: "首页", swords: "九大剑术", muzixi: "木子汐", relics: "遗物", events: "事件", mechanics: "机制", search: "搜索" };
  const assets = "assets/";
  let backgroundHash = "";
  let returnContext = null;
  let openedFromPage = false;

  function escapeHtml(value) {
    return String(value ?? "").replace(/[&<>\"]/g, (char) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;" }[char]));
  }
  function decode(value) { try { return decodeURIComponent(value); } catch { return value; } }
  function imageFor(path) { return path ? assets + path : assets + "card-placeholder.png"; }
  function rarityLabel(value) { return ({ BASIC: "基础", COMMON: "普通", UNCOMMON: "罕见", RARE: "稀有", SPECIAL: "特殊" }[value] || value || ""); }
  function typeLabel(value) { return ({ ATTACK: "攻击", SKILL: "技能", POWER: "能力", STATUS: "状态", CURSE: "诅咒" }[value] || value || "卡牌"); }
  function costLabel(value) { return value === -1 ? "X" : value === -2 ? "不可打出" : value === undefined ? "—" : String(value); }
  function cardHref(card) { return "#cards/" + encodeURIComponent(card.id); }
  function entryHref(route, id) { return "#" + route + "/" + encodeURIComponent(id); }
  function hashFor(route, values = {}) {
    const params = new URLSearchParams();
    for (const [key, value] of Object.entries(values)) if (value && value !== "all" && !(key === "view" && value === "grid")) params.set(key, value);
    return "#" + route + (params.size ? "?" + params.toString() : "");
  }
  function routeInfo(hash = location.hash) {
    const [path, query = ""] = hash.slice(1).split("?");
    const [route = "home", ...parts] = path.split("/");
    return { route: route || "home", id: decode(parts.join("/")), params: new URLSearchParams(query) };
  }
  const keywordRoutes = {
    "灵魂共鸣": "persona", "木子汐人格": "persona", "泪汐儿人格": "persona", "人格交替": "persona", "一体双魂": "persona",
    "绝对麻痹": "paralysis", "临时最大生命": "vitality", "出鞘剑": "true-damage", "鬼剑系": "return",
    "顿悟": "enlightenment", "生机": "vitality", "凋萎": "vitality", "麻痹": "paralysis", "返回": "return", "真伤": "true-damage"
  };
  const keywordPattern = new RegExp(Object.keys(keywordRoutes).sort((a, b) => b.length - a.length).join("|"), "g");
  function richText(value) {
    return escapeHtml(value).replace(keywordPattern, (word) => `<a class="keyword" href="${entryHref("mechanics", keywordRoutes[word])}">${word}</a>`);
  }
  function intro(title, description, eyebrow = "模组图鉴") {
    return `<div class="page-intro"><div class="eyebrow">${escapeHtml(eyebrow)}</div><h1>${escapeHtml(title)}</h1><p>${description}</p></div>`;
  }
  function layout(title, body, activeRoute) {
    breadcrumbs.textContent = `模组图鉴 / ${title}`;
    document.title = `${title === "首页" ? "九大剑术" : title} · 模组图鉴`;
    document.querySelectorAll(".nav a").forEach((link) => {
      const active = link.dataset.route === (activeRoute || routeInfo().route);
      link.classList.toggle("active", active);
      if (active) link.setAttribute("aria-current", "page"); else link.removeAttribute("aria-current");
    });
    app.innerHTML = body;
  }
  function updateCounts() {
    const counts = { swords: cards.filter((card) => card.group === "九大剑术").length, muzixi: cards.filter((card) => card.group === "木子汐卡牌").length, relics: relics.length, events: eventList.length, mechanics: mechanicList.length };
    document.querySelectorAll("[data-count]").forEach((element) => { element.textContent = counts[element.dataset.count] ?? ""; });
  }

  function home() {
    const swordCount = cards.filter((card) => card.group === "九大剑术").length;
    const muzixiCount = cards.filter((card) => card.group === "木子汐卡牌").length;
    const directories = [
      ["swords", "九大剑术", "基础剑术与顿悟形态，九种剑术的两重境界。", `${swordCount} 张卡牌`],
      ["muzixi", "木子汐", "生机、麻痹与一体双魂。查阅角色和完整卡池。", `${muzixiCount} 张卡牌`],
      ["relics", "遗物", "剑祖的传承、观剑典，以及角色专属遗物。", `${relics.length} 件遗物`],
      ["events", "事件", "白窟、虚空岛与八尊谙。比较选项和收益。", `${eventList.length} 个事件`]
    ];
    layout("首页", `<section class="home-intro"><div class="home-copy"><div class="eyebrow">SLAY THE SPIRE · 模组图鉴</div><h1>九大剑术</h1><p>以《我有一身被动技》为灵感的《杀戮尖塔》模组。这里收录九大剑术、木子汐，以及相关卡牌、遗物、事件与机制。</p><div class="home-counts"><span>${cards.length} 张卡牌</span><span>${relics.length} 件遗物</span><span>${eventList.length} 个事件</span></div></div><figure class="home-art"><img src="${assets}muzixi/character/victory1.png" alt="木子汐与森林中的魔树" fetchpriority="high"><figcaption>木子汐 · 角色插画</figcaption></figure></section>
      <form class="home-search" data-site-search role="search"><label class="sr-only" for="home-query">搜索模组图鉴</label><input class="search" id="home-query" name="q" type="search" placeholder="搜索卡牌、遗物、事件或机制" autocomplete="off"><button class="button" type="submit">搜索图鉴 <span aria-hidden="true">↗</span></button></form>
      <div class="section-heading"><h2>图鉴目录</h2><p>按内容查阅</p></div><div class="directory-grid">${directories.map(([route, title, description, count], index) => `<a class="directory-link" href="#${route}"><span class="directory-number">0${index + 1}</span><div><h3>${title}<span aria-hidden="true"> ↗</span></h3><p>${description}</p></div><span class="directory-count">${count}</span></a>`).join("")}</div>
      <section class="home-mechanics"><div class="section-heading"><h2>机制速查</h2><a class="text-link" href="#mechanics">全部规则 →</a></div><div class="keyword-links">${mechanicList.map((item) => `<a href="${entryHref("mechanics", item.id)}">${escapeHtml(item.name)} <span aria-hidden="true">↗</span></a>`).join("")}</div></section>
      <div class="home-note"><div><h3>开始游玩</h3><p>需要《杀戮尖塔》、ModTheSpire 与 BaseMod。订阅模组后，通过 ModTheSpire 启动游戏。</p></div><a class="text-link" href="https://steamcommunity.com/sharedfiles/filedetails/?id=3554467601" target="_blank" rel="noreferrer">Steam 创意工坊 ↗</a></div>`, "home");
  }

  function cardTile(card) {
    return `<a class="card-tile" href="${cardHref(card)}" data-card="${escapeHtml(card.id)}"><img src="${imageFor(card.image)}" alt="" loading="lazy"><span class="card-meta"><span class="card-title"><strong>${escapeHtml(card.name)}</strong><small>${escapeHtml(costLabel(card.cost))} 费</small></span><span class="card-category">${escapeHtml(typeLabel(card.type))} · ${escapeHtml(rarityLabel(card.rarity))}${card.group === "九大剑术" && card.kind ? ` · ${escapeHtml(card.kind)}` : ""}</span><span class="card-summary">${escapeHtml(card.description.replace(/^剑术牌。\s*/, ""))}</span></span></a>`;
  }
  function selectOptions(choices, selected) { return choices.map(([value, label]) => `<option value="${escapeHtml(value)}"${value === selected ? " selected" : ""}>${escapeHtml(label)}</option>`).join(""); }
  function cardsPage(route, params) {
    const isMuzixi = route === "muzixi";
    const source = cards.filter((card) => card.group === (isMuzixi ? "木子汐卡牌" : "九大剑术"));
    const state = { q: params.get("q") || "", filter: params.get("filter") || "all", type: params.get("type") || "all", cost: params.get("cost") || "all", view: params.get("view") === "list" ? "list" : "grid" };
    const filters = isMuzixi ? [["all", "全部稀有度"], ["BASIC", "基础"], ["COMMON", "普通"], ["UNCOMMON", "罕见"], ["RARE", "稀有"]] : [["all", "全部形态"], ["基础剑术", "基础剑术"], ["顿悟形态", "顿悟形态"]];
    const overview = isMuzixi ? `<section class="character-overview"><div><div class="character-facts"><span>70 初始生命</span><span>3 能量</span><span>99 金币</span></div><p>初始牌组：4 张打击、4 张防御、生机奔涌、生命汲取。</p><p>初始遗物：<a class="keyword" href="${entryHref("relics", relics.find((item) => item.group === "木子汐")?.id || "")}">神魔瞳</a>。战斗开始时进入木子汐人格并获得 2 点生机。</p><div class="keyword-links"><a href="#mechanics/vitality">生机与凋萎 ↗</a><a href="#mechanics/paralysis">麻痹 ↗</a><a href="#mechanics/persona">人格与灵魂共鸣 ↗</a></div></div><img class="character-art" src="${assets}muzixi/character/muzixi_big.png" alt="木子汐角色立绘"></section><div class="section-heading"><h2>角色卡牌</h2><a class="text-link" href="#relics?group=muzixi">专属遗物 →</a></div>` : `<div class="page-index"><a href="#mechanics/enlightenment">顿悟规则 ↗</a><a href="#relics?group=swords">相关遗物 ↗</a><span>打开卡牌可对照升级效果与顿悟形态</span></div>`;
    layout(routes[route], intro(routes[route], isMuzixi ? "独立的角色与卡牌体系。积累生机，操纵麻痹，在木子汐与泪汐儿人格之间切换。" : "九张基础剑术，通过顿悟替换为九张对应的特殊形态。查阅牌面效果、升级变化与进化关系。", isMuzixi ? "角色 / MUZIXI" : "卡牌 / SWORD TECHNIQUES") + overview + `<div class="toolbar"><label class="sr-only" for="card-search">搜索卡牌</label><input class="search" id="card-search" type="search" placeholder="搜索名称、效果或关键词" value="${escapeHtml(state.q)}"><label class="sr-only" for="card-filter">${isMuzixi ? "稀有度" : "形态"}</label><select class="select" id="card-filter">${selectOptions(filters, state.filter)}</select><label class="sr-only" for="card-type">卡牌类型</label><select class="select" id="card-type">${selectOptions([["all", "全部类型"], ["ATTACK", "攻击"], ["SKILL", "技能"], ["POWER", "能力"]], state.type)}</select><label class="sr-only" for="card-cost">能量费用</label><select class="select" id="card-cost">${selectOptions([["all", "全部费用"], ["0", "0 费"], ["1", "1 费"], ["2", "2 费"], ["3+", "3 费及以上"], ["X", "X 费"]], state.cost)}</select></div><div class="list-heading"><p class="list-status" id="list-status" role="status"></p><div class="view-switch" role="group" aria-label="卡牌显示方式"><button type="button" data-view="grid" aria-pressed="${state.view === "grid"}">网格</button><button type="button" data-view="list" aria-pressed="${state.view === "list"}">列表</button></div></div><div class="card-grid" id="card-grid"></div>`, route);
    const search = document.getElementById("card-search");
    const filter = document.getElementById("card-filter");
    const type = document.getElementById("card-type");
    const cost = document.getElementById("card-cost");
    const grid = document.getElementById("card-grid");
    function update(writeUrl = true) {
      Object.assign(state, { q: search.value, filter: filter.value, type: type.value, cost: cost.value });
      const query = state.q.trim().toLocaleLowerCase();
      const visible = source.filter((card) => (!query || `${card.name} ${card.description} ${card.upgradeDescription}`.toLocaleLowerCase().includes(query)) && (state.filter === "all" || (isMuzixi ? card.rarity : card.kind) === state.filter) && (state.type === "all" || card.type === state.type) && (state.cost === "all" || (state.cost === "X" ? card.cost === -1 : state.cost === "3+" ? card.cost >= 3 : card.cost === Number(state.cost))));
      grid.classList.toggle("is-list", state.view === "list");
      grid.innerHTML = visible.length ? visible.map(cardTile).join("") : `<div class="empty">没有找到符合条件的卡牌。试试其他关键词或筛选条件。</div>`;
      document.getElementById("list-status").textContent = visible.length === source.length ? `共 ${source.length} 张卡牌` : `找到 ${visible.length} / ${source.length} 张卡牌`;
      document.querySelectorAll("[data-view]").forEach((button) => button.setAttribute("aria-pressed", String(button.dataset.view === state.view)));
      if (writeUrl) { const hash = hashFor(route, state); history.replaceState(null, "", hash); backgroundHash = hash; }
    }
    search.addEventListener("input", () => update());
    [filter, type, cost].forEach((control) => control.addEventListener("change", () => update()));
    document.querySelectorAll("[data-view]").forEach((button) => button.addEventListener("click", () => { state.view = button.dataset.view; update(); }));
    update(false);
  }

  function relicTile(relic) {
    return `<article class="relic" id="relics-${escapeHtml(relic.id)}"><img src="${imageFor(relic.image)}" alt="" loading="lazy"><div><div class="relic-heading"><h2><a href="${entryHref("relics", relic.id)}">${escapeHtml(relic.name)}</a></h2><span>${escapeHtml(relic.group === "木子汐" ? "木子汐专属" : "共享遗物")}</span></div><p>${richText(relic.description)}</p></div></article>`;
  }
  function relicsPage(params) {
    const group = params.get("group") || "all";
    const visible = relics.filter((item) => group === "all" || item.group === (group === "muzixi" ? "木子汐" : "九大剑术"));
    layout("遗物", intro("遗物", "查阅共享遗物与木子汐专属遗物的获得方式、触发时机和完整效果。", "道具 / RELICS") + `<nav class="page-index" aria-label="遗物分类"><a href="#relics"${group === "all" ? ' aria-current="page"' : ""}>全部 ${relics.length}</a><a href="#relics?group=swords"${group === "swords" ? ' aria-current="page"' : ""}>共享遗物</a><a href="#relics?group=muzixi"${group === "muzixi" ? ' aria-current="page"' : ""}>木子汐专属</a></nav><div class="relic-list">${visible.map(relicTile).join("")}</div>`);
  }
  function events() {
    layout("事件", intro("事件", "查看事件的出现阶段与选项。代价和收益直接列出，故事与结算细节可展开阅读。", "探索 / EVENTS") + `<nav class="page-index" aria-label="事件目录">${eventList.map((item) => `<a href="${entryHref("events", item.id)}">${escapeHtml(item.name)} ↓</a>`).join("")}</nav><div class="event-list">${eventList.map((item) => `<article class="event-card" id="events-${escapeHtml(item.id)}"><div class="event-visual"><img src="${imageFor(item.image)}" alt="${escapeHtml(item.name)}事件插画" loading="lazy"><div><span class="event-act">${escapeHtml(item.act)}</span><h2><a href="${entryHref("events", item.id)}">${escapeHtml(item.name)}</a></h2><p>${escapeHtml(item.summary)}</p></div></div><div class="table-scroll"><table class="event-table"><thead><tr><th scope="col">选项与条件</th><th scope="col">代价</th><th scope="col">收益</th></tr></thead><tbody>${item.options.map((option) => `<tr><th scope="row">${escapeHtml(option.name)}<small>${escapeHtml(option.condition)}</small></th><td>${richText(option.cost)}</td><td>${richText(option.reward)}</td></tr>`).join("")}</tbody></table></div><details class="event-extra"><summary>故事与选择后的文本</summary><p class="event-story">${escapeHtml(item.story)}</p>${item.options.map((option) => `<div class="event-result"><strong>${escapeHtml(option.name)}</strong><p>${escapeHtml(option.result)}</p></div>`).join("")}</details><details class="event-extra"><summary>结算细节</summary><ul>${item.notes.map((note) => `<li>${richText(note)}</li>`).join("")}</ul></details></article>`).join("")}</div>`);
  }
  function mechanicLinks(item) {
    const queries = { vitality: "生机", paralysis: "麻痹", persona: "人格", return: "鬼剑", "true-damage": "真伤" };
    const route = ["vitality", "paralysis", "persona"].includes(item.id) ? "muzixi" : "swords";
    return `<a class="text-link" href="${hashFor(route, { q: queries[item.id] })}">${item.id === "enlightenment" ? "浏览剑术牌" : "查看相关卡牌"} →</a>${item.id === "enlightenment" ? '<a class="text-link" href="#events">相关事件 →</a>' : ""}${item.id === "true-damage" ? `<a class="text-link" href="${hashFor("swords", { q: "出鞘剑" })}">出鞘剑相关卡牌 →</a>` : ""}`;
  }
  function mechanics() {
    layout("机制", intro("机制", "从关键词读懂牌面效果。每条机制包含规则、结算边界与一个具体示例。", "规则 / MECHANICS") + `<nav class="page-index" aria-label="机制目录">${mechanicList.map((item) => `<a href="${entryHref("mechanics", item.id)}">${escapeHtml(item.name)}</a>`).join("")}</nav><div class="mechanic-list">${mechanicList.map((item) => `<article class="mechanic-detail" id="mechanics-${escapeHtml(item.id)}"><div class="mechanic-head"><span class="mechanic-key">${escapeHtml(item.key)}</span><h2><a href="${entryHref("mechanics", item.id)}">${escapeHtml(item.name)}</a></h2><p>${escapeHtml(item.description)}</p></div><div class="mechanic-columns"><div><h3>规则</h3><ul>${item.rules.map((rule) => `<li>${escapeHtml(rule)}</li>`).join("")}</ul></div><aside class="mechanic-example"><h3>例子</h3><p>${escapeHtml(item.example)}</p><div class="detail-links">${mechanicLinks(item)}</div></aside></div></article>`).join("")}</div>`);
  }

  function searchPage(params) {
    const query = (params.get("q") || "").trim();
    const lower = query.toLocaleLowerCase();
    const match = (item) => lower && JSON.stringify(item).toLocaleLowerCase().includes(lower);
    const groups = [
      { title: "卡牌", route: "cards", items: cards.filter((card) => lower && `${card.name} ${card.description} ${card.upgradeDescription} ${card.group}`.toLocaleLowerCase().includes(lower)) },
      { title: "遗物", route: "relics", items: relics.filter(match) },
      { title: "事件", route: "events", items: eventList.filter(match) },
      { title: "机制", route: "mechanics", items: mechanicList.filter(match) }
    ];
    const total = groups.reduce((sum, group) => sum + group.items.length, 0);
    layout("搜索", intro("搜索图鉴", "搜索卡牌名称和效果、遗物、事件或机制。", "查阅 / SEARCH") + `<form class="home-search" data-site-search role="search"><label class="sr-only" for="search-query">搜索图鉴</label><input class="search" id="search-query" name="q" type="search" placeholder="输入名称或关键词" value="${escapeHtml(query)}"><button class="button" type="submit">搜索</button></form><p class="list-status" role="status">${query ? `“${escapeHtml(query)}” · ${total} 条结果` : "试试：生机、顿悟、观剑典"}</p><div class="search-results">${groups.filter((group) => group.items.length).map((group) => `<section><div class="section-heading"><h2>${group.title}</h2><p>${group.items.length} 条结果</p></div>${group.items.map((item) => `<a class="search-result" href="${group.route === "cards" ? cardHref(item) : entryHref(group.route, item.id)}"${group.route === "cards" ? ` data-card="${escapeHtml(item.id)}"` : ""}><span class="result-type">${escapeHtml(item.group || item.act || group.title)}</span><h3>${escapeHtml(item.name)} <span aria-hidden="true">↗</span></h3><p>${escapeHtml(item.description || item.summary || "")}</p></a>`).join("")}</section>`).join("")}${query && !total ? '<div class="empty">没有找到相关条目。可以缩短关键词，或从左侧目录浏览。</div>' : ""}</div>`);
  }

  function showCard(id) {
    const card = cards.find((item) => item.id === id);
    if (!card) { hideModal(); returnContext = null; backgroundHash = ""; layout("未找到卡牌", intro("未找到这张卡牌", '<a class="text-link" href="#swords">返回九大剑术 →</a>')); return; }
    const counterpart = card.evolution ? cards.find((item) => item.name === card.evolution) : cards.find((item) => item.evolution === card.name);
    const upgradeCost = card.upgradeCost ?? card.cost;
    const upgradeText = card.upgradeDescription || card.description;
    const unchanged = upgradeText === card.description && upgradeCost === card.cost;
    modalBody.innerHTML = `<div class="detail"><div class="detail-art"><img src="${imageFor(card.image)}" alt="${escapeHtml(card.name)}卡牌插画"><p>${escapeHtml(card.group)} · ${escapeHtml(card.group === "九大剑术" ? card.kind : rarityLabel(card.rarity))}</p></div><div class="detail-copy"><span class="detail-label">${escapeHtml(typeLabel(card.type))} · ${escapeHtml(rarityLabel(card.rarity))}</span><h2 id="modal-title">${escapeHtml(card.name)}</h2><div class="detail-row"><span class="detail-chip">${escapeHtml(costLabel(card.cost))} 费${upgradeCost !== card.cost ? ` → ${escapeHtml(costLabel(upgradeCost))} 费` : ""}</span><a class="text-link" href="${card.group === "九大剑术" ? "#swords" : "#muzixi"}">所属卡池 →</a></div><div class="detail-effects"><div class="effect-block"><h3>基础</h3><p>${richText(card.description)}</p></div><div class="effect-block upgraded"><h3>升级后</h3>${unchanged ? '<p class="unchanged">升级不改变费用与牌面效果。</p>' : `<p>${richText(upgradeText)}</p>`}</div></div>${counterpart ? `<div class="detail-links"><span>${card.evolution ? "顿悟形态" : "基础剑术"}</span><a class="text-link" href="${cardHref(counterpart)}" data-card="${escapeHtml(counterpart.id)}">${escapeHtml(counterpart.name)} →</a><a class="text-link" href="#mechanics/enlightenment">顿悟规则 ↗</a></div>` : ""}<p class="detail-hint">点击效果中的关键词可查看机制说明。</p></div></div>`;
    modal.hidden = false;
    if (shell) shell.inert = true;
    document.body.classList.add("modal-open");
    document.getElementById("modal-close").focus();
    document.title = `${card.name} · 九大剑术图鉴`;
  }
  function hideModal() {
    modal.hidden = true;
    if (shell) shell.inert = false;
    document.body.classList.remove("modal-open");
  }
  function closeCard() {
    if (modal.hidden) return;
    if (openedFromPage) history.back();
    else { history.replaceState(null, "", returnContext?.hash || "#swords"); render(); }
  }
  function setMenu(open, restoreFocus = false) {
    const sidebar = document.getElementById("sidebar");
    const button = document.getElementById("menu-button");
    const isMobile = window.matchMedia("(max-width: 800px)").matches;
    open = open && isMobile;
    sidebar.classList.toggle("open", open);
    sidebar.inert = isMobile && !open;
    if (isMobile && !open) sidebar.setAttribute("aria-hidden", "true"); else sidebar.removeAttribute("aria-hidden");
    document.getElementById("main-content").inert = open;
    button.setAttribute("aria-expanded", String(open));
    button.setAttribute("aria-label", open ? "关闭导航" : "打开导航");
    const shade = document.getElementById("menu-shade");
    if (shade) shade.hidden = !open;
    if (open) (sidebar.querySelector('.nav a[aria-current="page"]') || sidebar.querySelector(".nav a"))?.focus();
    else if (restoreFocus) requestAnimationFrame(() => {
      if (!sidebar.classList.contains("open") && modal.hidden) {
        (window.matchMedia("(max-width: 800px)").matches ? button : document.getElementById("main-content")).focus({ preventScroll: true });
      }
    });
  }
  function renderContent({ route, params }) {
    if (route === "swords" || route === "muzixi") cardsPage(route, params);
    else if (route === "relics") relicsPage(params);
    else if (route === "events") events();
    else if (route === "mechanics") mechanics();
    else if (route === "search") searchPage(params);
    else home();
  }
  function render() {
    const { route, id, params } = routeInfo();
    if (route === "cards") {
      setMenu(false);
      const card = cards.find((item) => item.id === id);
      if (!returnContext) {
        const saved = history.state?.wikiCardOrigin;
        const sourceRoute = card?.group === "木子汐卡牌" ? "muzixi" : "swords";
        returnContext = saved && typeof saved.hash === "string" && routes[routeInfo(saved.hash).route] ? saved : { hash: "#" + sourceRoute, scroll: 0, focusCard: null };
        renderContent(routeInfo(returnContext.hash));
        backgroundHash = returnContext.hash;
        openedFromPage = Boolean(saved && history.state?.wikiCardFromPage);
        window.scrollTo({ top: returnContext.scroll || 0, behavior: "instant" });
      }
      history.replaceState({ ...history.state, wikiCardOrigin: returnContext, wikiCardFromPage: openedFromPage }, "", location.href);
      showCard(id);
      return;
    }
    const context = returnContext;
    const restoring = context && location.hash === context.hash && backgroundHash === context.hash;
    hideModal();
    returnContext = null;
    openedFromPage = false;
    setMenu(false);
    if (!restoring) {
      renderContent({ route, params });
      backgroundHash = location.hash || "#home";
    } else {
      breadcrumbs.textContent = `模组图鉴 / ${routes[route] || "首页"}`;
      document.title = `${routes[route] || "九大剑术"} · 模组图鉴`;
    }
    updateCounts();
    const globalInput = document.querySelector('#global-search [name="q"]');
    if (globalInput) globalInput.value = route === "search" ? params.get("q") || "" : "";
    if (restoring) {
      window.scrollTo({ top: context.scroll, behavior: "instant" });
      const trigger = Array.from(app.querySelectorAll("[data-card]")).find((element) => element.dataset.card === context.focusCard);
      trigger?.focus({ preventScroll: true });
    } else if (id) {
      const target = document.getElementById(`${route}-${id}`);
      if (target) target.scrollIntoView({ block: "start", behavior: "instant" }); else window.scrollTo({ top: 0, behavior: "instant" });
    } else window.scrollTo({ top: 0, behavior: "instant" });
  }

  document.addEventListener("submit", (event) => {
    if (!event.target.matches("[data-site-search], #global-search")) return;
    event.preventDefault();
    const query = new FormData(event.target).get("q")?.toString().trim() || "";
    const target = hashFor("search", { q: query });
    if (location.hash === target) render(); else location.hash = target;
  });
  document.addEventListener("click", (event) => {
    if (event.target.closest(".skip-link")) {
      event.preventDefault();
      setMenu(false);
      document.getElementById("main-content").focus();
      return;
    }
    const link = event.target.closest("[data-card]");
    if (link && !event.ctrlKey && !event.metaKey && !event.shiftKey && !event.altKey && event.button === 0) {
      event.preventDefault();
      const id = link.dataset.card;
      if (modal.hidden) {
        returnContext = { hash: location.hash || "#home", scroll: window.scrollY, focusCard: id };
        openedFromPage = true;
        location.hash = cardHref({ id }).slice(1);
      } else {
        history.replaceState(history.state, "", cardHref({ id }));
        showCard(id);
      }
    }
    if (event.target === modal) closeCard();
    if (event.target.closest(".nav a, .brand")) setMenu(false);
  });
  document.getElementById("modal-close").addEventListener("click", closeCard);
  document.getElementById("menu-button").addEventListener("click", () => setMenu(!document.getElementById("sidebar").classList.contains("open"), true));
  document.getElementById("menu-shade")?.addEventListener("click", () => setMenu(false, true));
  document.addEventListener("keydown", (event) => {
    if (event.key === "Escape") {
      if (!modal.hidden) closeCard();
      else if (document.getElementById("sidebar").classList.contains("open")) setMenu(false, true);
    }
    if (event.key !== "Tab" || modal.hidden) return;
    const focusables = Array.from(modal.querySelectorAll('a[href], button:not([disabled]), input:not([disabled]), select:not([disabled]), [tabindex="0"]'));
    const first = focusables[0];
    const last = focusables[focusables.length - 1];
    if (event.shiftKey && document.activeElement === first) { event.preventDefault(); last?.focus(); }
    else if (!event.shiftKey && document.activeElement === last) { event.preventDefault(); first?.focus(); }
  });
  window.addEventListener("hashchange", render);
  window.matchMedia("(max-width: 800px)").addEventListener("change", () => setMenu(false, document.getElementById("sidebar").contains(document.activeElement) || document.activeElement === document.getElementById("menu-shade")));
  updateCounts();
  render();
})();
