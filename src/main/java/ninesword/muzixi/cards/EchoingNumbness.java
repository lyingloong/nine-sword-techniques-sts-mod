package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import ninesword.muzixi.powers.ParalysisPower;

public class EchoingNumbness extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:EchoingNumbness";
    public EchoingNumbness() {
        super(ID, "EchoingNumbness", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = 4;
        returnToHand = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(monster, player,
                new ParalysisPower(monster, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EchoingNumbness();
    }
}
