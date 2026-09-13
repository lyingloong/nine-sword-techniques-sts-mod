package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RootGuard extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:RootGuard";

    public RootGuard() {
        super(ID, "RootGuard", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 8;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        block(player, block);
        if (isMuzixi(player)) {
            gainVitality(player, 1);
        }
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
        return new RootGuard();
    }
}
