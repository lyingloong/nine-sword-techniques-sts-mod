package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.ParalysisThresholdAction;
import ninesword.muzixi.powers.ParalysisPower;

public class NerveLock extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:NerveLock";
    private int weakAmount = 2;

    public NerveLock() {
        super(ID, "NerveLock", 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = 4;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(monster, player,
                new ParalysisPower(monster, magicNumber), magicNumber));
        addToBot(new ParalysisThresholdAction(player, monster, 4, weakAmount, 0));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            weakAmount = 3;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NerveLock();
    }
}
