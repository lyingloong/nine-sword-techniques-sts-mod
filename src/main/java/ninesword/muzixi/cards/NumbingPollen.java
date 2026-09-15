package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import ninesword.muzixi.powers.ParalysisPower;

public class NumbingPollen extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:NumbingPollen";
    public NumbingPollen() {
        super(ID, "NumbingPollen", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = 5;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(monster, player,
                new ParalysisPower(monster, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(monster, player,
                new VulnerablePower(monster, 1, false), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NumbingPollen();
    }
}
