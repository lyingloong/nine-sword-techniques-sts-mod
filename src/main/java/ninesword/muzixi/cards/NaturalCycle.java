package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.NaturalCyclePower;

public class NaturalCycle extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:NaturalCycle";

    public NaturalCycle() {
        super(ID, "NaturalCycle", 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = 2;
        baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(player, player, new NaturalCyclePower(player, magicNumber), magicNumber));
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
        return new NaturalCycle();
    }
}
