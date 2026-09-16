package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import ninesword.muzixi.powers.VitalityPower;

/** Resolves Flourish using the standard X-cost and Chemical X conventions. */
public class FlourishAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int drawMultiplier;
    private final boolean freeToPlayOnce;
    private final int energyOnUse;

    public FlourishAction(AbstractPlayer player, int drawMultiplier,
                          boolean freeToPlayOnce, int energyOnUse) {
        this.player = player;
        this.drawMultiplier = Math.max(0, drawMultiplier);
        this.freeToPlayOnce = freeToPlayOnce;
        this.energyOnUse = energyOnUse;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (player != null) {
            int effect = energyOnUse == -1 ? EnergyPanel.totalCount : energyOnUse;
            if (player.hasRelic(ChemicalX.ID)) {
                effect += 2;
                player.getRelic(ChemicalX.ID).flash();
            }

            if (effect > 0) {
                VitalityPower.gain(player, effect);
                int drawAmount = safeMultiply(effect, drawMultiplier);
                if (drawAmount > 0) {
                    addToTop(new DrawCardAction(player, drawAmount));
                }
                if (!freeToPlayOnce) {
                    player.energy.use(EnergyPanel.totalCount);
                }
            }
        }
        isDone = true;
    }

    private static int safeMultiply(int left, int right) {
        long result = (long) left * right;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }
}
