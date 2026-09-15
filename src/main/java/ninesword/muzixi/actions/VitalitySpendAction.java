package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import ninesword.muzixi.powers.VitalityPower;

/** Resolves a card effect whose magnitude is the amount of Vitality actually spent. */
public class VitalitySpendAction extends AbstractGameAction {
    public enum Effect { BLOCK, HEAL, DRAW }

    private final AbstractPlayer player;
    private final int maximum;
    private final int multiplier;
    private final Effect effect;

    public VitalitySpendAction(AbstractPlayer player, int maximum, int multiplier, Effect effect) {
        this.player = player;
        this.maximum = Math.max(0, maximum);
        this.multiplier = Math.max(0, multiplier);
        this.effect = effect;
        actionType = effect == Effect.HEAL ? ActionType.HEAL
                : effect == Effect.DRAW ? ActionType.CARD_MANIPULATION
                : ActionType.BLOCK;
    }

    @Override
    public void update() {
        if (player == null) {
            isDone = true;
            return;
        }
        int spent = VitalityPower.spend(player, maximum);
        int value = safeMultiply(spent, multiplier);
        if (value > 0) {
            if (effect == Effect.BLOCK) {
                addToTop(new GainBlockAction(player, player, value));
            } else if (effect == Effect.HEAL) {
                addToTop(new HealAction(player, player, value));
            } else if (effect == Effect.DRAW) {
                addToTop(new DrawCardAction(player, value));
            }
        }
        isDone = true;
    }

    private static int safeMultiply(int left, int right) {
        long result = (long) left * right;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }
}
