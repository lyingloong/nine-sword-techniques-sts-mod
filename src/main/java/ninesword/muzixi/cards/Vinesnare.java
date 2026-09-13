package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Vinesnare extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:Vinesnare";

    public Vinesnare() {
        super(ID, "Vinesnare", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        baseBlock = 5;
        magicNumber = 2;
        baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        block(player, block);
        addToBot(new ApplyPowerAction(monster, player, new WeakPower(monster, magicNumber, false), magicNumber));
        addToBot(new ApplyPowerAction(monster, player, new VulnerablePower(monster, magicNumber, false), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(3);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Vinesnare();
    }
}
