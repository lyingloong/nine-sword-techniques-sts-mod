package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class LivingArmorPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:LivingArmor";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int THRESHOLD = 8;

    public LivingArmorPower(AbstractCreature owner, int block) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, block);
        type = PowerType.BUFF;
        loadIcons("LivingArmor");
        updateDescription();
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (owner != null && owner.isPlayer == isPlayer && VitalityPower.getAmount(owner) >= THRESHOLD) {
            flash();
            addToBot(new GainBlockAction(owner, owner, amount));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], THRESHOLD, amount);
    }
}
