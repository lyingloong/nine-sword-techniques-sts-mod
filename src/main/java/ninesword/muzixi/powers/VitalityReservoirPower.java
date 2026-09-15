package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import ninesword.muzixi.actions.GainVitalityAction;

public class VitalityReservoirPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:VitalityReservoir";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int BASE_THRESHOLD = 4;
    private static final int UPGRADED_GAIN = 3;
    private static final int UPGRADED_THRESHOLD = 5;
    private int baseGain;
    private int upgradedGain;

    public VitalityReservoirPower(AbstractCreature owner, int amount, int threshold) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        int gain = Math.max(0, amount);
        if (threshold >= UPGRADED_THRESHOLD) {
            upgradedGain = gain;
        } else {
            baseGain = gain;
        }
        syncAmount();
        type = PowerType.BUFF;
        loadIcons("VitalityReservoir");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        int vitality = VitalityPower.getAmount(owner);
        if (vitality <= BASE_THRESHOLD) {
            queueGain(baseGain);
            queueGain(upgradedGain);
        } else if (vitality <= UPGRADED_THRESHOLD) {
            queueGain(upgradedGain);
        }
    }

    private void queueGain(int gain) {
        if (gain > 0) {
            flash();
            addToBot(new GainVitalityAction(owner, gain));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        if (stackAmount >= UPGRADED_GAIN) {
            upgradedGain = safeAdd(upgradedGain, stackAmount);
        } else if (stackAmount > 0) {
            baseGain = safeAdd(baseGain, stackAmount);
        }
        syncAmount();
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (baseGain > 0 && upgradedGain > 0) {
            description = String.format(STRINGS.DESCRIPTIONS[2],
                    safeAdd(baseGain, upgradedGain), upgradedGain);
        } else if (upgradedGain > 0) {
            description = String.format(STRINGS.DESCRIPTIONS[1], upgradedGain);
        } else {
            description = String.format(STRINGS.DESCRIPTIONS[0], baseGain);
        }
    }

    private void syncAmount() {
        amount = safeAdd(baseGain, upgradedGain);
    }

    private static int safeAdd(int left, int right) {
        long result = (long) left + right;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }
}
