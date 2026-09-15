package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import ninesword.muzixi.powers.VitalityPower;

/** Gains Vitality, then checks the post-gain threshold for Vital Cycle. */
public class VitalCycleAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int gain;
    private final int threshold;
    private final int draws;

    public VitalCycleAction(AbstractPlayer player, int gain, int threshold, int draws) {
        this.player = player;
        this.gain = gain;
        this.threshold = threshold;
        this.draws = draws;
        actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        if (player != null) {
            VitalityPower.gain(player, gain);
            if (VitalityPower.getAmount(player) >= threshold && draws > 0) {
                addToTop(new DrawCardAction(player, draws));
            }
        }
        isDone = true;
    }
}
