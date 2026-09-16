package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import ninesword.muzixi.actions.TriggerDualityConvergenceAction;

public class MuzixiPersonaPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:MuzixiPersona";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public MuzixiPersonaPower(AbstractCreature owner) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        amount = -1;
        type = PowerType.BUFF;
        loadIcons("MuzixiPersona");
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        // A persona is a state marker, not a stackable numeric effect.
        removeOpposingPersona();
    }

    @Override
    public void onInitialApplication() {
        playPersonaEntryVisuals(false);
        boolean actualSwitch = owner != null && owner.isPlayer
                && owner.hasPower(TearPersonaPower.POWER_ID);
        if (actualSwitch) {
            addToTop(new TriggerDualityConvergenceAction(
                    (com.megacrit.cardcrawl.characters.AbstractPlayer) owner));
        }
        removeOpposingPersona();
    }

    @Override
    public void updateParticles() {
        updatePersonaVisuals(false);
    }

    private void removeOpposingPersona() {
        if (owner != null && owner.hasPower(TearPersonaPower.POWER_ID)) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, TearPersonaPower.POWER_ID));
        }
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new ApplyPowerAction(owner, owner,
                new HarmonicSoulPower(owner, 1), 1));
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0];
    }
}
