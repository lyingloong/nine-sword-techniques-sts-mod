package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class ParalysisFlower extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:ParalysisFlower";

    public ParalysisFlower() {
        super(ID, "ParalysisFlower", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        magicNumber = 2;
        baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(monster, player, new WeakPower(monster, magicNumber, false), magicNumber));
        gainVitality(player, magicNumber);
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
        return new ParalysisFlower();
    }
}
