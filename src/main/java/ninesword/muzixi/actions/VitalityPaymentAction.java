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

    public VitalityPaymentAction(AbstractPlayer player, int cost,
                                 int heal, int energy, int cards) {
        this.player = player;
        this.cost = Math.max(0, cost);
        this.heal = Math.max(0, heal);
        this.energy = Math.max(0, energy);
        this.cards = Math.max(0, cards);
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (player == null || VitalityPower.getAmount(player) < cost
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
