package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;

/** Repeats the first Paralysis application each player turn once per stack. */
public class NumbingEchoPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:NumbingEcho";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean triggered;

    public NumbingEchoPower(AbstractCreature owner) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = 1;
        type = PowerType.BUFF;
        loadIcons("NumbingEcho");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        triggered = false;
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (!(target instanceof AbstractMonster)
                || power == null || !ParalysisPower.POWER_ID.equals(power.ID)
                || power.amount <= 0 || source != owner
                || AbstractDungeon.actionManager == null
                || AbstractDungeon.actionManager.turnHasEnded
                // ApplyPowerAction notifies this hook before Artifact blocks a
                // debuff.  Such a target is not a valid first application;
                // leave the once-per-turn trigger available for the next
                // target in a group effect.
                || target.hasPower(ArtifactPower.POWER_ID)) {
            return;
        }
        // Mark the trigger before queueing the echoed application.  The
        // echoed ApplyPowerAction calls this hook too, but sees this flag and
        // returns immediately, so it cannot recurse.  Marking it up front
        // also makes a group application deterministic: only the first valid
        // target gets an echo, even while the echoed action is still queued.
        if (triggered) {
            return;
        }
        triggered = true;
        flash();
        int echoedAmount = Math.max(0, power.amount);
        for (int i = 0; i < amount; i++) {
            addToTop(new ApplyPowerAction(target, owner,
                    new ParalysisPower(target, echoedAmount), echoedAmount));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }
}
