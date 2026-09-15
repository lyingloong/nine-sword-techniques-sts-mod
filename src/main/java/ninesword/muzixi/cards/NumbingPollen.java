package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.ParalysisPower;
import ninesword.muzixi.powers.VitalityPower;

public class NumbingPollen extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:NumbingPollen";
    private int bonusVitality = 1;

    public NumbingPollen() {
        super(ID, "NumbingPollen", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        boolean alreadyParalyzed = monster != null && ParalysisPower.getAmount(monster) > 0;
        addToBot(new ApplyPowerAction(monster, player,
                new ParalysisPower(monster, magicNumber), magicNumber));
        if (alreadyParalyzed) {
            gainVitality(player, bonusVitality);
        }
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
        return new NumbingPollen();
    }
}
