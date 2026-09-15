package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.VitalCycleAction;

public class VitalCycle extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:VitalCycle";
    private int threshold = 6;
    private int drawAmount = 1;

    public VitalCycle() {
        super(ID, "VitalCycle", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new VitalCycleAction(player, magicNumber, threshold, drawAmount));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            drawAmount = 2;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VitalCycle();
    }
}
