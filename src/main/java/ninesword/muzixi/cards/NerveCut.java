package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.ParalysisPower;

public class NerveCut extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:NerveCut";
    private int threshold = 3;
    private int bonusDamage = 5;

    public NerveCut() {
        super(ID, "NerveCut", 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 9;
        magicNumber = baseMagicNumber = 3;
        damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int finalDamage = damage;
        if (monster != null && ParalysisPower.getAmount(monster) >= threshold) {
            long totalBase = (long) baseDamage + bonusDamage;
            int dynamicBase = totalBase >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) totalBase;
            finalDamage = calculateDamageForBase(monster, dynamicBase);
        }
        damage(player, monster, finalDamage,
                com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(3);
            bonusDamage = 7;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NerveCut();
    }
}
