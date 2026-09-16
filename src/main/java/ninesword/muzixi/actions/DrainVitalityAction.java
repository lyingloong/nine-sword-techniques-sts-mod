package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.VitalityPower;

/** Makes the target lose Vitality while granting the full stated amount to the player. */
public class DrainVitalityAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractMonster target;
    private final int amount;

    public DrainVitalityAction(AbstractPlayer player, AbstractMonster target, int amount) {
        this.player = player;
        this.target = target;
        this.amount = Math.max(0, amount);
        actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        if (player != null && target != null && !target.isDeadOrEscaped() && amount > 0) {
            // lose() converts any amount beyond the target's current Vitality
            // into Wither. The player gains the full card value either way.
            VitalityPower.lose(target, amount);
            VitalityPower.removeExpired(target);
            VitalityPower.gain(player, amount);
        }
        isDone = true;
    }
}
