package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import ninesword.muzixi.actions.GainVitalityAction;

/** Stores the amount gained by Vitality Tide and pays it out next turn. */
public class VitalityTidePower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:VitalityTide";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public VitalityTidePower(AbstractCreature owner, int amount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, amount);
        type = PowerType.BUFF;
        loadIcons("VitalityTide");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (amount > 0) {
            flash();
            addToBot(new GainVitalityAction(owner, amount));
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }
}
