package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import ninesword.muzixi.powers.VitalityPower;

/** Resolves a card effect whose magnitude is the amount of Vitality actually spent. */
public class VitalitySpendAction extends AbstractGameAction {
    /**
     * The reward to resolve for each point of Vitality actually spent.
     * DRAW_AND_ENERGY is used by Natural Cycle, whose two rewards must be
     * based on the same atomic payment (spending twice would be incorrect).
     */
    public enum Effect { BLOCK, HEAL, DRAW, ENERGY, DRAW_AND_ENERGY }

    private final AbstractPlayer player;
    private final int maximum;
    private final int multiplier;
    private final Effect effect;
    private final int energyMultiplier;

    public VitalitySpendAction(AbstractPlayer player, int maximum, int multiplier, Effect effect) {
        this(player, maximum, multiplier, effect,
                effect == Effect.ENERGY || effect == Effect.DRAW_AND_ENERGY ? multiplier : 0);
    }

    /**
     * Construct a payment with an independent energy reward.  The overload is
     * intentionally kept alongside the original constructor so existing card
     * implementations remain source-compatible.
     */
    public VitalitySpendAction(AbstractPlayer player, int maximum, int multiplier,
                               Effect effect, int energyMultiplier) {
        this.player = player;
        this.maximum = Math.max(0, maximum);
        this.multiplier = Math.max(0, multiplier);
        this.effect = effect;
        this.energyMultiplier = Math.max(0, energyMultiplier);
        actionType = effect == Effect.HEAL ? ActionType.HEAL
                : effect == Effect.DRAW || effect == Effect.DRAW_AND_ENERGY
                ? ActionType.CARD_MANIPULATION
                : effect == Effect.ENERGY ? ActionType.POWER : ActionType.BLOCK;
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
            } else if (effect == Effect.DRAW || effect == Effect.DRAW_AND_ENERGY) {
                addToTop(new DrawCardAction(player, value));
            }
        }
        if ((effect == Effect.ENERGY || effect == Effect.DRAW_AND_ENERGY)
                && energyMultiplier > 0 && spent > 0) {
            addToTop(new GainEnergyAction(safeMultiply(spent, energyMultiplier)));
        }
        isDone = true;
    }

    private static int safeMultiply(int left, int right) {
        long result = (long) left * right;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }
}
