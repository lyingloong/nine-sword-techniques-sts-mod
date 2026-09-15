package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
        loadIcons("Paralysis");
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
        // Absolute Paralysis is a player-side power that changes the normal
        // end-of-turn expiry.  Keep half of the stacks (rounding up the
        // amount that expires) and restore only the reductions represented by
        // the stacks that actually left.  Looking at the player here is
        // intentional: Paralysis is a debuff on the enemy, while the card
        // which grants Absolute Paralysis is a buff on its source.
        int removed = amount;
        if (!owner.isPlayer && AbstractDungeon.player != null
                && AbstractDungeon.player.hasPower(AbsoluteParalysisPower.POWER_ID)) {
            // Avoid integer overflow for malformed/imported powers while
            // retaining the documented ceiling division.
            removed = (amount / 2) + (amount % 2);
        }
        removeStacks(removed);
        // removeStacks queues the structural removal when all stacks expired.
        // Do not mutate the power list directly during the callback iteration.
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
        int appliedStrength = lowerStat(true, requested);
        strengthReduction = safeAdd(strengthReduction, appliedStrength);
        int appliedDexterity = lowerStat(false, requested);
        dexterityReduction = safeAdd(dexterityReduction, appliedDexterity);
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
        AbstractPower reference = strength ? strengthReference : dexterityReference;
        if (reference != null && stat != reference) {
            // A zero-valued standard stat removes itself after an exact
            // cancellation. Its pending restoration still belongs to
            // Paralysis and can be transferred to the replacement object.
            // Any nonzero detached object was explicitly purged, so its old
            // reduction no longer exists and must leave the ledger too.
            if (reference.amount != 0) {
                if (strength) {
                    strengthReduction = 0;
                } else {
                    dexterityReduction = 0;
                }
            }
            if (strength) {
                strengthReference = null;
                createdStrengthPower = false;
            } else {
                dexterityReference = null;
                createdDexterityPower = false;
            }
        }
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
        if (strength && strengthReference == null) {
            strengthReference = stat;
            createdStrengthPower = created;
        } else if (!strength && dexterityReference == null) {
            dexterityReference = stat;
            createdDexterityPower = created;
        }
        return Math.max(0, applied);
    }

    /** Restore a stat without losing buffs that replaced the original Power. */
    private void restoreStat(boolean strength, int requested) {
        if (requested <= 0 || owner == null || owner.powers == null) {
            return;
        }
        AbstractPower reference = strength ? strengthReference : dexterityReference;
        if (reference == null) {
            return;
        }
        if (owner.getPower(reference.ID) != reference) {
            // StrengthPower/DexterityPower remove themselves when a later
            // stack lands exactly on zero. In that case the positive stack
            // was only canceling Paralysis and must reappear when Paralysis
            // ends. A nonzero detached reference, however, was explicitly
            // removed by an effect such as Ten Thousand Flowers; restoring it
            // would leave a new stat Power behind after that purge.
            if (reference.amount == 0) {
                // Apply it on the action queue because restoreStat can run
                // while the owner's Power list is being iterated.
                AbstractPower restoration = strength
                        ? new StrengthPower(owner, requested)
                        : new DexterityPower(owner, requested);
                addToTop(new ApplyPowerAction(owner, owner, restoration, requested));
            }
            return;
        }
        reference.amount = clamp((long) reference.amount + requested);
        reference.updateDescription();
        AbstractDungeon.onModifyPower();
        boolean created = strength ? createdStrengthPower : createdDexterityPower;
        if (created && reference.amount == 0) {
            // Another queued effect may modify this Power before cleanup.
            // Recheck both identity and amount at execution time so a delayed
            // removal never deletes a newly gained positive stat.
            addToTop(new RemoveZeroStatPowerAction(owner, reference));
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
        return value >= Integer.MAX_VALUE ? Integer.MAX_VALUE
                : value <= Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) value;
    }

    private static final class RemoveZeroStatPowerAction extends AbstractGameAction {
        private final AbstractCreature target;
        private final AbstractPower power;

        private RemoveZeroStatPowerAction(AbstractCreature target, AbstractPower power) {
            this.target = target;
            this.power = power;
            actionType = ActionType.POWER;
        }

        @Override
        public void update() {
            if (target != null && target.powers != null && power != null
                    && power.amount == 0 && target.getPower(power.ID) == power) {
                power.onRemove();
                target.powers.remove(power);
                if (AbstractDungeon.getCurrMapNode() != null) {
                    AbstractDungeon.onModifyPower();
                }
            }
            isDone = true;
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }
}
