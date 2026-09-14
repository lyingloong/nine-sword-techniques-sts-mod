package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * A turn-bound debuff used by Muzixi's paralysis cards.
 *
 * <p>Paralysis is deliberately not implemented by applying ordinary Weak or
 * Frail.  Every stack is a paired, temporary reduction to Strength and
 * Dexterity.  The references and reduction amounts below form a small ledger:
 * when another effect changes either stat while paralysis is active, cleanup
 * only gives back the amount this power actually took away.</p>
 */
public class ParalysisPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:Paralysis";
    private static final int MAX_STAT = 999;
    private static final int MIN_STAT = -999;
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Set<AbstractCreature> PENDING_COMBAT_CLEANUP =
            Collections.newSetFromMap(new IdentityHashMap<AbstractCreature, Boolean>());

    /** Number of Strength points removed by this instance. */
    private int strengthReduction;
    /** Number of Dexterity points removed by this instance. */
    private int dexterityReduction;
    /** The exact stat powers modified by this instance. */
    private AbstractPower strengthReference;
    private AbstractPower dexterityReference;
    /** Whether the corresponding stat power was created by this instance. */
    private boolean createdStrengthPower;
    private boolean createdDexterityPower;

    public ParalysisPower(AbstractCreature owner, int amount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, amount);
        type = PowerType.DEBUFF;
        canGoNegative = false;
        // There is intentionally no second, mandatory art asset for this
        // status yet.  Vitality is the shared Muzixi resource icon and keeps
        // the Power safe to load in both languages/build configurations.
        loadIcons("Vitality");
        updateDescription();
    }

    /** Return the current paralysis stacks on a creature. */
    public static int getAmount(AbstractCreature creature) {
        if (creature == null) {
            return 0;
        }
        AbstractPower power = creature.getPower(POWER_ID);
        return power instanceof ParalysisPower ? Math.max(0, power.amount) : 0;
    }

    /**
     * Remove up to {@code requested} stacks and restore only the reductions
     * still owned by this Power.  This is useful to cards such as Nerve
     * Rupture, and also keeps ReducePowerAction semantics exact.
     *
     * @return the number of stacks removed
     */
    public static int remove(AbstractCreature creature, int requested) {
        if (creature == null || requested <= 0) {
            return 0;
        }
        AbstractPower power = creature.getPower(POWER_ID);
        return power instanceof ParalysisPower
                ? ((ParalysisPower) power).removeStacks(requested)
                : 0;
    }

    @Override
    public void onInitialApplication() {
        int initial = amount;
        amount = 0;
        addStacks(initial);
    }

    @Override
    public void stackPower(int stackAmount) {
        if (stackAmount > 0) {
            addStacks(stackAmount);
        } else if (stackAmount < 0) {
            removeStacks(-stackAmount);
        }
    }

    @Override
    public void reducePower(int reduceAmount) {
        removeStacks(reduceAmount);
    }

    /**
     * Paralysis expires at the end of the affected creature's own turn.  The
     * game passes the owner's side as {@code isPlayer}; checking it explicitly
     * also makes this safe if a caller invokes the callback for both sides.
     */
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (owner == null || owner.isPlayer != isPlayer || amount <= 0) {
            return;
        }
        flash();
        cleanupReductions();
        amount = 0;
        updateDescription();
        // Defer list removal until the callback iteration has finished.  The
        // cleanup above is immediate, so no later end-of-turn effect sees a
        // stale Strength/Dexterity penalty.
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    /** Forced removal (including dispel effects) must never leave a stat debuff. */
    @Override
    public void onRemove() {
        cleanupReductions();
    }

    /** A dying target can bypass RemoveSpecificPowerAction's callback. */
    @Override
    public void onDeath() {
        cleanupReductions();
    }

    /** Mark the owner for deferred list cleanup after the victory iteration. */
    @Override
    public void onVictory() {
        cleanupReductions();
        amount = 0;
        updateDescription();
        if (owner != null) {
            PENDING_COMBAT_CLEANUP.add(owner);
        }
    }

    /**
     * Remove zeroed paralysis powers after AbstractCreature has finished
     * iterating powers during victory.  The mod calls this for the player;
     * enemy instances are discarded with their monsters, but this method is
     * public so a caller can clean any creature explicitly.
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

    /** Add stacks and apply the paired stat reductions. */
    private void addStacks(int requested) {
        if (owner == null || requested <= 0) {
            return;
        }
        amount = safeAdd(amount, requested);
        strengthReduction += lowerStat(true, requested);
        dexterityReduction += lowerStat(false, requested);
        updateDescription();
    }

    /** Remove stacks and restore exactly one point per removed stack. */
    private int removeStacks(int requested) {
        if (requested <= 0 || amount <= 0) {
            return 0;
        }
        int removed = Math.min(requested, amount);
        amount -= removed;
        restoreStat(true, Math.min(removed, strengthReduction));
        restoreStat(false, Math.min(removed, dexterityReduction));
        strengthReduction -= Math.min(removed, strengthReduction);
        dexterityReduction -= Math.min(removed, dexterityReduction);
        updateDescription();
        if (amount == 0 && owner != null && owner.powers != null
                && owner.powers.contains(this)) {
            // ReducePowerAction normally removes a power whose amount reaches
            // zero.  Queue the same removal for callers that invoke this
            // helper directly, without mutating the list during iteration.
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
        return removed;
    }

    /**
     * Lower Strength or Dexterity and remember the exact power object changed.
     * A missing stat power is created with the negative amount; it is removed
     * again if cleanup brings it back to zero.
     */
    private int lowerStat(boolean strength, int requested) {
        if (requested <= 0 || owner == null || owner.powers == null) {
            return 0;
        }
        String id = strength ? StrengthPower.POWER_ID : DexterityPower.POWER_ID;
        AbstractPower stat = owner.getPower(id);
        boolean created = false;
        if (stat == null) {
            // Add a zero-valued standard stat first, then apply the same
            // mutation path as an already-present stat.  Constructing it with
            // a negative amount here would count the reduction twice below.
            stat = strength
                    ? new StrengthPower(owner, 0)
                    : new DexterityPower(owner, 0);
            owner.addPower(stat);
            stat = owner.getPower(id);
            created = true;
        }
        if (stat == null) {
            return 0;
        }
        int before = stat.amount;
        int after = clamp((long) before - requested);
        stat.amount = after;
        stat.updateDescription();
        AbstractDungeon.onModifyPower();
        int applied = before - after;
        if (strength) {
            if (strengthReference == null) {
                strengthReference = stat;
                createdStrengthPower = created;
            }
        } else if (dexterityReference == null) {
            dexterityReference = stat;
            createdDexterityPower = created;
        }
        return Math.max(0, applied);
    }

    /** Restore a stat only when the original power is still present. */
    private void restoreStat(boolean strength, int requested) {
        if (requested <= 0 || owner == null || owner.powers == null) {
            return;
        }
        AbstractPower reference = strength ? strengthReference : dexterityReference;
        if (reference == null || owner.getPower(reference.ID) != reference) {
            return;
        }
        reference.amount = clamp((long) reference.amount + requested);
        reference.updateDescription();
        AbstractDungeon.onModifyPower();
        boolean created = strength ? createdStrengthPower : createdDexterityPower;
        if (created && reference.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, reference));
        }
    }

    /** Restore all outstanding reductions and forget the ledger. */
    private void cleanupReductions() {
        restoreStat(true, strengthReduction);
        restoreStat(false, dexterityReduction);
        strengthReduction = 0;
        dexterityReduction = 0;
        strengthReference = null;
        dexterityReference = null;
        createdStrengthPower = false;
        createdDexterityPower = false;
    }

    private static int safeAdd(int left, int right) {
        long result = (long) left + right;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE
                : result <= 0 ? 0 : (int) result;
    }

    private static int clamp(long value) {
        return (int) Math.max(MIN_STAT, Math.min(MAX_STAT, value));
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }
}
