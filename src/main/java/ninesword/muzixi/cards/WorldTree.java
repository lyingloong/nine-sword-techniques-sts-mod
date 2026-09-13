package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.WorldTreeAction;

public class WorldTree extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:WorldTree";

    public WorldTree() {
        super(ID, "WorldTree", 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        magicNumber = 6;
        baseMagicNumber = 6;
        baseBlock = 4;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new WorldTreeAction(player, magicNumber, block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(2);
            upgradeBlock(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WorldTree();
    }
}
