package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import ninesword.muzixi.actions.VitalitySpendAction;

public class NaturalCyclePower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:NaturalCycle";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public NaturalCyclePower(AbstractCreature owner, int amount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        loadIcons("NaturalCycle");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (owner instanceof AbstractPlayer && amount > 0 && VitalityPower.getAmount(owner) > 0) {
            flash();
            addToBot(new VitalitySpendAction((AbstractPlayer) owner, amount, 1,
                    VitalitySpendAction.Effect.DRAW));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }
}
