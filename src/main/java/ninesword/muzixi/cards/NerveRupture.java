package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.NerveRuptureAction;
import ninesword.muzixi.powers.ParalysisPower;

public class NerveRupture extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:NerveRupture";
    private int damagePerStack = 2;

    public NerveRupture() {
        super(ID, "NerveRupture", 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 16;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int paralysis = ParalysisPower.getAmount(monster);
        long scaledBase = (long) baseDamage + (long) paralysis * damagePerStack;
        int dynamicBase = scaledBase >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) scaledBase;
        int finalDamage = calculateDamageForBase(monster, dynamicBase);
        addToBot(new NerveRuptureAction(player, monster, finalDamage));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
            damagePerStack = 3;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NerveRupture();
    }
}
