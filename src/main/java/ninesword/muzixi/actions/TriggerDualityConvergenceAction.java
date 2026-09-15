package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import ninesword.muzixi.powers.DualityConvergencePower;

/** Fires the stored two-persona payoff after a real persona transition. */
public class TriggerDualityConvergenceAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public TriggerDualityConvergenceAction(AbstractPlayer player) {
        this.player = player;
        actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        DualityConvergencePower.trigger(player);
        isDone = true;
    }
}
