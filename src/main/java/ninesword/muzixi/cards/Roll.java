package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Roll extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:Roll";
    private static final int BLOCK_TIMES = 2;

    public Roll() {
        super(ID, "Roll", 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 2;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        for (int i = 0; i < BLOCK_TIMES; i++) {
            addToBot(new GainBlockAction(player, player, block));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Roll();
    }
}
