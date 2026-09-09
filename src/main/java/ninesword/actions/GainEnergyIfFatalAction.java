package ninesword.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class GainEnergyIfFatalAction extends AbstractGameAction {
    private final AbstractMonster monster;

    public GainEnergyIfFatalAction(AbstractMonster monster, int amount) {
        this.monster = monster;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (monster != null && !monster.escaped && !monster.halfDead
                && (monster.isDying || monster.currentHealth <= 0)) {
            addToTop(new GainEnergyAction(amount));
        }
        isDone = true;
    }
}
