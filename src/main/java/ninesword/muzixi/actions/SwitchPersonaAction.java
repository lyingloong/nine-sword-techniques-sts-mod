package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import ninesword.muzixi.powers.HarmonicSoulPower;
import ninesword.muzixi.powers.MuzixiPersonaPower;
import ninesword.muzixi.powers.TearPersonaPower;

public class SwitchPersonaAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public SwitchPersonaAction(AbstractPlayer player) {
        this.player = player;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        boolean hasMuzixiPersona = player.hasPower(MuzixiPersonaPower.POWER_ID);
        boolean hasTearPersona = player.hasPower(TearPersonaPower.POWER_ID);

        if (hasMuzixiPersona) {
            queueSwitch(new TearPersonaPower(player), hasMuzixiPersona, hasTearPersona);
        } else {
            queueSwitch(new MuzixiPersonaPower(player), hasMuzixiPersona, hasTearPersona);
        }

        if ((hasMuzixiPersona || hasTearPersona) && player.hasPower(HarmonicSoulPower.POWER_ID)) {
            int drawAmount = player.getPower(HarmonicSoulPower.POWER_ID).amount;
            addToTop(new GainEnergyAction(1));
            addToTop(new DrawCardAction(player, drawAmount));
        }
        isDone = true;
    }

    private void queueSwitch(com.megacrit.cardcrawl.powers.AbstractPower nextPersona,
                             boolean removeMuzixi, boolean removeTear) {
        addToTop(new ApplyPowerAction(player, player, nextPersona, 1));
        // Remove both markers defensively before applying the next one. Normal play only has one.
        if (removeTear) {
            addToTop(new RemoveSpecificPowerAction(player, player, TearPersonaPower.POWER_ID));
        }
        if (removeMuzixi) {
            addToTop(new RemoveSpecificPowerAction(player, player, MuzixiPersonaPower.POWER_ID));
        }
    }
}
