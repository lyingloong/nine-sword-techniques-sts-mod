package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import ninesword.muzixi.actions.GainVitalityAction;

public class MuzixiPersonaPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:MuzixiPersona";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public MuzixiPersonaPower(AbstractCreature owner) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        amount = 1;
        type = PowerType.BUFF;
        loadIcons("MuzixiPersona");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new GainBlockAction(owner, owner, 3));
        addToBot(new GainVitalityAction(owner, 1));
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0];
    }
}
