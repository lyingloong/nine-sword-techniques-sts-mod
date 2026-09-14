package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import ninesword.muzixi.actions.EnterTearPersonaAction;

public class TearPersonaPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:TearPersona";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public TearPersonaPower(AbstractCreature owner) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        amount = 1;
        type = PowerType.BUFF;
        loadIcons("TearPersona");
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        if (owner != null && owner.isPlayer) {
            addToBot(new EnterTearPersonaAction((com.megacrit.cardcrawl.characters.AbstractPlayer) owner));
        }
    }

    @Override
    public void updateDescription() {
        description = STRINGS.DESCRIPTIONS[0];
    }
}
