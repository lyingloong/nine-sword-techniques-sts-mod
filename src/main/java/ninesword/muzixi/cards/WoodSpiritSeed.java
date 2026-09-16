package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.ParalysisPower;

public class WoodSpiritSeed extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:WoodSpiritSeed";

    public WoodSpiritSeed() {
        super(ID, "WoodSpiritSeed", 0, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = 1;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(monster, player,
                new ParalysisPower(monster, magicNumber), magicNumber));
        gainVitality(player, magicNumber);
    }

    @Override
    public void triggerWhenDrawn() {
        addToBot(new MakeTempCardInDiscardAction(makeStatEquivalentCopy(), 1));
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
        return new WoodSpiritSeed();
    }
}
