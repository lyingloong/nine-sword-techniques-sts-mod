package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.EvergreenPower;

public class SoulReflection extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:SoulReflection";

    public SoulReflection() {
        super(ID, "SoulReflection", 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        magicNumber = 3;
        baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(player, player, new EvergreenPower(player, magicNumber), magicNumber));
        gainVitality(player, 2);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SoulReflection();
    }
}
