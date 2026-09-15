package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import ninesword.muzixi.powers.VitalityPower;

/** Resolves Flourish's branch after earlier power triggers have settled. */
public class FlourishAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int threshold;
    private final int drawAmount;
    private final int fallbackVitality;

    public FlourishAction(AbstractPlayer player, int threshold,
                          int drawAmount, int fallbackVitality) {
        this.player = player;
        this.threshold = threshold;
        this.drawAmount = drawAmount;
        this.fallbackVitality = fallbackVitality;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (player != null) {
            if (VitalityPower.getAmount(player) >= threshold) {
                if (drawAmount > 0) {
                    addToTop(new DrawCardAction(player, drawAmount));
                }
            } else if (fallbackVitality > 0) {
                VitalityPower.gain(player, fallbackVitality);
            }
        }
        isDone = true;
    }
}
