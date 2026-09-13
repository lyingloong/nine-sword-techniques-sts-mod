package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.NaturalCyclePower;

public class EyeOfLife extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:EyeOfLife";

    public EyeOfLife() {
        super(ID, "EyeOfLife", 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = 1;
        baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(player, player, new NaturalCyclePower(player, magicNumber), magicNumber));
        gainVitality(player, 1);
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
        return new EyeOfLife();
    }
}
