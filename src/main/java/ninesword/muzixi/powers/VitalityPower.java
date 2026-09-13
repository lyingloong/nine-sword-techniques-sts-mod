package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

public class VitalityPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:Vitality";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public VitalityPower(AbstractCreature owner, int amount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        loadIcons("Vitality");
        updateDescription();
    }

    public static int getAmount(AbstractCreature creature) {
        if (creature == null || !creature.hasPower(POWER_ID)) {
            return 0;
        }
        return creature.getPower(POWER_ID).amount;
    }

    /** Remove the zeroed combat resource after the victory callback finishes iterating powers. */
    public static void removeExpired(AbstractCreature creature) {
        if (creature == null || creature.powers == null) {
            return;
        }
        Iterator<AbstractPower> iterator = creature.powers.iterator();
        while (iterator.hasNext()) {
            AbstractPower power = iterator.next();
            if (POWER_ID.equals(power.ID) && power.amount <= 0) {
                power.onRemove();
                iterator.remove();
            }
        }
    }

    /**
     * Vitality is a combat-only resource.  The base game calls this callback
     * before it clears the post-combat action queue, so resetting the amount
     * directly is safer than enqueueing a RemoveSpecificPowerAction here.
     */
    @Override
    public void onVictory() {
        amount = 0;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }
}
