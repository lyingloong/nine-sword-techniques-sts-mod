package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import ninesword.muzixi.powers.MuzixiPersonaPower;
import ninesword.muzixi.powers.TearPersonaPower;

public class SwitchPersonaAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public SwitchPersonaAction(AbstractPlayer player) {
        this.player = player;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        boolean hasMuzixiPersona = player.hasPower(MuzixiPersonaPower.POWER_ID);
        boolean hasTearPersona = player.hasPower(TearPersonaPower.POWER_ID);

        if (hasMuzixiPersona && !hasTearPersona) {
            queueSwitch(new TearPersonaPower(player), hasMuzixiPersona, hasTearPersona);
        } else {
            // No persona (or a malformed state containing both markers) starts
            // in Muzixi's persona; this is also the deterministic first switch.
            queueSwitch(new MuzixiPersonaPower(player), hasMuzixiPersona, hasTearPersona);
        }

        isDone = true;
    }

    private void queueSwitch(com.megacrit.cardcrawl.powers.AbstractPower nextPersona,
                             boolean removeMuzixi, boolean removeTear) {
        // Append in execution order: remove old markers, then apply the new one.
        // TearPersonaPower owns its universal enter effect (including Harmonic Soul conversion).
        if (removeTear) {
            addToBot(new RemoveSpecificPowerAction(player, player, TearPersonaPower.POWER_ID));
        }
        if (removeMuzixi) {
            addToBot(new RemoveSpecificPowerAction(player, player, MuzixiPersonaPower.POWER_ID));
        }
        addToBot(new ApplyPowerAction(player, player, nextPersona, 1));
    }
}
