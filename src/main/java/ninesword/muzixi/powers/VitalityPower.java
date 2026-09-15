package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

public class VitalityPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:Vitality";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Set<AbstractCreature> PENDING_COMBAT_CLEANUP =
            Collections.newSetFromMap(new IdentityHashMap<AbstractCreature, Boolean>());

    public VitalityPower(AbstractCreature owner, int amount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, amount);
        type = PowerType.BUFF;
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

    /**
     * Gain vitality while respecting the mutual exclusion with Wither. A gain
     * first removes Wither stacks without changing the independent temporary
     * max-health ledger; only the remaining amount becomes Vitality.
     */
    public static int gain(AbstractCreature creature, int requested) {
        if (creature == null || requested <= 0) {
            return 0;
        }

        WitherPower wither = getWither(creature);
        int canceled = wither == null ? 0 : wither.removeStacks(requested);
        int gained = requested - canceled;
        if (gained <= 0) {
            return 0;
        }

        VitalityPower vitality = getVitality(creature);
        if (vitality == null) {
            vitality = new VitalityPower(creature, 0);
            creature.addPower(vitality);
        }
        vitality.amount += gained;
        vitality.updateDescription();
        VitalityMaxHealthPower.add(creature, gained);
        AbstractDungeon.onModifyPower();
        notifyVitalityGained(creature, gained);
        return gained;
    }

    /** Spend only the Vitality that currently exists. This never creates Wither. */
    public static int spend(AbstractCreature creature, int requested) {
        if (creature == null || requested <= 0) {
            return 0;
        }
        VitalityPower vitality = getVitality(creature);
        if (vitality == null || vitality.amount <= 0) {
            return 0;
        }
        int spent = Math.min(requested, vitality.amount);
        vitality.amount -= spent;
        vitality.updateDescription();
        // Internal resource payments do not go through ReducePowerAction,
        // therefore they must notify the power UI themselves.  Without this
        // callback the icon/tooltip can remain stale until another power
        // mutation happens (especially visible when a card spends all
        // Vitality to cancel a Wither application).
        AbstractDungeon.onModifyPower();
        // Do not remove the Power directly here.  spend() can be reached from
        // WitherPower.stackPower(), while ApplyPowerAction is iterating the
        // owner's Power list; structural removal at that point can throw a
        // ConcurrentModificationException.  A zero amount is harmless and is
        // removed by the deferred combat cleanup below.
        return spent;
    }

    /** Lose Vitality; any excess loss becomes Wither. */
    public static int lose(AbstractCreature creature, int requested) {
        if (creature == null || requested <= 0) {
            return 0;
        }
        int spent = spend(creature, requested);
        int excess = requested - spent;
        if (excess > 0) {
            WitherPower.add(creature, excess);
        }
        return spent;
    }

    /** Remove Vitality while it is being converted into Wither. */
    static int removeForWither(AbstractCreature creature, int requested) {
        return spend(creature, requested);
    }

    private static VitalityPower getVitality(AbstractCreature creature) {
        AbstractPower power = creature.getPower(POWER_ID);
        return power instanceof VitalityPower ? (VitalityPower) power : null;
    }

    private static WitherPower getWither(AbstractCreature creature) {
        AbstractPower power = creature.getPower(WitherPower.POWER_ID);
        return power instanceof WitherPower ? (WitherPower) power : null;
    }

    /** Notify only powers that explicitly subscribe to resolved Vitality gains. */
    private static void notifyVitalityGained(AbstractCreature creature, int gained) {
        if (creature == null || gained <= 0) {
            return;
        }
        for (AbstractPower power : new ArrayList<>(creature.powers)) {
            if (power instanceof VitalityGainListener) {
                ((VitalityGainListener) power).onVitalityGained(gained);
            }
        }
    }

    private static void cleanupAfterCombat(AbstractCreature creature) {
        Iterator<AbstractPower> iterator = creature.powers.iterator();
        while (iterator.hasNext()) {
            AbstractPower power = iterator.next();
            if (POWER_ID.equals(power.ID)) {
                power.onRemove();
                iterator.remove();
            }
        }
    }

    /** Remove a zero marker after callbacks, or the combat resource after victory. */
    public static void removeExpired(AbstractCreature creature) {
        if (creature == null || creature.powers == null) {
            return;
        }
        if (PENDING_COMBAT_CLEANUP.remove(creature)) {
            cleanupAfterCombat(creature);
            return;
        }
        VitalityPower vitality = getVitality(creature);
        if (vitality != null && vitality.amount <= 0) {
            // spend() can run while ApplyPowerAction is iterating this list.
            // PostUpdate reaches here after that callback has returned, so a
            // zero resource marker can now be removed without invalidating an
            // iterator. A later gain simply creates a fresh marker.
            vitality.onRemove();
            creature.powers.remove(vitality);
            if (AbstractDungeon.getCurrMapNode() != null) {
                AbstractDungeon.onModifyPower();
            }
        }
    }

    /**
     * Vitality is a combat-only resource.  The base game calls this callback
     * before it clears the post-combat action queue, so resetting the amount
     * directly is safer than enqueueing a RemoveSpecificPowerAction here.
     */
    @Override
    public void onInitialApplication() {
        int initial = amount;
        amount = 0;
        if (initial > 0) {
            gain(owner, initial);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        gain(owner, stackAmount);
    }

    @Override
    public void reducePower(int reduceAmount) {
        // ReducePowerAction handles the exact-removal branch itself. Internal
        // cards use the atomic Vitality spend/payment actions, while this
        // override keeps partial reductions from driving the resource below zero.
        spend(owner, reduceAmount);
    }

    @Override
    public void onVictory() {
        amount = 0;
        updateDescription();
        PENDING_COMBAT_CLEANUP.add(owner);
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }
}
