package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import ninesword.muzixi.cards.VitalityRiot;
import ninesword.muzixi.powers.VitalityPower;

/** Spends all Vitality, then deals a single normal hit based on the amount spent. */
public class VitalityRiotAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final VitalityRiot card;

    public VitalityRiotAction(AbstractPlayer player, VitalityRiot card) {
        this.player = player;
        this.card = card;
        actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (player != null && card != null) {
            int spent = VitalityPower.spend(player, VitalityPower.getAmount(player));
            if (spent > 0) {
                int[] damage = card.calculateDamageForVitality(spent);
                if (damage.length > 0) {
                    addToTop(new DamageAllEnemiesAction(player, damage,
                            DamageInfo.DamageType.NORMAL, AttackEffect.FIRE));
                }
            }
        }
        isDone = true;
    }
}
