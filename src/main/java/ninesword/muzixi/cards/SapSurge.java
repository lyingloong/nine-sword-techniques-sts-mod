package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SapSurge extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:SapSurge";

    public SapSurge() {
        super(ID, "SapSurge", 2, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        baseDamage = 10;
        magicNumber = 2;
        baseMagicNumber = 2;
        damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        damage(player, monster, damage);
        gainVitality(player, magicNumber);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(2);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SapSurge();
    }
}
