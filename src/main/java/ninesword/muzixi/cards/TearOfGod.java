package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import ninesword.muzixi.powers.VitalityPower;

public class TearOfGod extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:TearOfGod";

    public TearOfGod() {
        super(ID, "TearOfGod", 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 18;
        magicNumber = 3;
        baseMagicNumber = 3;
        damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        damage(player, monster, damage, com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.FIRE);
        if (isTear(player)) {
            int spend = Math.min(magicNumber, VitalityPower.getAmount(player));
            if (spend > 0) {
                addToBot(new ReducePowerAction(player, player, VitalityPower.POWER_ID, spend));
                addToBot(new ApplyPowerAction(monster, player, new WeakPower(monster, spend, false), spend));
                addToBot(new ApplyPowerAction(monster, player, new VulnerablePower(monster, spend, false), spend));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(7);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TearOfGod();
    }
}
