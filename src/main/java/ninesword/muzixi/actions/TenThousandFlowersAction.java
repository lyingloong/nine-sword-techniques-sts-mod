package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/** Counts every enemy BUFF/DEBUFF power once for Ten Thousand Flowers. */
public class TenThousandFlowersAction extends AbstractGameAction {
    private final AbstractCreature source;

    public TenThousandFlowersAction(AbstractCreature source) {
        this.source = source;
        actionType = ActionType.POWER;
    }

    @Override
    public void update() {
        int total = 0;
        if (AbstractDungeon.getMonsters() != null) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (monster == null || monster.isDeadOrEscaped() || monster.powers == null) {
                    continue;
                }
                for (AbstractPower power : monster.powers) {
                    if (power.type != AbstractPower.PowerType.BUFF
                            && power.type != AbstractPower.PowerType.DEBUFF) {
                        continue;
                    }
                    if (total < Integer.MAX_VALUE) {
                        total++;
                    }
                }
            }
        }

        if (total > 0) {
            addToTop(new GainVitalityAction(source, total));
        }
        isDone = true;
    }
}
