package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.ParalysisPower;

/** Removes all paralysis, then deals the card-aware damage snapshotted on use. */
public class NerveRuptureAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractMonster target;
    private final int damage;

    public NerveRuptureAction(AbstractPlayer player, AbstractMonster target,
                              int damage) {
        this.player = player;
        this.target = target;
        this.damage = Math.max(0, damage);
        actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (target != null && !target.isDeadOrEscaped()) {
            int removed = ParalysisPower.getAmount(target);
            if (removed > 0) {
                ParalysisPower.remove(target, removed);
            }
            addToTop(new DamageAction(target,
                    new DamageInfo(player, damage, DamageInfo.DamageType.NORMAL),
                    AttackEffect.SLASH_HEAVY));
        }
        isDone = true;
    }
}
