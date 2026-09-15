package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import ninesword.muzixi.powers.HarmonicSoulPower;
import ninesword.muzixi.powers.MuzixiPersonaPower;
import ninesword.muzixi.powers.TearPersonaPower;

import java.util.ArrayList;

/** Converts up to nine Harmonic Soul when Tear persona is entered. */
public class EnterTearPersonaAction extends AbstractGameAction {
    private final AbstractPlayer player;
    /**
     * When true this action is an explicit request to enter the persona (for
     * example Eye of Death).  An already active Tear persona is left alone;
     * no second conversion of Harmonic Soul is performed.
     */
    private final boolean ensurePersona;

    public EnterTearPersonaAction(AbstractPlayer player) {
        this(player, false);
    }

    public EnterTearPersonaAction(AbstractPlayer player, boolean ensurePersona) {
        this.player = player;
        this.ensurePersona = ensurePersona;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (player == null) {
            isDone = true;
            return;
        }

        boolean entered;
        boolean actualSwitch = false;
        if (ensurePersona) {
            actualSwitch = !player.hasPower(TearPersonaPower.POWER_ID)
                    && player.hasPower(MuzixiPersonaPower.POWER_ID);
            // Explicit entry is idempotent.  This is important when Eye of
            // Death is played while Tear is already active: the card still
            // deals its HP loss, but must not spend Harmonic Soul again.
            if (player.hasPower(TearPersonaPower.POWER_ID)) {
                removeMuzixiMarker();
                isDone = true;
                return;
            }
            removeMuzixiMarker();
            if (!player.hasPower(TearPersonaPower.POWER_ID)) {
                // addPower does not invoke onInitialApplication (that hook is
                // owned by ApplyPowerAction), so this action remains the sole
                // owner of the conversion and cannot recursively enqueue
                // itself.
                player.addPower(new TearPersonaPower(player));
            }
            entered = player.hasPower(TearPersonaPower.POWER_ID);
        } else {
            // The no-argument form is called by TearPersonaPower's
            // onInitialApplication hook.  If the marker was removed before
            // this queued action runs, there is nothing to convert.
            if (!player.hasPower(TearPersonaPower.POWER_ID)) {
                isDone = true;
                return;
            }
            // Repair a malformed state in which both persona markers exist.
            // A newly applied Tear marker always wins and the old marker must
            // not continue granting its start-of-turn effect.
            removeMuzixiMarker();
            entered = true;
        }

        // Eye of Death is a direct-entry card rather than SwitchPersonaAction.
        // If it really crossed from Muzixi to Tear, let the mixed-persona
        // payoff observe the new marker as well.  Insert this before the
        // conversion actions below so the convergence trigger itself runs
        // immediately after conversion and before the card's remaining
        // effects (its queued payoff actions retain normal action ordering).
        if (ensurePersona && actualSwitch) {
            addToTop(new TriggerDualityConvergenceAction(player));
        }

        if (!entered || !player.hasPower(HarmonicSoulPower.POWER_ID)) {
            isDone = true;
            return;
        }

        int amount = Math.min(9, Math.max(0, player.getPower(HarmonicSoulPower.POWER_ID).amount));
        if (amount > 0) {
            /*
             * The caller may have actions immediately after this one (Eye of
             * Death's HP loss is one example).  Put conversion actions on top
             * in reverse order so all conversion effects resolve before the
             * caller's next action while retaining the normal
             * Reduce -> Draw -> Energy order.
             */
            for (int i = amount - 1; i >= 0; i--) {
                addToTop(new GainEnergyAction(1));
                addToTop(new DrawCardAction(player, 1));
            }
            addToTop(new ReducePowerAction(player, player, HarmonicSoulPower.POWER_ID, amount));
        }
        isDone = true;
    }

    /** Remove the opposing marker without mutating a list during iteration. */
    private void removeMuzixiMarker() {
        if (!player.hasPower(MuzixiPersonaPower.POWER_ID) || player.powers == null) {
            return;
        }
        for (AbstractPower power : new ArrayList<>(player.powers)) {
            if (power != null && MuzixiPersonaPower.POWER_ID.equals(power.ID)
                    && player.powers.contains(power)) {
                power.onRemove();
                player.powers.remove(power);
                if (AbstractDungeon.player != null && AbstractDungeon.getCurrMapNode() != null) {
                    AbstractDungeon.onModifyPower();
                }
            }
        }
    }
}
