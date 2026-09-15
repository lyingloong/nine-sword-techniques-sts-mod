package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Owns all combat-only maximum-health changes made by the Vitality system.
 * Positive and negative changes are tracked independently so combat cleanup
 * never depends on whether Vitality or Wither still exists.
 */
public class VitalityMaxHealthPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:VitalityMaxHealth";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Set<AbstractCreature> PENDING_COMBAT_CLEANUP =
            Collections.newSetFromMap(new IdentityHashMap<AbstractCreature, Boolean>());

    private int positiveAmount;
    private int negativeAmount;
    private int initialAmount;

    public VitalityMaxHealthPower(AbstractCreature owner, int amount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        initialAmount = amount;
        this.amount = 0;
        type = PowerType.BUFF;
        canGoNegative = true;
        loadIcons("VitalityMaxHealth");
        updateDescription();
    }

    /** Returns the net temporary maximum-health change. */
    public static int getAmount(AbstractCreature creature) {
        VitalityMaxHealthPower power = getPower(creature);
        return power == null ? 0 : power.amount;
    }

    /** Adds combat-only maximum health from an actual Vitality gain. */
    public static void add(AbstractCreature creature, int requested) {
        if (creature == null || requested <= 0) {
            return;
        }
        VitalityMaxHealthPower power = ensurePower(creature);
        power.positiveAmount += requested;
        creature.maxHealth += requested;
        power.syncAmount();
        creature.healthBarUpdatedEvent();
    }

    /**
     * Applies a combat-only negative maximum-health modifier for Wither.
     * Maximum health must remain at least one, so only the amount that can be
     * applied is recorded for later restoration.
     */
    public static int addNegative(AbstractCreature creature, int requested) {
        if (creature == null || requested <= 0) {
            return 0;
        }
        int applied = Math.min(requested, Math.max(0, creature.maxHealth - 1));
        if (applied <= 0) {
            return 0;
        }
        VitalityMaxHealthPower power = ensurePower(creature);
        power.negativeAmount += applied;
        creature.maxHealth -= applied;
        creature.currentHealth = Math.min(creature.currentHealth, creature.maxHealth);
        power.syncAmount();
        creature.healthBarUpdatedEvent();
        return applied;
    }

    private static VitalityMaxHealthPower ensurePower(AbstractCreature creature) {
        VitalityMaxHealthPower power = getPower(creature);
        if (power == null) {
            power = new VitalityMaxHealthPower(creature, 0);
            creature.addPower(power);
        }
        return power;
    }

    private static VitalityMaxHealthPower getPower(AbstractCreature creature) {
        if (creature == null) {
            return null;
        }
        AbstractPower power = creature.getPower(POWER_ID);
        return power instanceof VitalityMaxHealthPower ? (VitalityMaxHealthPower) power : null;
    }

    private void syncAmount() {
        amount = positiveAmount - negativeAmount;
        updateDescription();
    }

    private void restoreAll() {
        if (positiveAmount == 0 && negativeAmount == 0) {
            return;
        }
        // Undo the exact deltas recorded by this Power without silently
        // changing current HP.
        owner.maxHealth = Math.max(1, owner.maxHealth - positiveAmount + negativeAmount);
        owner.currentHealth = Math.min(owner.currentHealth, owner.maxHealth);
        positiveAmount = 0;
        negativeAmount = 0;
        syncAmount();
        owner.healthBarUpdatedEvent();
    }

    @Override
    public void onInitialApplication() {
        int initial = initialAmount;
        initialAmount = 0;
        if (initial > 0) {
            add(owner, initial);
        } else if (initial < 0) {
            addNegative(owner, -initial);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        if (stackAmount > 0) {
            add(owner, stackAmount);
        } else if (stackAmount < 0) {
            addNegative(owner, -stackAmount);
        }
    }

    @Override
    public void onRemove() {
        restoreAll();
    }

    @Override
    public void onVictory() {
        PENDING_COMBAT_CLEANUP.add(owner);
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }

    /**
     * Victory callbacks run while the creature's Power list is being iterated.
     * Defer removal until the post-battle hook, keeping cleanup owned here.
     */
    public static void removeExpired(AbstractCreature creature) {
        if (creature == null || creature.powers == null || !PENDING_COMBAT_CLEANUP.remove(creature)) {
            return;
        }
        Iterator<AbstractPower> iterator = creature.powers.iterator();
        while (iterator.hasNext()) {
            AbstractPower power = iterator.next();
            if (POWER_ID.equals(power.ID)) {
                power.onRemove();
                iterator.remove();
            }
        }
    }
}
