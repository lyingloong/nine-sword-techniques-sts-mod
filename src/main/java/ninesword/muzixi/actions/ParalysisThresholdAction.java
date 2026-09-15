package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import ninesword.muzixi.powers.ParalysisPower;

/** Resolves effects that depend on the target's Paralysis after it was applied. */
public class ParalysisThresholdAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractMonster target;
    private final int threshold;
    private final int weakAmount;
    private final int drawAmount;

    public ParalysisThresholdAction(AbstractPlayer player, AbstractMonster target,
                                    int threshold, int weakAmount, int drawAmount) {
        this.player = player;
        this.target = target;
        this.threshold = Math.max(0, threshold);
        this.weakAmount = Math.max(0, weakAmount);
        this.drawAmount = Math.max(0, drawAmount);
        actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        if (target != null && !target.isDeadOrEscaped()
                && ParalysisPower.getAmount(target) >= threshold) {
            // Reverse insertion preserves Weak -> Draw ahead of UseCardAction.
            if (drawAmount > 0) {
                addToTop(new DrawCardAction(player, drawAmount));
            }
            if (weakAmount > 0) {
                addToTop(new ApplyPowerAction(target, player,
                        new WeakPower(target, weakAmount, false), weakAmount));
            }
        }
        isDone = true;
    }
}
