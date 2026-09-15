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
            // This is a genuine transition from Muzixi to Tear.
            queueSwitch(new TearPersonaPower(player), true, false, true);
        } else {
            // No persona (or a malformed state containing both markers) starts
            // in Muzixi's persona; this is also the deterministic first switch.
            // A neutral Persona Shift still counts as a switch for effects such
            // as Duality Convergence; direct neutral entry cards do not.
            boolean actualSwitch = !hasMuzixiPersona;
            queueSwitch(new MuzixiPersonaPower(player), hasMuzixiPersona, hasTearPersona,
                    actualSwitch);
        }

        isDone = true;
    }

    private void queueSwitch(com.megacrit.cardcrawl.powers.AbstractPower nextPersona,
                             boolean removeMuzixi, boolean removeTear,
                             boolean actualSwitch) {
        // This action may have card effects already queued behind it. Insert
        // the transition at the top in reverse order so removal, application,
        // Tear's entry conversion, and the switch payoff all finish first.
        if (actualSwitch) {
            addToTop(new TriggerDualityConvergenceAction(player));
        }
        addToTop(new ApplyPowerAction(player, player, nextPersona, 1));
        if (removeMuzixi) {
            addToTop(new RemoveSpecificPowerAction(player, player, MuzixiPersonaPower.POWER_ID));
        }
        if (removeTear) {
            addToTop(new RemoveSpecificPowerAction(player, player, TearPersonaPower.POWER_ID));
        }
    }
}
