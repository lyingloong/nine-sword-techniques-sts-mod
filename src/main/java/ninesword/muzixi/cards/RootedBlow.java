package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.RootedBlowAction;

public class RootedBlow extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:RootedBlow";
    private int maximumSpend = 3;
    private int extraDamage = 4;

    public RootedBlow() {
        super(ID, "RootedBlow", 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 8;
        damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int[] damageBySpentVitality = new int[maximumSpend + 1];
        for (int spent = 0; spent <= maximumSpend; spent++) {
            long totalBase = (long) baseDamage + (long) spent * extraDamage;
            int dynamicBase = totalBase >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) totalBase;
            damageBySpentVitality[spent] = calculateDamageForBase(monster, dynamicBase);
        }
        addToBot(new RootedBlowAction(player, monster, damageBySpentVitality));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(3);
            extraDamage = 5;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RootedBlow();
    }
}
