package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import ninesword.muzixi.actions.VitalitySpendAction;

public class LivingArmorPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:LivingArmor";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public LivingArmorPower(AbstractCreature owner, int maximumSpend) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, maximumSpend);
        type = PowerType.BUFF;
        loadIcons("LivingArmor");
        updateDescription();
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (isPlayer && owner instanceof AbstractPlayer && amount > 0
                && VitalityPower.getAmount(owner) > 0) {
            flash();
            addToBot(new VitalitySpendAction((AbstractPlayer) owner, amount, 1,
                    VitalitySpendAction.Effect.PLATED_ARMOR));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }
}
