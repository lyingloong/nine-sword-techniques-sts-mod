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
        if (hasTear) {
            addToBot(new RemoveSpecificPowerAction(player, player, TearPersonaPower.POWER_ID));
        }
        // Apply after removal even if the malformed state contains both
        // markers. Removing the existing Muzixi marker as well avoids stacking
        // its amount when this action repairs a malformed state.
        if (hasTear || !hasMuzixi) {
            if (hasMuzixi) {
                addToBot(new RemoveSpecificPowerAction(player, player, MuzixiPersonaPower.POWER_ID));
            }
            addToBot(new ApplyPowerAction(player, player, new MuzixiPersonaPower(player), 1));
        }
        isDone = true;
    }
}
