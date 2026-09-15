package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/** Applies a fixed amount of Paralysis to its target at the start of each turn. */
public class NerveLockPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:NerveLock";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private AbstractCreature source;

    public NerveLockPower(AbstractCreature owner, int amount) {
        this(owner, owner, amount);
    }

    public NerveLockPower(AbstractCreature owner, AbstractCreature source, int amount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.source = source;
        this.amount = Math.max(0, amount);
        type = PowerType.DEBUFF;
        canGoNegative = false;
        // There is no separate Nerve Lock artwork yet.  The paralysis icon is
        // the same status this lock applies and is preferable to a missing
        // texture (which would crash power rendering in the compendium).
        loadIcons("Paralysis");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (owner == null || owner.isPlayer || amount <= 0) {
            return;
        }
        AbstractCreature actualSource = source == null ? owner : source;
        flash();
        addToBot(new ApplyPowerAction(owner, actualSource,
                new ParalysisPower(owner, amount), amount));
    }

    @Override
    public void stackPower(int stackAmount) {
        if (stackAmount > 0) {
            amount = safeAdd(amount, stackAmount);
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }

    private static int safeAdd(int left, int right) {
        long result = (long) left + right;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }
}
