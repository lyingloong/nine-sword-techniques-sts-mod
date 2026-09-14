package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import ninesword.muzixi.powers.HarmonicSoulPower;

/** Converts up to nine Harmonic Soul when Tear persona is entered. */
public class EnterTearPersonaAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public EnterTearPersonaAction(AbstractPlayer player) {
        this.player = player;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (player == null || !player.hasPower(HarmonicSoulPower.POWER_ID)) {
            isDone = true;
            return;
        }
        int amount = Math.min(9, Math.max(0, player.getPower(HarmonicSoulPower.POWER_ID).amount));
        if (amount > 0) {
            addToBot(new ReducePowerAction(player, player, HarmonicSoulPower.POWER_ID, amount));
            for (int i = 0; i < amount; i++) {
                addToBot(new DrawCardAction(player, 1));
                addToBot(new GainEnergyAction(1));
            }
        }
        isDone = true;
    }
}
