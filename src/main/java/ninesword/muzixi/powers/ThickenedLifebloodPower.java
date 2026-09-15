package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/** Restores health whenever the owner gains resolved Vitality. */
public class ThickenedLifebloodPower extends MuzixiPower implements VitalityGainListener {
    public static final String POWER_ID = "NineSwordTechniques:ThickenedLifeblood";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    /**
     * @param healPerVitality health restored for each point that remains after
     *                        Wither cancellation (normally one)
     */
    public ThickenedLifebloodPower(AbstractCreature owner, int healPerVitality) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, healPerVitality);
        type = PowerType.BUFF;
        loadIcons("ThickenedLifeblood");
        updateDescription();
    }

    @Override
    public void onVitalityGained(int gained) {
        if (gained > 0 && amount > 0) {
            flash();
            addToTop(new HealAction(owner, owner, safeMultiply(gained, amount)));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }

    private static int safeMultiply(int left, int right) {
        long result = (long) left * right;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }
}
