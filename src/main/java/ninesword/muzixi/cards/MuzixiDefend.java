package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MuzixiDefend extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:MuzixiDefend";

    public MuzixiDefend() {
        super(ID, "MuzixiDefend", 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        baseBlock = 5;
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        block(player, block);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(3);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MuzixiDefend();
    }
}
