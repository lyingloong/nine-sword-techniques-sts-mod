package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RootGuard extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:RootGuard";

    public RootGuard() {
        super(ID, "RootGuard", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 7;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        block(player, block);
        if (isMuzixi(player)) {
            gainVitality(player, upgraded ? 2 : 1);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(2);
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RootGuard();
    }
}
