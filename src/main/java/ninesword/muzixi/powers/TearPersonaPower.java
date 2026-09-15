package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.localization.PowerStrings;
import ninesword.muzixi.actions.EnterTearPersonaAction;
import ninesword.muzixi.actions.TriggerDualityConvergenceAction;

public class TearPersonaPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:TearPersona";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public TearPersonaPower(AbstractCreature owner) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        amount = -1;
        type = PowerType.BUFF;
        loadIcons("TearPersona");
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        // A persona is a state marker, not a stackable numeric effect.
        removeOpposingPersona();
    }

    @Override
    public void onInitialApplication() {
        playPersonaEntryVisuals(true);
        boolean actualSwitch = owner != null && owner.isPlayer
                && owner.hasPower(MuzixiPersonaPower.POWER_ID);
        if (actualSwitch) {
            addToTop(new TriggerDualityConvergenceAction(
                    (com.megacrit.cardcrawl.characters.AbstractPlayer) owner));
        }
        if (owner != null && owner.isPlayer) {
            /*
             * ApplyPowerAction invokes this callback while the ApplyPowerAction
             * itself is the current action.  Entry conversion must happen
             * before actions that were already queued after the persona
             * application (for example DualityConvergence).  addToTop is the
             * same ordering used by the base game's stance-entry effects and
             * also prevents a card's following effect from observing stale
             * Harmonic Soul.
             */
            addToTop(new EnterTearPersonaAction(
                    (com.megacrit.cardcrawl.characters.AbstractPlayer) owner));
        }
        // Added last so it executes first, before entry conversion and the
        // switch payoff queued above.
        removeOpposingPersona();
    }

    @Override
    public void updateParticles() {
        updatePersonaVisuals(true);
    }

    /**
     * Direct-entry actions use {@code AbstractCreature.addPower} and therefore
     * do not receive {@link #onInitialApplication()}. Keep their transition
     * feedback identical to an ordinary power application.
     */
    public void playEntryVisualsForDirectApplication() {
        playPersonaEntryVisuals(true);
    }

    private void removeOpposingPersona() {
        if (owner != null && owner.hasPower(MuzixiPersonaPower.POWER_ID)) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, MuzixiPersonaPower.POWER_ID));
        }
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0];
    }
}
