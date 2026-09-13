package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.AncientBarkPower;

public class AncientBark extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:AncientBark";

    public AncientBark() {
        super(ID, "AncientBark", 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = 6;
        baseMagicNumber = 6;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(player, player, new AncientBarkPower(player, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(3);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new AncientBark();
    }
}
