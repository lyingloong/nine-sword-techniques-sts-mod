package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Arrays;

/** Resolves the HP-loss portion of Eye of Death after persona entry. */
public class EyeOfDeathAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int loss;

    public EyeOfDeathAction(AbstractPlayer player, int loss) {
        this.player = player;
        this.loss = Math.max(0, loss);
        actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (player != null) {
            if (loss > 0) {
                if (AbstractDungeon.getMonsters() != null) {
                    addToTop(new DamageAllEnemiesAction(player, fixedDamageMatrix(loss),
                            DamageInfo.DamageType.HP_LOSS, AttackEffect.FIRE));
                }
                addToTop(new LoseHPAction(player, player, loss));
            }
        }
        isDone = true;
    }

    private static int[] fixedDamageMatrix(int damage) {
        int[] matrix = new int[AbstractDungeon.getMonsters().monsters.size()];
        Arrays.fill(matrix, Math.max(0, damage));
        return matrix;
    }
}
