package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.VitalityPower;

public class LifeDrain extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:LifeDrain";

    public LifeDrain() {
        super(ID, "LifeDrain", 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 4;
        magicNumber = 2;
        baseMagicNumber = 2;
        damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        damage(player, monster, damage);
        if (hasVitality(player, 2)) {
            addToBot(new ReducePowerAction(player, player, VitalityPower.POWER_ID, 2));
            addToBot(new HealAction(player, player, magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(3);
            upgradeMagicNumber(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LifeDrain();
    }
}
