package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import ninesword.muzixi.actions.GainVitalityAction;

/** Grants a fixed amount of Vitality at the beginning of every turn. */
public class VitalityReservoirPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:VitalityReservoir";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    /**
     * The third argument represented a threshold in an older design. Keep
     * this overload so old card classes remain source-compatible, but ignore
     * the obsolete threshold under the current fixed-gain rules.
     */
    public VitalityReservoirPower(AbstractCreature owner, int amount, int ignoredThreshold) {
        this(owner, amount);
    }

    public VitalityReservoirPower(AbstractCreature owner, int amount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, amount);
        type = PowerType.BUFF;
        loadIcons("VitalityReservoir");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (owner != null && amount > 0) {
            flash();
            addToBot(new GainVitalityAction(owner, amount));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        if (stackAmount > 0) {
            amount = safeAdd(amount, stackAmount);
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }

    private static int safeAdd(int left, int right) {
        long result = (long) left + right;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }
}
