package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import ninesword.muzixi.powers.VitalityPower;

public class WorldTreeAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final int maximumSpend;
    private final int blockPerVitality;

    public WorldTreeAction(AbstractPlayer player, int maximumSpend, int blockPerVitality) {
        this.player = player;
        this.maximumSpend = maximumSpend;
        this.blockPerVitality = blockPerVitality;
        actionType = ActionType.BLOCK;
    }

    @Override
    public void update() {
        int spent = Math.min(maximumSpend, VitalityPower.getAmount(player));
        if (spent > 0) {
            addToTop(new GainBlockAction(player, player, spent * blockPerVitality));
            addToTop(new ReducePowerAction(player, player, VitalityPower.POWER_ID, spent));
        }
        isDone = true;
    }
}
