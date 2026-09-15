package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.EchoingNumbnessAction;

public class EchoingNumbness extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:EchoingNumbness";
    private int threshold = 4;

    public EchoingNumbness() {
        super(ID, "EchoingNumbness", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new EchoingNumbnessAction(player, monster, magicNumber, threshold, 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            threshold = 3;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EchoingNumbness();
    }
}
