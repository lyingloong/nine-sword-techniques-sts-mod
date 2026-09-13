package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class VineLash extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:VineLash";

    public VineLash() {
        super(ID, "VineLash", 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 7;
        magicNumber = 2;
        baseMagicNumber = 2;
        damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        damage(player, monster, damage);
        if (monster.hasPower(VulnerablePower.POWER_ID)) {
            addToBot(new ApplyPowerAction(monster, player, new PoisonPower(monster, player, magicNumber), magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(3);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VineLash();
    }
}
