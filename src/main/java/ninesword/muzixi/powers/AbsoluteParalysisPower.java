package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

/** Causes only half (rounded up) of Paralysis to expire at turn end. */
public class AbsoluteParalysisPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:AbsoluteParalysis";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public AbsoluteParalysisPower(AbstractCreature owner) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        type = PowerType.BUFF;
        loadIcons("AbsoluteParalysis");
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        // This is a non-numeric rule modifier; additional copies have no
        // further effect and should not display a misleading stack count.
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0];
    }
}
