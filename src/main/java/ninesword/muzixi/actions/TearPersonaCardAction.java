package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Arrays;

/** Resolves Leixier persona's cost and HP loss after a card is played. */
public class TearPersonaCardAction extends AbstractGameAction {
    private static final int MAX_HEALTH_LOSS = 2;
    private static final int ENEMY_HP_LOSS = 2;

    private final AbstractPlayer player;

    public TearPersonaCardAction(AbstractPlayer player) {
        this.player = player;
        actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (player != null) {
            player.decreaseMaxHealth(MAX_HEALTH_LOSS);
            if (AbstractDungeon.getMonsters() != null) {
                addToTop(new DamageAllEnemiesAction(player,
                        fixedDamageMatrix(ENEMY_HP_LOSS),
                        DamageInfo.DamageType.HP_LOSS,
                        AttackEffect.NONE));
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
