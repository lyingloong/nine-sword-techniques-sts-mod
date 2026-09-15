package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import ninesword.muzixi.powers.VitalityPower;

/** Atomically pays a fixed Vitality cost before queueing its rewards. */
public class VitalityPaymentAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int cost;
    private final int heal;
    private final int energy;
    private final int cards;
    /** Optional balance that must remain available when the payment resolves. */
    private final int minimumVitality;

    public VitalityPaymentAction(AbstractPlayer player, int cost,
                                 int heal, int energy, int cards) {
        this(player, cost, heal, energy, cards, 0);
    }

    /**
     * Variant used by threshold cards (for example Vitality Distillation).
     * Checking the threshold in the action, rather than while the card is
     * played, keeps the result correct when earlier queued effects alter the
     * player's Vitality before this payment resolves.
     */
    public VitalityPaymentAction(AbstractPlayer player, int cost,
                                 int heal, int energy, int cards,
                                 int minimumVitality) {
        this.player = player;
        this.cost = Math.max(0, cost);
        this.heal = Math.max(0, heal);
        this.energy = Math.max(0, energy);
        this.cards = Math.max(0, cards);
        this.minimumVitality = Math.max(0, minimumVitality);
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (player == null || VitalityPower.getAmount(player) < minimumVitality
                || VitalityPower.getAmount(player) < cost
                || VitalityPower.spend(player, cost) != cost) {
            isDone = true;
            return;
        }

        // Insert in reverse order so rewards resolve together before any
        // actions that were already queued behind this payment.
        if (cards > 0) {
            addToTop(new DrawCardAction(player, cards));
        }
        if (energy > 0) {
            addToTop(new GainEnergyAction(energy));
        }
        if (heal > 0) {
            addToTop(new HealAction(player, player, heal));
        }
        isDone = true;
    }
}
