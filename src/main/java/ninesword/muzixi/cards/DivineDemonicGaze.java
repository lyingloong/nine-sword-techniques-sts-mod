package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.HarmonicSoulPower;

public class DivineDemonicGaze extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:DivineDemonicGaze";

    public DivineDemonicGaze() {
        super(ID, "DivineDemonicGaze", 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        magicNumber = 2;
        baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(player, player, new HarmonicSoulPower(player, magicNumber), magicNumber));
        gainVitality(player, 3);
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
        return new DivineDemonicGaze();
    }
}
