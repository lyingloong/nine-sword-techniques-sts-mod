package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class HarmonicSoulPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:HarmonicSoul";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public HarmonicSoulPower(AbstractCreature owner, int drawAmount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        amount = drawAmount;
        type = PowerType.BUFF;
        loadIcons("HarmonicSoul");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }
}
