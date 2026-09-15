package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class ParasiticSeed extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:ParasiticSeed";

    public ParasiticSeed() {
        super(ID, "ParasiticSeed", 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 2;
        magicNumber = 2;
        baseMagicNumber = 2;
        damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        damage(player, monster, damage);
        addToBot(new ApplyPowerAction(monster, player, new PoisonPower(monster, player, magicNumber), magicNumber));
        gainVitality(player, 2);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(2);
            upgradeMagicNumber(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ParasiticSeed();
    }
}
