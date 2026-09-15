package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/** Once per turn, grants Block when a single gain of Vitality is large enough. */
public class ThickenedLifebloodPower extends MuzixiPower implements VitalityGainListener {
    public static final String POWER_ID = "NineSwordTechniques:ThickenedLifeblood";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int THRESHOLD = 3;
    private boolean triggered;

    public ThickenedLifebloodPower(AbstractCreature owner, int block) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, block);
        type = PowerType.BUFF;
        loadIcons("ThickenedLifeblood");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        triggered = false;
    }

    @Override
    public void onVitalityGained(int gained) {
        if (!triggered && gained >= THRESHOLD) {
            triggered = true;
            flash();
            addToTop(new GainBlockAction(owner, owner, amount));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], THRESHOLD, amount);
    }
}
