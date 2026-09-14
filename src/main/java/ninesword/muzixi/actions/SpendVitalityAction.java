package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import ninesword.muzixi.powers.VitalityPower;

/** Spends at most the currently available Vitality; it cannot create Wither. */
public class SpendVitalityAction extends AbstractGameAction {
    private final AbstractCreature target;
    private final int amount;

    public SpendVitalityAction(AbstractCreature target, int amount) {
        this.target = target;
        this.amount = amount;
        actionType = ActionType.REDUCE_POWER;
    }

    @Override
    public void update() {
        VitalityPower.spend(target, amount);
        isDone = true;
    }
}
