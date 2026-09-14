package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;

/** Takes a pre-removal snapshot of every enemy BUFF/DEBUFF for Ten Thousand Flowers. */
public class TenThousandFlowersAction extends AbstractGameAction {
    private final AbstractCreature source;

    public TenThousandFlowersAction(AbstractCreature source) {
        this.source = source;
        actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        int total = 0;
        List<PowerRef> powersToRemove = new ArrayList<PowerRef>();
        if (AbstractDungeon.getMonsters() != null) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (monster == null || monster.isDeadOrEscaped() || monster.powers == null) {
                    continue;
                }
                for (AbstractPower power : new ArrayList<AbstractPower>(monster.powers)) {
                    if (power.type != AbstractPower.PowerType.BUFF
                            && power.type != AbstractPower.PowerType.DEBUFF) {
                        continue;
                    }
                    int stacks = power.amount == 0 ? 1 : safeAbs(power.amount);
                    total = safeAdd(total, stacks);
                    powersToRemove.add(new PowerRef(monster, power));
                }
            }
        }

        // Remove in the same action sequence after the snapshot has been made.
        for (PowerRef ref : powersToRemove) {
            addToBot(new RemoveSpecificPowerAction(ref.monster, source, ref.power.ID));
        }
        if (total > 0) {
            addToBot(new GainVitalityAction(source, total));
        }
        isDone = true;
    }

    private static int safeAbs(int value) {
        return value == Integer.MIN_VALUE ? Integer.MAX_VALUE : Math.abs(value);
    }

    private static int safeAdd(int a, int b) {
        long result = (long) a + b;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }

    private static final class PowerRef {
        private final AbstractMonster monster;
        private final AbstractPower power;

        private PowerRef(AbstractMonster monster, AbstractPower power) {
            this.monster = monster;
            this.power = power;
        }
    }
}
