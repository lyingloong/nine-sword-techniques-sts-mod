package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import ninesword.muzixi.powers.VitalityPower;

/** Applies the Vitality/Wither mutual-exclusion rules as one action. */
public class GainVitalityAction extends AbstractGameAction {
    private final AbstractCreature target;
    private final int amount;

    public GainVitalityAction(AbstractCreature target, int amount) {
        this.target = target;
        this.amount = amount;
        actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        // VitalityPower.gain performs the mutual-exclusion bookkeeping and
        // sends the single post-gain notification itself.  Keeping the
        // notification in one place prevents powers such as Evergreen from
        // triggering twice for one card effect.
        VitalityPower.gain(target, amount);
        isDone = true;
    }
}
