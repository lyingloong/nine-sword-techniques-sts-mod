package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.ParalysisPower;

public class NeuralCollapseAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractMonster target;
    private final int interval;

    public NeuralCollapseAction(AbstractPlayer player, AbstractMonster target, int interval) {
        this.player = player;
        this.target = target;
        this.interval = Math.max(1, interval);
        actionType = ActionType.DEBUFF;
    }

    @Override
    public void update() {
        if (target != null && !target.isDeadOrEscaped()) {
            int stacks = ParalysisPower.getAmount(target);
            int extra = stacks / interval;
            if (extra > 0) {
                addToTop(new ApplyPowerAction(target, player,
                        new ParalysisPower(target, extra), extra));
            }
        }
        isDone = true;
    }
}
