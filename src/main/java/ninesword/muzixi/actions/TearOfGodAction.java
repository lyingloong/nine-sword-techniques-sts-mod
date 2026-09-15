package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import ninesword.muzixi.powers.MuzixiPersonaPower;
import ninesword.muzixi.powers.TearPersonaPower;
import ninesword.muzixi.powers.VitalityPower;

import java.util.Arrays;

/** Resolves 神之泪 using all Vitality present when the action starts. */
public class TearOfGodAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int damagePerVitality;

    public TearOfGodAction(AbstractPlayer player, int damagePerVitality) {
        this.player = player;
        this.damagePerVitality = Math.max(0, damagePerVitality);
        actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        int consumed = VitalityPower.getAmount(player);
        VitalityPower.spend(player, consumed);
        int damage = safeMultiply(consumed, damagePerVitality);
        boolean tear = player.hasPower(TearPersonaPower.POWER_ID);
        boolean muzixi = !tear && player.hasPower(MuzixiPersonaPower.POWER_ID);

        if (damage > 0 && AbstractDungeon.getMonsters() != null) {
            // Insert follow-ups first so LIFO execution remains Damage -> payoff.
            if (tear) {
                addToTop(new TearOfGodDebuffAction(player));
            } else if (muzixi && consumed > 1) {
                addToTop(new GainVitalityAction(player, consumed / 2));
            }
            addToTop(new DamageAllEnemiesAction(player, fixedDamageMatrix(damage),
                    DamageInfo.DamageType.HP_LOSS, AttackEffect.FIRE));
        } else if (muzixi && consumed > 1) {
            addToTop(new GainVitalityAction(player, consumed / 2));
        }
        isDone = true;
    }

    private static int safeMultiply(int a, int b) {
        long result = (long) a * b;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }

    private static int[] fixedDamageMatrix(int damage) {
        int[] matrix = new int[AbstractDungeon.getMonsters().monsters.size()];
        Arrays.fill(matrix, Math.max(0, damage));
        return matrix;
    }
}
