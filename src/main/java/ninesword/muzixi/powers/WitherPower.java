package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

/** The combat-only negative counterpart to Vitality. */
public class WitherPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:Wither";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Set<AbstractCreature> PENDING_COMBAT_CLEANUP =
            Collections.newSetFromMap(new IdentityHashMap<AbstractCreature, Boolean>());

    public WitherPower(AbstractCreature owner, int amount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, amount);
        type = PowerType.DEBUFF;
        canGoNegative = false;
        loadIcons("Vitality");
        updateDescription();
    }

    public static int getAmount(AbstractCreature creature) {
        if (creature == null) {
            return 0;
        }
        AbstractPower power = creature.getPower(POWER_ID);
        return power == null ? 0 : Math.max(0, power.amount);
    }

    /** Add Wither while consuming any existing Vitality first. */
    public static void add(AbstractCreature creature, int requested) {
        if (creature == null || requested <= 0) {
            return;
        }
        WitherPower power = getPower(creature);
        if (power == null) {
            power = new WitherPower(creature, 0);
            creature.addPower(power);
        }
        power.addStacks(requested);
    }

    private static WitherPower getPower(AbstractCreature creature) {
        if (creature == null) {
            return null;
        }
        AbstractPower power = creature.getPower(POWER_ID);
        return power instanceof WitherPower ? (WitherPower) power : null;
    }

    private void addStacks(int requested) {
        int vitality = VitalityPower.getAmount(owner);
        int canceled = Math.min(vitality, requested);
        if (canceled > 0) {
            VitalityPower.removeForWither(owner, canceled);
        }
        int excess = requested - canceled;
        if (excess <= 0) {
            return;
        }

        int applied = VitalityMaxHealthPower.addNegative(owner, excess);
        amount += applied;
        updateDescription();
    }

    /** Remove Wither and restore its matching negative temporary max health. */
    public int removeStacks(int requested) {
        if (requested <= 0 || amount <= 0) {
            return 0;
        }
        int removed = Math.min(requested, amount);
        amount -= removed;
        VitalityMaxHealthPower.restoreNegative(owner, removed);
        updateDescription();
        if (amount <= 0) {
            amount = 0;
            if (owner != null && owner.powers != null && owner.powers.contains(this)) {
                addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            }
        }
        return removed;
    }

    @Override
    public void onInitialApplication() {
        int initial = amount;
        amount = 0;
        addStacks(initial);
    }

    @Override
    public void stackPower(int stackAmount) {
        addStacks(stackAmount);
    }

    @Override
    public void reducePower(int reduceAmount) {
        removeStacks(reduceAmount);
    }

    @Override
    public void onRemove() {
        // Manual removal during combat also clears the linked negative
        // modifier. Combat-end cleanup zeroes this Power first, leaving the
        // independent temporary-max-health Power to restore its own ledger.
        if (amount > 0) {
            VitalityMaxHealthPower.restoreNegative(owner, amount);
            amount = 0;
            updateDescription();
        }
    }

    @Override
    public void onVictory() {
        PENDING_COMBAT_CLEANUP.add(owner);
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }

    public static void removeExpired(AbstractCreature creature) {
        if (creature == null || creature.powers == null || !PENDING_COMBAT_CLEANUP.remove(creature)) {
            return;
        }
        Iterator<AbstractPower> iterator = creature.powers.iterator();
        while (iterator.hasNext()) {
            AbstractPower power = iterator.next();
            if (POWER_ID.equals(power.ID)) {
                // Do not let Wither restore the temporary max-health ledger at
                // combat end; VitalityMaxHealthPower owns that cleanup.
                power.amount = 0;
                power.updateDescription();
                power.onRemove();
                iterator.remove();
            }
        }
    }
}
