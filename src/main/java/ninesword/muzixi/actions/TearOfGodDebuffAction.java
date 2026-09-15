package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

/** Applies Tear of God's debuffs from each target's actual damage taken. */
public class TearOfGodDebuffAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public TearOfGodDebuffAction(AbstractPlayer player) {
        this.player = player;
        actionType = ActionType.DEBUFF;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getMonsters() != null) {
            for (int i = AbstractDungeon.getMonsters().monsters.size() - 1; i >= 0; i--) {
                AbstractMonster monster = AbstractDungeon.getMonsters().monsters.get(i);
                int damageTaken = monster == null ? 0 : Math.max(0, monster.lastDamageTaken);
                if (damageTaken > 0 && !monster.isDeadOrEscaped()) {
                    addToTop(new ApplyPowerAction(monster, player,
                            new VulnerablePower(monster, damageTaken, false), damageTaken));
                    addToTop(new ApplyPowerAction(monster, player,
                            new WeakPower(monster, damageTaken, false), damageTaken));
                }
            }
        }
        isDone = true;
    }
}
