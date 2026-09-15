package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.ParalysisPower;

/** Applies Echoing Numbness' threshold check after the initial Paralysis. */
public class EchoingNumbnessAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractMonster target;
    private final int paralysis;
    private final int threshold;
    private final int draws;

    public EchoingNumbnessAction(AbstractPlayer player, AbstractMonster target,
                                 int paralysis, int threshold, int draws) {
        this.player = player;
        this.target = target;
        this.paralysis = paralysis;
        this.threshold = threshold;
        this.draws = draws;
        actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        if (target != null && !target.isDeadOrEscaped()) {
            // Insert in reverse so both steps resolve before UseCardAction.
            addToTop(new ParalysisThresholdAction(player, target, threshold, 0, draws));
            addToTop(new ApplyPowerAction(target, player,
                    new ParalysisPower(target, paralysis), paralysis));
        }
        isDone = true;
    }
}
