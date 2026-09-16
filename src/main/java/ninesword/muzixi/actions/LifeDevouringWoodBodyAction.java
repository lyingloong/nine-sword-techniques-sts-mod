package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.StrengthPower;
import ninesword.muzixi.powers.VitalityPower;

/** Converts all Vitality present at resolution into an equal amount of Strength. */
public class LifeDevouringWoodBodyAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public LifeDevouringWoodBodyAction(AbstractPlayer player) {
        this.player = player;
        actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        if (player != null) {
            int lost = VitalityPower.lose(player, VitalityPower.getAmount(player));
            VitalityPower.removeExpired(player);
            if (lost > 0) {
                addToTop(new ApplyPowerAction(player, player,
                        new StrengthPower(player, lost), lost));
            }
        }
        isDone = true;
    }
}
