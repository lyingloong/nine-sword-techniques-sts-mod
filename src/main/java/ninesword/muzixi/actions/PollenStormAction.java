package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.ParalysisPower;

public class PollenStormAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int amount;

    public PollenStormAction(AbstractPlayer player, int amount) {
        this.player = player;
        this.amount = Math.max(0, amount);
        actionType = ActionType.DEBUFF;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getMonsters() != null) {
            for (int i = AbstractDungeon.getMonsters().monsters.size() - 1; i >= 0; i--) {
                AbstractMonster target = AbstractDungeon.getMonsters().monsters.get(i);
                if (target != null && !target.isDeadOrEscaped() && amount > 0) {
                    addToTop(new ApplyPowerAction(target, player,
                            new ParalysisPower(target, amount), amount));
                }
            }
        }
        isDone = true;
    }
}
