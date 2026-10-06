(function () {
  const app = document.getElementById("app");
  const breadcrumbs = document.getElementById("breadcrumbs");
  const modal = document.getElementById("modal");
  const modalBody = document.getElementById("modal-body");
  const routes = { home: "首页", swords: "九大剑术", muzixi: "木子汐", events: "事件", mechanics: "机制" };
  const cards = window.NINE_SWORD_DATA?.cards || [];
  const assets = "assets/";

  function escapeHtml(value) {
    return String(value ?? "").replace(/[&<>\"]/g, (char) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;" }[char]));
  }

  function imageFor(path, fallback = "assets/card-placeholder.png") {
    return path ? assets + path : fallback;
  }

  function rarityLabel(value) {
    return ({ BASIC: "基础", COMMON: "普通", UNCOMMON: "罕见", RARE: "稀有", SPECIAL: "特殊" }[value] || value || "");
  }

  function typeLabel(value) {
    return ({ ATTACK: "攻击", SKILL: "技能", POWER: "能力", STATUS: "状态", CURSE: "诅咒" }[value] || value || "卡牌");
  }

  function costLabel(value) {
    return value === -1 ? "X" : value === -2 ? "—" : value === undefined ? "" : String(value);
  }

  function layout(title, body) {
    breadcrumbs.textContent = `模组图鉴 / ${title}`;
    document.querySelectorAll(".nav a").forEach((link) => link.classList.toggle("active", link.dataset.route === currentRoute()));
    app.innerHTML = body;
  }

  function currentRoute() { return (location.hash.slice(1).split("/")[0] || "home"); }

  function home() {
    layout("首页", `
      <section class="hero">
        <div class="hero-copy">
          <div class="eyebrow">NINE SWORD TECHNIQUES · SLAY THE SPIRE MOD</div>
          <h1>九大剑术，<br>皆自剑楼出。</h1>
          <p>以《我有一身被动技》为灵感的《杀戮尖塔》主题模组。九张基础剑术牌、九张顿悟形态，以及一个围绕生机与人格展开的新角色。</p>
          <div class="button-row"><a class="button" href="#swords">浏览九大剑术</a><a class="button secondary" href="#muzixi">认识木子汐</a></div>
        </div>
      </section>
      <div class="stats" aria-label="内容统计">
        <div class="stat"><strong>18</strong><span>剑术牌与顿悟形态</span></div>
        <div class="stat"><strong>67</strong><span>木子汐专属卡牌</span></div>
        <div class="stat"><strong>8</strong><span>主题遗物</span></div>
        <div class="stat"><strong>3</strong><span>探索事件</span></div>
      </div>
      <div class="section-heading"><div><h2>从这里开始</h2></div><p>像查阅《杀戮尖塔》资料库一样，按主题浏览模组内容。</p></div>
      <div class="feature-grid">
        <a class="feature" href="#swords"><span class="feature-index">01 / SWORD TECHNIQUES</span><div><h3>九大剑术</h3><p>基础剑术、顿悟形态与共享遗物</p></div><span class="feature-arrow">→</span></a>
        <a class="feature" href="#muzixi"><span class="feature-index">02 / CHARACTER</span><div><h3>木子汐</h3><p>生机、麻痹与一体双魂</p></div><span class="feature-arrow">→</span></a>
        <a class="feature" href="#events"><span class="feature-index">03 / EVENTS</span><div><h3>事件</h3><p>白窟、虚空岛与八尊谙</p></div><span class="feature-arrow">→</span></a>
        <a class="feature" href="#mechanics"><span class="feature-index">04 / RULES</span><div><h3>机制</h3><p>理解模组中的专属规则</p></div><span class="feature-arrow">→</span></a>
      </div>
      <div class="section-heading"><div><h2>模组演示</h2></div><p>木子汐的森林与双魂，是这套角色体验的第一眼。</p></div>
      <figure class="showcase"><img src="${assets}muzixi/character/victory1.png" alt="木子汐演示场景"><figcaption class="showcase-copy"><span class="eyebrow">MUZIXI · DEMO SCENE</span><h3>一体双魂，生机入战</h3><p>木子汐在两种人格之间切换，以生机扩展战斗空间，再用麻痹和真伤压制敌人。进入角色页面，查看完整卡池与规则。</p><div class="button-row" style="margin-top:22px"><a class="button secondary" href="#muzixi">查看角色资料</a></div></figcaption></figure>
      <blockquote class="quote">非葬剑冢亲传，自行避视，如遭反噬，一切自负。<br><small>—— 温庭，《剑经》</small></blockquote>
    `);
  }

  function cardTile(card) {
    const cost = costLabel(card.cost);
    return `<button class="card-tile" type="button" data-card="${escapeHtml(card.id)}"><img src="${imageFor(card.image)}" alt="${escapeHtml(card.name)}"><span class="card-meta"><strong>${escapeHtml(card.name)}</strong><span><i class="tag">${escapeHtml(card.group)}</i>${escapeHtml(typeLabel(card.type))} · ${escapeHtml(rarityLabel(card.rarity))}</span><small>${escapeHtml(cost === undefined ? "" : `${cost} 能量`)}</small></span></button>`;
  }

  function relicTile(relic) {
    return `<article class="relic"><img src="${assets}${relic.image}" alt=""><div><strong>${escapeHtml(relic.name)}</strong><p>${escapeHtml(relic.description)}</p></div></article>`;
  }

  function cardsPage() {
    const swordCards = cards.filter((card) => card.group === "九大剑术");
    const sharedRelics = (window.NINE_SWORD_DATA?.relics || []).filter((relic) => relic.group === "九大剑术");
    layout("九大剑术", `<div class="page-intro"><div class="eyebrow">01 / SWORD TECHNIQUES</div><h1>九大剑术</h1><p>九张基础剑术牌可以通过顿悟替换为对应的特殊形态。每一对牌都保留自己的战斗节奏，从藏剑到出鞘，从鬼剑到御魂。</p></div><div class="toolbar"><input class="search" id="card-search" type="search" placeholder="搜索卡牌名称或描述"><select class="select" id="card-filter"><option value="all">全部卡牌</option><option value="基础剑术">基础剑术</option><option value="顿悟形态">顿悟形态</option></select></div><div class="card-grid" id="card-grid">${swordCards.map(cardTile).join("")}</div><div class="section-heading"><div><h2>共享遗物</h2></div><p>所有角色都可以遇见的剑术主题遗物</p></div><div class="relic-list">${sharedRelics.map(relicTile).join("")}</div><div class="section-heading"><div><h2>共享药水</h2></div></div><div class="panel"><p>当前版本暂未注册共享药水。此处保留栏目，后续加入药水后会与九大剑术内容一起更新。</p></div>`);
    bindCardList(swordCards);
  }

  function muzixi() {
    const muzixiCards = cards.filter((card) => card.group === "木子汐卡牌");
    const relics = (window.NINE_SWORD_DATA?.relics || []).filter((relic) => relic.group === "木子汐");
    layout("木子汐", `<div class="page-intro"><div class="eyebrow">02 / CHARACTER</div><h1>木子汐</h1><p>独立于九大剑术体系的青绿色角色。积累生机，操纵麻痹，在木子汐与泪汐儿人格之间切换，用临时最大生命换取爆发。</p></div><div class="split-layout"><div><div class="panel"><h3>角色概览</h3><p>初始生命 70 · 3 能量 · 99 金币</p><p>初始牌组：4 张打击、4 张防御、生机奔涌、生命汲取</p><p>初始遗物：神魔瞳</p><div class="mini-grid"><div><strong>生机</strong><p>获得生机会提高本场战斗的临时最大生命。</p></div><div><strong>一体双魂</strong><p>人格切换会消耗灵魂共鸣，换取抽牌与能量。</p></div></div></div><div class="section-heading"><div><h2>角色卡牌</h2></div><p>共 ${muzixiCards.length} 张已收录卡牌</p></div><div class="toolbar"><input class="search" id="muzixi-search" type="search" placeholder="搜索木子汐卡牌"><select class="select" id="muzixi-filter"><option value="all">全部稀有度</option><option value="BASIC">基础</option><option value="COMMON">普通</option><option value="UNCOMMON">罕见</option><option value="RARE">稀有</option></select></div><div class="card-grid" id="muzixi-grid">${muzixiCards.map(cardTile).join("")}</div><div class="section-heading"><div><h2>专属遗物</h2></div><p>木子汐的角色初始遗物</p></div><div class="relic-list">${relics.map(relicTile).join("")}</div><div class="section-heading"><div><h2>药水</h2></div></div><div class="panel"><p>当前版本暂未注册木子汐专属药水。此处保留栏目，后续加入药水后会与角色卡牌和遗物一起更新。</p></div></div><aside><img class="portrait" src="${assets}muzixi-big.png" alt="木子汐立绘"><div class="panel" style="margin-top:12px"><h3>专属内容</h3><p>神魔瞳是木子汐的初始遗物，战斗开始时进入木子汐人格并获得生机。</p><p>她的卡池围绕生机、麻痹和人格切换展开。</p></div></aside></div>`);
    bindCardList(muzixiCards, "muzixi");
  }

  function bindCardList(source, prefix = "") {
    const search = document.getElementById(prefix ? "muzixi-search" : "card-search");
    const filter = document.getElementById(prefix ? "muzixi-filter" : "card-filter");
    const grid = document.getElementById(prefix ? "muzixi-grid" : "card-grid");
    const update = () => { const query = search.value.trim().toLowerCase(); const value = filter.value; const visible = source.filter((card) => (!query || `${card.name} ${card.description}`.toLowerCase().includes(query)) && (value === "all" || card.kind === value)); grid.innerHTML = visible.length ? visible.map(cardTile).join("") : `<div class="empty">没有找到符合条件的卡牌。</div>`; bindCardButtons(); };
    search.addEventListener("input", update); filter.addEventListener("change", update); bindCardButtons();
  }

  function events() {
    const list = window.NINE_SWORD_DATA?.events || [];
    layout("事件", `<div class="page-intro"><div class="eyebrow">03 / EVENTS</div><h1>事件</h1><p>在高塔的不同阶段遇见剑楼留下的痕迹。每个事件都提供一次带有代价的选择，下面按游戏实际选项展开。</p></div><div class="event-list event-list-detailed">${list.map((event) => `<article class="event-card"><div class="event-visual"><img src="${assets}${event.image}" alt="${escapeHtml(event.name)}"><div><span class="event-act">${escapeHtml(event.act)}</span><h2>${escapeHtml(event.name)}</h2><p>${escapeHtml(event.summary)}</p></div></div><div class="event-story">${escapeHtml(event.story)}</div><div class="event-options">${event.options.map((option) => `<div class="event-option"><div class="event-option-title"><strong>${escapeHtml(option.name)}</strong><span>${escapeHtml(option.condition)}</span></div><div class="event-option-grid"><div><small>代价</small><p>${escapeHtml(option.cost)}</p></div><div><small>结果</small><p>${escapeHtml(option.reward)}</p></div></div><div class="event-result">${escapeHtml(option.result)}</div></div>`).join("")}</div><div class="event-notes"><strong>结算细节</strong><ul>${event.notes.map((note) => `<li>${escapeHtml(note)}</li>`).join("")}</ul></div></article>`).join("")}</div>`);
  }

  function mechanics() {
    const list = window.NINE_SWORD_DATA?.mechanics || [];
    layout("机制", `<div class="page-intro"><div class="eyebrow">04 / MECHANICS</div><h1>机制</h1><p>这些规则定义了模组的独特节奏，也是阅读卡牌描述时最重要的关键词。每条机制都列出结算顺序、边界和一个具体例子。</p></div><div class="mechanic-list">${list.map((item) => `<article class="mechanic-detail"><div class="mechanic-head"><span class="mechanic-key">${escapeHtml(item.key)}</span><h2>${escapeHtml(item.name)}</h2><p>${escapeHtml(item.description)}</p></div><div class="mechanic-columns"><div><strong>规则</strong><ul>${item.rules.map((rule) => `<li>${escapeHtml(rule)}</li>`).join("")}</ul></div><div class="mechanic-example"><strong>例子</strong><p>${escapeHtml(item.example)}</p><div class="button-row">${item.links.map((link) => `<a class="button secondary" href="#${escapeHtml(link.route)}">${escapeHtml(link.name)} →</a>`).join("")}</div></div></div></article>`).join("")}</div>`);
  }

  function bindCardButtons() { document.querySelectorAll("[data-card]").forEach((button) => button.addEventListener("click", () => showCard(button.dataset.card))); }
  function showCard(id) { const card = cards.find((item) => item.id === id); if (!card) return; const cost = costLabel(card.cost); const upgradeCost = costLabel(card.upgradeCost); modalBody.innerHTML = `<div class="detail"><img src="${imageFor(card.image)}" alt="${escapeHtml(card.name)}"><div><span class="detail-label">${escapeHtml(card.group)} / ${escapeHtml(card.kind || rarityLabel(card.rarity))}</span><h2 id="modal-title">${escapeHtml(card.name)}</h2><div class="detail-row"><span class="detail-chip">${escapeHtml(typeLabel(card.type))}</span><span class="detail-chip">${escapeHtml(cost)} 能量${upgradeCost !== cost ? ` → ${escapeHtml(upgradeCost)}` : ""}</span><span class="detail-chip">${escapeHtml(rarityLabel(card.rarity))}</span></div><p>${escapeHtml(card.description || "暂无描述")}</p>${card.upgradeDescription ? `<p><strong>升级后</strong><br>${escapeHtml(card.upgradeDescription)}</p>` : ""}${card.evolution ? `<p><strong>顿悟形态</strong><br>${escapeHtml(card.evolution)}</p>` : ""}</div></div>`; modal.hidden = false; }
  function render() { const route = currentRoute(); ({ home, swords: cardsPage, muzixi, events, mechanics }[route] || home)(); window.scrollTo(0, 0); }
  document.addEventListener("click", (event) => { if (event.target === modal) modal.hidden = true; });
  document.getElementById("modal-close").addEventListener("click", () => { modal.hidden = true; });
  document.addEventListener("keydown", (event) => { if (event.key === "Escape") modal.hidden = true; });
  document.getElementById("menu-button").addEventListener("click", () => document.getElementById("sidebar").classList.toggle("open"));
  document.querySelectorAll(".nav a, .brand").forEach((link) => link.addEventListener("click", () => document.getElementById("sidebar").classList.remove("open")));
  window.addEventListener("hashchange", render); render();
})();
