package ninesword.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.cards.evolved.AbsoluteImperialRule;

import java.util.HashMap;
import java.util.Map;

public class AbsoluteImperialRuleAction extends AbstractGameAction {
    private final AbsoluteImperialRule card;
    private final AbstractPlayer player;
    private final int hitsRemaining;
    private final Map<AbstractMonster, Integer> previousHits;

    public AbsoluteImperialRuleAction(AbsoluteImperialRule card, AbstractPlayer player, int hits) {
        this(card, player, hits, new HashMap<AbstractMonster, Integer>());
    }

    private AbsoluteImperialRuleAction(AbsoluteImperialRule card, AbstractPlayer player, int hits,
                                       Map<AbstractMonster, Integer> previousHits) {
        this.card = card;
        this.player = player;
        this.hitsRemaining = hits;
        this.previousHits = previousHits;
    }

    @Override
    public void update() {
        if (hitsRemaining <= 0 || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            isDone = true;
            return;
        }

        AbstractMonster target = AbstractDungeon.getMonsters()
                .getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (target == null) {
            isDone = true;
            return;
        }

        int bonusDamage = previousHits.containsKey(target) ? previousHits.get(target) : 0;
        int hitDamage = card.calculateHitDamage(target, bonusDamage);
        previousHits.put(target, bonusDamage + 1);

        if (hitsRemaining > 1) {
            addToTop(new AbsoluteImperialRuleAction(card, player, hitsRemaining - 1, previousHits));
        }
        addToTop(new DamageAction(target,
                new DamageInfo(player, hitDamage, card.damageTypeForTurn), AttackEffect.SLASH_HORIZONTAL));
        isDone = true;
    }
}
