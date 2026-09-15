package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class EvergreenPower extends MuzixiPower implements VitalityGainListener {
    public static final String POWER_ID = "NineSwordTechniques:Evergreen";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public EvergreenPower(AbstractCreature owner, int amount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        loadIcons("Evergreen");
        updateDescription();
    }

    @Override
    public void onVitalityGained(int gained) {
        if (gained > 0) {
            flash();
            addToTop(new GainBlockAction(owner, owner, safeMultiply(amount, gained)));
        }
    }

    private static int safeMultiply(int left, int right) {
        long result = (long) left * right;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }
}
