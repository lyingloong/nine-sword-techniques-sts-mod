package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.cards.NerveRupture;
import ninesword.muzixi.powers.ParalysisPower;

/** Removes all paralysis and calculates damage from the layers removed at resolution. */
public class NerveRuptureAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractMonster target;
    private final NerveRupture card;

    public NerveRuptureAction(AbstractPlayer player, AbstractMonster target,
                              NerveRupture card) {
        this.player = player;
        this.target = target;
        this.card = card;
        actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (target != null && !target.isDeadOrEscaped()) {
            int removed = ParalysisPower.getAmount(target);
            if (removed > 0) {
                ParalysisPower.remove(target, removed);
            }
            int damage = card == null ? 0
                    : Math.max(0, card.calculateDamageForParalysis(target, removed));
            addToTop(new DamageAction(target,
                    new DamageInfo(player, damage, DamageInfo.DamageType.NORMAL),
                    AttackEffect.SLASH_HEAVY));
        }
        isDone = true;
    }
}
