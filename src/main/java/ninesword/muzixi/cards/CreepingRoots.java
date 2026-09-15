package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.ParalysisPower;

public class CreepingRoots extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:CreepingRoots";
    private int vitalityAmount = 1;

    public CreepingRoots() {
        super(ID, "CreepingRoots", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        magicNumber = 5;
        baseMagicNumber = 5;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(monster, player,
                new ParalysisPower(monster, magicNumber), magicNumber));
        gainVitality(player, vitalityAmount);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            vitalityAmount = 2;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CreepingRoots();
    }
}
