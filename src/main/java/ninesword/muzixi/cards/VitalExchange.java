package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.VitalitySpendAction;

public class VitalExchange extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:VitalExchange";
    private int maximumSpend = 2;

    public VitalExchange() {
        super(ID, "VitalExchange", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = 4;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new VitalitySpendAction(player, maximumSpend, magicNumber,
                VitalitySpendAction.Effect.HEAL));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VitalExchange();
    }
}
