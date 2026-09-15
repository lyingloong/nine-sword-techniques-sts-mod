package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import ninesword.muzixi.powers.MuzixiPersonaPower;
import ninesword.muzixi.powers.TearPersonaPower;

/** Enters Muzixi persona idempotently (used by Eye of Life). */
public class EnterMuzixiPersonaAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public EnterMuzixiPersonaAction(AbstractPlayer player) {
        this.player = player;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (player == null) {
            isDone = true;
            return;
        }
        boolean hasTear = player.hasPower(TearPersonaPower.POWER_ID);
        boolean hasMuzixi = player.hasPower(MuzixiPersonaPower.POWER_ID);
        // Entering from the opposite marker is a real transition (for
        // example, Eye of Life while Tear is active).  Neutral entry and
        // repairing a malformed dual-marker state are not transitions.
        boolean actualSwitch = hasTear && !hasMuzixi;
        // Apply after removal even if the malformed state contains both
        // markers. Removing the existing Muzixi marker as well avoids stacking
        // its amount when this action repairs a malformed state.
        if (hasTear || !hasMuzixi) {
            // Insert in reverse execution order so a caller such as Eye of
            // Life cannot resolve its following effects before the persona.
            if (actualSwitch) {
                addToTop(new TriggerDualityConvergenceAction(player));
            }
            addToTop(new ApplyPowerAction(player, player, new MuzixiPersonaPower(player), 1));
            if (hasMuzixi) {
                addToTop(new RemoveSpecificPowerAction(player, player,
                        MuzixiPersonaPower.POWER_ID));
            }
            if (hasTear) {
                addToTop(new RemoveSpecificPowerAction(player, player,
                        TearPersonaPower.POWER_ID));
            }
        }
        isDone = true;
    }
}
