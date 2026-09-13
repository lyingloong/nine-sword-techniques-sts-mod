package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LeafDance extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:LeafDance";

    public LeafDance() {
        super(ID, "LeafDance", 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        magicNumber = 2;
        baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        gainVitality(player, magicNumber);
        addToBot(new DrawCardAction(1));
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
        return new LeafDance();
    }
}
