package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class AncientBarkPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:AncientBark";
    private static final int THRESHOLD = 4;
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public AncientBarkPower(AbstractCreature owner, int amount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        loadIcons("AncientBark");
        updateDescription();
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (VitalityPower.getAmount(owner) >= THRESHOLD) {
            flash();
            addToBot(new GainBlockAction(owner, owner, amount));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], THRESHOLD, amount);
    }
}
